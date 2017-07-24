package com.example.sadeep.winternightd.notebook;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.example.sadeep.winternightd.animation.XAnimation;
import com.example.sadeep.winternightd.bottombar.BottomBarCombined;
import com.example.sadeep.winternightd.misc.Globals;
import com.example.sadeep.winternightd.misc.Utils;
import com.example.sadeep.winternightd.note.Note;
import com.example.sadeep.winternightd.bottombar.NoteActionsToolbar;
import com.example.sadeep.winternightd.selection.CursorPosition;

import static com.example.sadeep.winternightd.notebook.NoteHolderModes.MODE_EDIT;
import static com.example.sadeep.winternightd.notebook.NoteHolderModes.MODE_VIEW;

/**
 * Created by Sadeep on 6/17/2017.
 */

final class NotebookViewHolderUtils {
    private NotebookViewHolderUtils(){}//static class

    public static final int VIEWTYPE_HEADER = 0;
    public static final int VIEWTYPE_NOTE_HOLDER = 1;
    public static final int VIEWTYPE_FOOTER = 2;

    static class NotebookViewHolder extends RecyclerView.ViewHolder{
        public ViewGroup holder;
        public NotebookViewHolder(ViewGroup holder) {
            super(holder);
            this.holder = holder;
        }
    }


    public static class NoteHolder extends NotebookItem{

        private Notebook notebook;

        private int mode = -1;//-1 means not assigned a mode yet

        public NoteActionsToolbar upper;
        public boolean noteEditable;


        public NoteHolder(Context context,Notebook notebook) {
            super(context);
            this.notebook = notebook;

            Notebook.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(Globals.dp2px*7,Globals.dp2px*5,Globals.dp2px*7,Globals.dp2px*5);
            setLayoutParams(params);
        }


        public void bind(Note note,int mode){
            getNoteSpace().removeAllViews();
            setMode(mode,false);
            note.setEditable(noteEditable);
            getNoteSpace().addView(note);
            
        }

        public void setMode(int mode,final boolean animate){
            if(mode==MODE_VIEW) NoteHolderModes.ModeView.setAsNoteHolderMode(this,animate);
            if(mode==MODE_EDIT) NoteHolderModes.ModeEdit.setAsNoteHolderMode(this,animate);

            this.mode = mode;
        }

        public int getMode() {
            return mode;
        }


        public Note getNote() {
            return getNoteSpace().getChildCount()!=0 ?   (Note) getNoteSpace().getChildAt(0) : null;
        }

        private void onNoteSubmitClicked() {
            if(!(mode== MODE_EDIT)||getNote()==null)return;

            notebook.editor.pushNote(getNote());
        }
    }

    static class Header extends LinearLayout {

        public Header(Context context) {
            super(context);
            setPadding(0, 0, 0, 0);
            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, Globals.dp2px * 6);
            params.setMargins(0, 0, 0, 0);
            setLayoutParams(params);
        }
    }

    static class Footer extends LinearLayout{

        public Footer(Context context, final Notebook notebook) {
            super(context);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, Utils.getHeight(new BottomBarCombined(context).getBottomBar()));
            setLayoutParams(params);

            notebook._BottomBar.getBottomBar().addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View xv, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    {
                        Footer.this.getLayoutParams().height = bottom;
                        Footer.this.requestLayout();
                    }
                }
            });
            notebook._BottomBar.getBottomBar().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if(notebook._BottomBar.getBottomBar().getParent()==null){
                        Footer.this.getLayoutParams().height = Globals.dp2px*10;
                        Footer.this.requestLayout();
                    }
                }
            });
        }
    }
}


