package com.example.sadeep.winternightd.notebook;

import android.content.Context;
import android.graphics.Color;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sadeep.winternightd.bottombar.ExtendedToolbar;
import com.example.sadeep.winternightd.misc.Globals;
import com.example.sadeep.winternightd.misc.NotebookItemChamber;

import java.text.Format;
import java.text.SimpleDateFormat;

import static com.example.sadeep.winternightd.notebook.NotebookViewHolderUtils.NoteHolder.*;

/**
 * Created by Sadeep on 7/24/2017.
 */

public class NoteHolderModes {
    public static final int MODE_VIEW = 0;
    public static final int MODE_EDIT = 1;

    public static class ModeView{

        public static void setAsNoteHolderMode(final NotebookViewHolderUtils.NoteHolder noteHolder, boolean animate){
            if(noteHolder.getMode()== MODE_VIEW)return;

            if(noteHolder.getNote()!=null)noteHolder.getNote().setEditable(false);
            noteHolder.noteEditable = false;

            noteHolder.getUpperChamber().setChamberContent(new ViewUpper(noteHolder.getContext()),animate);
            noteHolder.getLowerChamber().emptyChamber(animate);

            noteHolder.setRadius(Globals.dp2px*4);

            final GestureDetector gestureDetector = new GestureDetector(noteHolder.getContext(), new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    noteHolder.getNoteSpace().setOnTouchListener(null);
                    noteHolder.setMode(MODE_EDIT,true);
                    setAsActiveNote(noteHolder);

                    return true;
                }
            });

            noteHolder.getNoteSpace().setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    gestureDetector.onTouchEvent(event);
                    return true;
                }
            });

            noteHolder.mode = MODE_VIEW;
        }

        public static void onBind(NotebookViewHolderUtils.NoteHolder noteHolder) {
            ViewUpper viewUpper = (ViewUpper) noteHolder.getUpperChamber().getChamberContent();
            viewUpper.setDateTime(noteHolder.getNote().noteInfo.currentVersionTime);
        }

        private static void setAsActiveNote(NotebookViewHolderUtils.NoteHolder noteHolder) {
            noteHolder.getNotebook().noteHolderController.setAllNoteHoldersModeExcept(DEFAULT_MODE,noteHolder,true);
            noteHolder.getNotebook().editor.activeNote = noteHolder.getNote();
        }

        public static class ViewUpper extends FrameLayout{
            private long dateTime;
            private TextView dateTimeTextView;

            public void setDateTime(long dateTime){
                this.dateTime = dateTime;
                updateDateTimeTextView();
            }

            public ViewUpper(Context context) {
                super(context);
                dateTimeTextView = new TextView(context);
                dateTimeTextView.setTextColor(0xff228822);
                dateTimeTextView.setBackgroundColor(Color.TRANSPARENT);
                setBackgroundColor(Color.TRANSPARENT);
                addView(dateTimeTextView);
                updateDateTimeTextView();

                setPadding(Globals.dp2px*2,Globals.dp2px*4,Globals.dp2px*2,Globals.dp2px*4);

                //setVisibility(GONE);
            }

            private void updateDateTimeTextView() {
                Format dateFormat =new SimpleDateFormat("MMM d, ''yy h:mm a");
                dateTimeTextView.setText(dateFormat.format(dateTime));
                //todo omit year if same year etc + today,yesterday,6mins ago
            }
        }
    }
    public static class ModeEdit{

        public static void setAsNoteHolderMode(NotebookViewHolderUtils.NoteHolder noteHolder, boolean animate){
            if(noteHolder.getMode()== MODE_EDIT)return;

            if(noteHolder.getNote()!=null)noteHolder.getNote().setEditable(true);
            noteHolder.noteEditable = true;

            noteHolder.getUpperChamber().setChamberContent(new EditUpper(noteHolder.getContext()),animate);
            noteHolder.getLowerChamber().setChamberContent(new EditLower(noteHolder.getContext()),animate);

            noteHolder.setRadius(Globals.dp2px*23);

            noteHolder.mode = MODE_EDIT;
        }

        public static void onBind(NotebookViewHolderUtils.NoteHolder noteHolder) {

        }

        public static class EditUpper extends View {

            public EditUpper(Context context) {
                super(context);
                NotebookItemChamber.LayoutParams params = new NotebookItemChamber.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Globals.dp2px*25);
                setLayoutParams(params);
            }
        }

        public static class EditLower extends LinearLayout{

            public EditLower(Context context) {
                super(context);
                NotebookItemChamber.LayoutParams params1 = new NotebookItemChamber.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                setLayoutParams(params1);

                ExtendedToolbar extendedToolbar = new ExtendedToolbar(context,true,true, true);
                addView(extendedToolbar);

                EditLower.LayoutParams params2= new EditLower.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                extendedToolbar.setLayoutParams(params2);
            }
        }
    }
}
