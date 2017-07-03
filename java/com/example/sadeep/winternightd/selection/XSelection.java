package com.example.sadeep.winternightd.selection;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.view.ViewTreeObserver;

import com.example.sadeep.winternightd.activities.ChangableActionBarActivity;
import com.example.sadeep.winternightd.field.FieldFactory;
import com.example.sadeep.winternightd.field.fields.SimpleIndentedField;
import com.example.sadeep.winternightd.note.Note;
import com.example.sadeep.winternightd.spans.SpansController;
import com.example.sadeep.winternightd.textboxes.XEditText;

/**
 * Created by Sadeep on 10/21/2016.
 */


/**
 * we don't use the built-in android selection system because we want to select not only text(checkboxes etc too) and
 * our selection may extend over several EditTexts.
 */
final public class XSelection {
    private static XEditText xEditText;

    private XSelection(){}

    private static  boolean selectionAvailable = false;
    private static Handle [] handles;
    private static Note note;

    private static ViewTreeObserver.OnPreDrawListener onPreDrawListener = new ViewTreeObserver.OnPreDrawListener() {
        @Override
        public boolean onPreDraw() {
            XSelection.refreshHandlePositions();
            note.clearFocus();
            note.requestFocus();
            xEditText.requestFocus();
            xEditText.setSelection(0);
            return true;
        }
    };

    public static boolean isSelectionAvailable() {
        return selectionAvailable;
    }

    public static Note getActiveNote(){
        return note;
    }
    public static void setActiveNote(Note note){
        XSelection.note = note;
    }

    public static CursorPosition getSelectionStart(){
        if(handles[0]==null||handles[1]==null||handles[0].getCursorPosition()==null||handles[1].getCursorPosition()==null)return null;
        return CursorPosition.min(handles[0].getCursorPosition(),handles[1].getCursorPosition());
    }
    public static CursorPosition getSelectionEnd(){
        if(handles[0]==null||handles[1]==null||handles[0].getCursorPosition()==null||handles[1].getCursorPosition()==null)return null;
        return CursorPosition.max(handles[0].getCursorPosition(),handles[1].getCursorPosition());
    }
    public static Note getSelectedNote(){
        return note;
    }


    public static void newSelection(Note note, CursorPosition pos1, CursorPosition pos2, XEditText xEditText){
        clearSelections();
        XSelection.note = note;
        XSelection.xEditText=xEditText;



        ColorFilter cf = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);

        //xEditText.clearFocus();
        SimpleIndentedField s= (SimpleIndentedField) FieldFactory.createNewField(note.getContext(), SimpleIndentedField.classFieldType,true);
        //note.addView(s);
        //s.getMainTextBox().requestFocus();
        ((XEditText)s.getMainTextBox()).setSelection(0);




        note.setCursorVisible(false);

        handles = new Handle[2];
        handles[0] = new Handle(note);
        handles[1] = new Handle(note);

        handles[0].updatePosition(pos1, true);
        handles[1].updatePosition(pos2, true);

        note.getViewTreeObserver().addOnPreDrawListener(onPreDrawListener);


        selectionAvailable = true;
        ((ChangableActionBarActivity)note.getContext()).changeActionBar(ChangableActionBarActivity.ACTIONBAR_SELECT);

    }

    public static void clearSelections() {

        if(note!=null)((ChangableActionBarActivity)note.getContext()).changeActionBar(0);

        try {
            handles[0].dismiss();
            handles[1].dismiss();
            note.getViewTreeObserver().removeOnPreDrawListener(onPreDrawListener);
            note.highlightSelection(new CursorPosition(0,0),new CursorPosition(0,0));

        }catch (Exception e){}

        try {
            note.setCursorVisible(true);
        }catch (Exception e){}

        selectionAvailable = false;
        note = null;
    }



    public static void replaceSelectionWith(CharSequence text){
        CursorPosition start = CursorPosition.min(handles[0].getCursorPosition(),handles[1].getCursorPosition());
        CursorPosition end = CursorPosition.max(handles[0].getCursorPosition(),handles[1].getCursorPosition());

        note.deleteContent(start,end);
        note.insertCharSequenceAt(start,text);
        start.characterIndex+=text.length();
        note.setCursorPosition(start);
        clearSelections();
    }

    static void handlePositionUpdated(boolean handleCursorPositionChanged) {
        if(handleCursorPositionChanged) {
            updateHighlightSpans();
            SpansController.updateToolbarForCurrentSelection();
        }
    }
    private static void refreshHandlePositions() {
        handles[0].updatePosition(handles[0].getCursorPosition(),false);
        handles[1].updatePosition(handles[1].getCursorPosition(),false);
    }
    private static void updateHighlightSpans() {
        if(handles[0].getCursorPosition()==null||handles[1].getCursorPosition()==null)return;
        CursorPosition start = CursorPosition.min(handles[0].getCursorPosition(),handles[1].getCursorPosition());
        CursorPosition end = CursorPosition.max(handles[0].getCursorPosition(),handles[1].getCursorPosition());

        note.highlightSelection(start,end);

    }
}

