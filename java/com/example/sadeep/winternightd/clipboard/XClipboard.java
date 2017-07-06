package com.example.sadeep.winternightd.clipboard;

import android.content.Context;

import com.example.sadeep.winternightd.field.fields.Field;
import com.example.sadeep.winternightd.field.fields.SimpleIndentedField;
import com.example.sadeep.winternightd.note.Note;
import com.example.sadeep.winternightd.notebookactivity.NotebookActivity;
import com.example.sadeep.winternightd.textboxes.XEditText;
import com.example.sadeep.winternightd.selection.CursorPosition;
import com.example.sadeep.winternightd.selection.XSelection;

import java.util.Vector;

/**
 * Created by Sadeep on 12/27/2016.
 */
final public class XClipboard {
    private XClipboard(){}

    public static CharSequence clipedSelectionStartText;
    public static Vector<Field> clipedSelectionFields;

    public static void copySelectionToClipboard(){
        if(!XSelection.isSelectionAvailable())return;

        CursorPosition start = XSelection.getSelectionStart();
        CursorPosition end = XSelection.getSelectionEnd();

        Note note = XSelection.getSelectedNote();

        Vector<Object> fieldsandstrings = new Vector<>();


        if(start.fieldIndex==end.fieldIndex){
            fieldsandstrings.add(note.getFieldAt(start.fieldIndex).duplicateSelection(start.characterIndex,end.characterIndex));
        }
        else {
            for(int i=start.fieldIndex;i<=end.fieldIndex;i++){
                int a=-2,b=-1;

                if(i==start.fieldIndex)a=start.characterIndex;
                if(i==end.fieldIndex)b=end.characterIndex;

                Object o = note.getFieldAt(i).duplicateSelection(a,b);
                fieldsandstrings.add(o);
            }
        }

        clipedSelectionStartText=null;
        clipedSelectionFields=new Vector<Field>();

        for(int c=0;c<fieldsandstrings.size();c++){
            if(fieldsandstrings.get(c) instanceof CharSequence)clipedSelectionStartText=(CharSequence)fieldsandstrings.get(c);
            else clipedSelectionFields.add((Field)fieldsandstrings.get(c));
        }

    }

    public static void copyClipboardToCurrentCursor(Context context) {

        Note note =null;
        if(context instanceof NotebookActivity){
            note = ((NotebookActivity)context).getActiveNote();
        }
        CursorPosition cp = note.getCurrentCursorPosition();

        if(cp.isInternal()){
            SimpleIndentedField field = (SimpleIndentedField) note.getFieldAt(cp.fieldIndex);
            XEditText edittext = (XEditText) field.getMainTextBox();


            CharSequence oldtext = edittext.getText();
            CharSequence textbegin = android.text.TextUtils.concat(oldtext.subSequence(0,cp.characterIndex),XClipboard.clipedSelectionStartText);
            CharSequence textend = oldtext.subSequence(cp.characterIndex,oldtext.length());

            edittext.setText(textbegin);

            int c=0;
            for(c=0;c<XClipboard.clipedSelectionFields.size();c++){
                note.addView(clipedSelectionFields.get(c).duplicate(),cp.fieldIndex+c+1);
            }

            if(note.getFieldAt(cp.fieldIndex+c)instanceof SimpleIndentedField){
                XEditText xEditText = (XEditText) ((SimpleIndentedField)note.getFieldAt(cp.fieldIndex+c)).getMainTextBox();
                int n=xEditText.length();
                xEditText.setText(android.text.TextUtils.concat(xEditText.getText(),textend));
                xEditText.requestFocus();
                xEditText.setSelection(n);
            }

        }
    }
}
