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

        private Context context;

        private int mode = -1;//-1 means not assigned a mode yet

        public NoteActionsToolbar upper;
        public boolean noteEditable;


        public NoteHolder(Context context,Notebook notebook) {
            super(context);
            this.context = context;
            this.notebook = notebook;

            final GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    NoteHolder.this.onMainCardDoubleTap();
                    return true;
                }
            });


            upper = new NoteActionsToolbar(context,true,true){
                @Override
                protected void onSendClick(View v) {
                    super.onSendClick(v);
                    onNoteSubmitClicked();
                }
            };

            CardView.LayoutParams upperlayoutparams = new CardView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
            upper.getLayout().setLayoutParams(upperlayoutparams);
            upperlayoutparams.setMargins(2*Globals.dp2px,0*Globals.dp2px,2*Globals.dp2px,1*Globals.dp2px);
            upperlayoutparams.gravity = Gravity.BOTTOM;
            glassCard.addView(upper.getLayout());


            setMode(NoteHolderModes.MODE_VIEW,false);

        }



        public void setMode(int mode,final boolean animate){
            //if(getNote()!=null)getNote().setEditable(mode==MODE_EDIT);
            if(mode == this.mode)return;
            this.mode = mode;
            switch (mode){
                case NoteHolderModes.MODE_VIEW:
                    setBackgroundColor(Color.TRANSPARENT);
                    if(getNote()!=null)getNote().setEditable(false);
                    if(mainCard.getParent()==null)addView(mainCard);
                    //if(glassCard.getParent()!=null)removeView(glassCard);
                    if(animate)
                        XAnimation.changeDimension(glassCard,500,XAnimation.DIMENSION_HEIGHT,calculateGlassCardHeight(),0);
                    else{
                        glassCard.getLayoutParams().height = 0;
                        requestLayout();
                    }

                    break;

                case NoteHolderModes.MODE_EDIT:
                    setBackgroundColor(Color.parseColor("#06000000"));
                    if(getNote()!=null){
                        getNote().setEditable(true);
                        getNote().setCursor(new CursorPosition(0,0));
                    }
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(glassCard.getParent()==null)addView(glassCard,0);
                            calculateGlassCardHeight();
                            if(animate)
                                XAnimation.changeDimension(glassCard,500,XAnimation.DIMENSION_HEIGHT,Math.min(0,mainCard.getHeight()),calculateGlassCardHeight());
                            else{
                                glassCard.getLayoutParams().height = calculateGlassCardHeight();
                                glassCard.requestLayout();
                            }
                        }
                    },1);

                    notebook.editor.noteHolder = this;
                    //handler.sendEmptyMessage(0);
                    break;
            }
        }

        public int getMode() {
            return mode;
        }

        public void bind(Note note){
            mainCard.removeAllViews();
            if(note.getParent()!=null)((ViewGroup)note.getParent()).removeView(note);
            mainCard.addView(note);
        }

        public Note getNote() {
            if(mainCard.getChildCount()!=0 && mainCard.getChildAt(0)instanceof Note)return (Note) mainCard.getChildAt(0);
            return null;
        }

        protected void onMainCardDoubleTap() {
            notebook.editor.setActiveNote(this);
        }

        private void onNoteSubmitClicked() {
            if(!(mode== NoteHolderModes.MODE_EDIT)||getNote()==null)return;

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


