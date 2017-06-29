package com.example.sadeep.winternightd.note;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.text.Spannable;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sadeep.winternightd.activities.BottomBar;
import com.example.sadeep.winternightd.dumping.FieldDataStream;
import com.example.sadeep.winternightd.field.FieldFactory;
import com.example.sadeep.winternightd.field.fields.CheckedField;
import com.example.sadeep.winternightd.textboxes.XEditText;
import com.example.sadeep.winternightd.spans.SpansFactory;
import com.example.sadeep.winternightd.field.fields.Field;
import com.example.sadeep.winternightd.field.fields.NumberedField;
import com.example.sadeep.winternightd.field.fields.SimpleIndentedField;
import com.example.sadeep.winternightd.field.SingleText;
import com.example.sadeep.winternightd.misc.Globals;
import com.example.sadeep.winternightd.spans.RichText;
import com.example.sadeep.winternightd.selection.CursorPosition;

/**
 * Created by Sadeep on 10/18/2016.
 */

/**
 * A note in WN, this is a collection of Fields. (a Note may only have Fields as children)
 */
public class Note extends LinearLayout {


    private boolean isEditable;
    public static final int defaultFieldType = CheckedField.classFieldType;


    private View scrollableParent;



    Note(Context context,boolean isEditable, boolean isNewNote, View scrollableParent) {
        super(context);
        this.scrollableParent = scrollableParent;

        init(isEditable);
        if(isNewNote) convertToNewNoteWithOneDefaultField();
    }

    public Note(Context context, AttributeSet attrs) {
        this(context, true,true,null,attrs);
    }

    public Note(Context context,boolean isEditable, boolean isNewNote,View scrollableParent, AttributeSet attrs) {
        super(context,attrs);
        this.scrollableParent = scrollableParent;
        init(isEditable);
        if(isNewNote) convertToNewNoteWithOneDefaultField();
    }

    private void init(boolean isEditable) {

        setOrientation(VERTICAL);
        //setPadding(0,0,0,0);
        setBackgroundColor(Color.TRANSPARENT);
        setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        this.isEditable = isEditable;
    }


//general methods

    public View getScrollableParent() {
        return scrollableParent;
    }

    public void convertToNewNoteWithOneDefaultField(){
        removeAllViews();
        addView(FieldFactory.createNewField(getContext(), defaultFieldType,true));
    }

    public Field getFieldAt(int index){
        Field f = null;
        try{
            f = (Field)getChildAt(index);
        }catch (Exception e){}
        return f;
    }

    public int getFieldCount(){
        return getChildCount();
    }

    /**
     * We make sure that the added child is a Field, because a Note is intended to hold only Fields as child views.
     * It would cause bugs in various classes if a non-Field child is available as a child in the Note. So here we prevent this.
     *
     * We make the added Field is editable if the note is editable and vice versa.
     */
    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params){

        Field f = null;
        try{
            f = (Field)child;
        }catch (Exception e){}

        if(f == null) return; //if child type is not Field stop the procedure

        f.setIsEditable(isEditable);

        super.addView(child,index,params);

        f.onAttachedToNote();
        f.refreshThisAndAllFieldsBelowToAccountForNoteChanges();
    }

    @Override
    public void removeView(View view) {
        int index = indexOfChild(view);
        removeViewAt(index);
    }
    @Override
    public void removeViewAt(int index) {
        super.removeViewAt(index);

        if(getFieldCount()-1>=index)//if the removed is not the bottom most Field
            getFieldAt(index).refreshThisAndAllFieldsBelowToAccountForNoteChanges();//refresh the new comer to the formers index and below
    }

    public boolean isEmpty(){
        if(getFieldCount()!=1)return false;
        Field f = getFieldAt(0);
        if(f.getFieldType()!=Note.defaultFieldType)return false;
        SimpleIndentedField s = (SimpleIndentedField)f;
        if(s.getMainTextBox().getText().length()!=0)return false;

        return true;
    }

    public FocusListener focusListener=null;
    public void onFocused() {
        if(focusListener!=null)focusListener.onFocused();
    }
    public interface FocusListener{void onFocused();}

//editability related methods

    public void setIsEditable(boolean isEditable){

        if(this.isEditable == isEditable)return;
        this.isEditable = isEditable;

        for(int c = 0; c < getChildCount(); c++){
            Field f = getFieldAt(c);
            if(f != null)f.setIsEditable(isEditable);
        }

    }

    public boolean getIsEditable() {
        return isEditable;
    }




//selection and clipboard related methods

    public CursorPosition getCursorPosition(){
        if(getFocusedChild()==null)return null;
        if(getFocusedChild()instanceof SimpleIndentedField) {
            return new CursorPosition(indexOfChild(getFocusedChild()),((SimpleIndentedField) getFocusedChild()).getMainTextBox().getSelectionStart());
        }
        return null;
    }

    public void setCursorPosition(CursorPosition pos) {
        Field f = getFieldAt(pos.fieldIndex);
        if(f instanceof SimpleIndentedField){
            XEditText tv = (XEditText) ((SimpleIndentedField) f).getMainTextBox();
            tv.requestFocus();
            tv.setSelection(pos.characterIndex);
        }
    }

    public void setCursorVisible(boolean visible){
        //for each Field in Note set the visibility to the expected value
        for(int c=0;c<getChildCount();c++){
            getFieldAt(c).setCursorVisible(visible);
        }
    }

    public void highlightSelection(CursorPosition start, CursorPosition end) {

        /**
         * We first remove all the highlightings; that is,
         *  we set the Field background color to transparent
         *  we remove all XSelectionSpans
         */
        for(int c=0;c<getChildCount();c++){
            getFieldAt(c).setBackgroundColor(Color.TRANSPARENT);
            if(getFieldAt(c)instanceof SingleText){
                Spannable spannable = (Spannable) ((SingleText) getFieldAt(c)).getMainTextBox().getText();
                Object[] spans = spannable.getSpans(0, spannable.length(), SpansFactory.XSelectionSpan.class);

                for (Object x : spans) spannable.removeSpan(x);
                ((SingleText) getFieldAt(c)).getMainTextBox().invalidate();
            }
        }

        if(start.equals(end))return;
        /**
         * Now we add spans to suit start and end.
         */
        if(start.characterIndex==end.characterIndex && start.fieldIndex==end.fieldIndex)return;

        for(int c=start.fieldIndex+1;c<end.fieldIndex;c++){
            getFieldAt(c).setBackgroundColor(Globals.defaultHighlightColor);
        }

        if(start.fieldIndex==end.fieldIndex){
            Field field = getFieldAt(start.fieldIndex);
            if(start.characterIndex>=0&&end.characterIndex>=0) {
                if(!(field instanceof SingleText))return;
                TextView tv = ((SingleText) getFieldAt(start.fieldIndex)).getMainTextBox();
                Spannable spannable = (Spannable) tv.getText();
                spannable.setSpan(SpansFactory.createSpan(SpansFactory.XSelectionSpan.spanType),start.characterIndex,end.characterIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv.invalidate();
            }
            else if (start.characterIndex==-2 && end.characterIndex ==-1)field.setBackgroundColor(Globals.defaultHighlightColor);
        }
        else {
            if(start.characterIndex==-2) getFieldAt(start.fieldIndex).setBackgroundColor(Globals.defaultHighlightColor);
            else if(start.characterIndex>=0) {
                Spannable spannable = (Spannable)((SingleText) getFieldAt(start.fieldIndex)).getMainTextBox().getText();
                spannable.setSpan(SpansFactory.createSpan(SpansFactory.XSelectionSpan.spanType),start.characterIndex,spannable.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ((SingleText) getFieldAt(start.fieldIndex)).getMainTextBox().invalidate();
            }

            if(end.characterIndex==-1) getFieldAt(end.fieldIndex).setBackgroundColor(Globals.defaultHighlightColor);
            else if(end.characterIndex>=0) {
                Spannable spannable = (Spannable)((SingleText) getFieldAt(end.fieldIndex)).getMainTextBox().getText();
                spannable.setSpan(SpansFactory.createSpan(SpansFactory.XSelectionSpan.spanType),0,end.characterIndex,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ((SingleText) getFieldAt(end.fieldIndex)).getMainTextBox().invalidate();
            }
        }
    }
    /**
     *  Insert a CharSequence to the note at the CursorPosition specified by start.
     */
    public void insertCharSequenceAt(CursorPosition start, CharSequence text) {
        //todo here accounting only for when start is at a SingleText Field && isInternal

        SingleText f;
        try{
            f = ((SingleText) getFieldAt(start.fieldIndex));
        }catch(Exception e){return;}
        if (!start.isInternal())return;

        CharSequence seq1 = f.getMainTextBox().getText().subSequence(0,start.characterIndex);
        CharSequence seq2 = f.getMainTextBox().getText().subSequence(start.characterIndex,f.getMainTextBox().length());

        f.getMainTextBox().setText(android.text.TextUtils.concat(seq1,text,seq2));

    }

    public void deleteContent(CursorPosition start, CursorPosition end){
        if(start.equals(end))return;

        while(end.fieldIndex >= start.fieldIndex+2){
            removeViewAt(start.fieldIndex+1);
            end.fieldIndex--;
        }

        if(start.fieldIndex==end.fieldIndex){
            if(!start.isInternal()){
                //not implemented
            }
            else{
                TextView tv =((SimpleIndentedField) getFieldAt(start.fieldIndex)).getMainTextBox();
                CharSequence oldtext = tv.getText();
                CharSequence seq1 =  oldtext.subSequence(0,start.characterIndex);
                CharSequence seq2 =  oldtext.subSequence(end.characterIndex,oldtext.length());

                seq1 = RichText.getCharSequence(RichText.generateRichText((Spanned)seq1));
                seq2 = RichText.getCharSequence(RichText.generateRichText((Spanned)seq2));
                CharSequence newtext = android.text.TextUtils.concat(seq1,seq2);
                tv.setText(newtext);
            }
        }
        else if (start.fieldIndex==end.fieldIndex-1){
            if(start.isInternal()&&end.isInternal()){

                TextView tv1 =((SimpleIndentedField) getFieldAt(start.fieldIndex)).getMainTextBox();
                CharSequence text1 = tv1.getText().subSequence(0,start.characterIndex);
                TextView tv2 =((SimpleIndentedField) getFieldAt(end.fieldIndex)).getMainTextBox();
                CharSequence text2 = tv2.getText().subSequence(end.characterIndex,tv2.length());

                tv1.setText(android.text.TextUtils.concat(text1,text2));

                removeViewAt(end.fieldIndex);
            }
        }

    }

    public Point getAbsoluteCoordinatesForCursorPosition(CursorPosition cpos) {
        return (getFieldAt(cpos.fieldIndex)).getAbsoluteCoordinatesForCharacterIndex(cpos.characterIndex);
    }

    public CursorPosition cursorPositionForCoordinate(Point absoluteCoordinate) {
        int rawX=absoluteCoordinate.x, rawY=absoluteCoordinate.y;

        for(int c = 0; c < getChildCount(); c++)
        {
            Field childField = getFieldAt(c);

            int[] xy = new int[2];
            childField.getLocationInWindow(xy);

            Rect rect = new Rect(xy[0], xy[1], xy[0]+childField.getWidth(), xy[1]+childField.getHeight());

            if (rect.contains(rawX, rawY))
            {
                int characterIndex =  childField.characterPositionForCoordinate(new Point(rawX, rawY));
                return new CursorPosition(c,characterIndex);
            }

            if(c==0){
                if(rawY<xy[1])return new CursorPosition(c,childField.characterPositionForCoordinate(new Point(rawX, rawY)));
            }
            if(c==getChildCount()-1){
                if(rawY>xy[1]+childField.getHeight())return new CursorPosition(c,childField.characterPositionForCoordinate(new Point(rawX, rawY)));
            }
        }

        return new CursorPosition(-1, CursorPosition.CHARACTERINDEX_ERROR);
    }

    public void setScrollableParent(View scrollableParent) {
        this.scrollableParent = scrollableParent;
    }




//dumping related methods

    public FieldDataStream getFieldDataStream(){
        FieldDataStream stream = new FieldDataStream();
        writeToFieldDataStream(stream);
        return stream;
    }

    public void writeToFieldDataStream(FieldDataStream stream){
        for(int c=0;c<getFieldCount();c++){
            Field field = getFieldAt(c);
            field.writeToFieldDataStream(stream);
        }
    }

    public void readFromFieldDataStream(FieldDataStream stream){
        while (!stream.endOfStream())
            addView(FieldFactory.fromFieldDataStream(getContext(), stream, isEditable));
    }

}
