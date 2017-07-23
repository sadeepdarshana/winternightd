package com.example.sadeep.winternightd.notebook;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.sadeep.winternightd.animation.XAnimation;
import com.example.sadeep.winternightd.bottombar.BottomBarCombined;
import com.example.sadeep.winternightd.misc.Globals;
import com.example.sadeep.winternightd.misc.Utils;
import com.example.sadeep.winternightd.note.Note;
import com.example.sadeep.winternightd.bottombar.UpperLayout;
import com.example.sadeep.winternightd.selection.CursorPosition;
import com.example.sadeep.winternightd.temp.d;

import java.lang.ref.WeakReference;

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


    public static class NoteHolder extends FrameLayout{

        public CardView mainCard;
        private CardView glassCard;

        private Notebook notebook;

        private Context context;

        private int mode = -1;//-1 means not assigned a mode yet
        public static final int MODE_VIEW = 0;
        public static final int MODE_EDIT = 1;

        public UpperLayout upper;


        public NoteHolder(Context context,Notebook notebook) {
            super(context);
            this.context = context;
            this.notebook = notebook;

            setBackgroundColor(Color.TRANSPARENT);
            setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));

            final GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    NoteHolder.this.onMainCardDoubleTap();
                    return true;
                }
            });


            mainCard = new CardView(context){
                @Override
                public boolean dispatchTouchEvent(MotionEvent ev) {
                    gestureDetector.onTouchEvent(ev);
                    super.dispatchTouchEvent(ev);
                    return true;
                }
            };
            glassCard = new CardView(context);


            mainCard.setCardBackgroundColor(Color.WHITE);
            mainCard.setCardElevation(Globals.dp2px * 2f);
            mainCard.setRadius(Globals.dp2px * 0);
            FrameLayout.LayoutParams maincardparams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            maincardparams.setMargins(Globals.dp2px * 6, 80, Globals.dp2px * 6, Globals.dp2px * 6);
            mainCard.setLayoutParams(maincardparams);
            mainCard.setMinimumHeight(Globals.dp2px * 100);
            mainCard.setContentPadding(Globals.dp2px * 7, Globals.dp2px * 7, Globals.dp2px * 7, Globals.dp2px * 7);
            mainCard.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View xv, int left, int top, int right, final int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    if(mode==MODE_EDIT) {
                        glassCard.postDelayed(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        glassCard.getLayoutParams().height = calculateGlassCardHeight();
                                        glassCard.requestLayout();
                                    }
                                }
                        ,1);
                    }
                }
            });

            glassCard.setCardBackgroundColor(Color.TRANSPARENT);
            glassCard.setCardElevation(Globals.dp2px * 2f);
            glassCard.setRadius(Globals.dp2px*23);
            FrameLayout.LayoutParams glasscardparams = new FrameLayout.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,400 );
            glasscardparams.setMargins(Globals.dp2px * 4, Globals.dp2px * 5, Globals.dp2px * 4, Globals.dp2px * 6);
            glassCard.setLayoutParams(glasscardparams);

            upper = new UpperLayout(context,true,true){
                @Override
                protected void onSendClick(View v) {
                    super.onSendClick(v);
                    onNoteSubmitClicked();
                }
            };
            CardView.LayoutParams upperlayoutparams = new CardView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
            upper.getUpperLayout().setLayoutParams(upperlayoutparams);
            upperlayoutparams.setMargins(2*Globals.dp2px,0*Globals.dp2px,2*Globals.dp2px,1*Globals.dp2px);
            upperlayoutparams.gravity = Gravity.BOTTOM;
            glassCard.addView(upper.getUpperLayout());


            setMode(MODE_VIEW,false);

        }

        private int calculateGlassCardHeight(){
            final int MARGIN = Globals.dp2px*8;
            int glassCardChildHeight = Globals.dp2px*50;
            int mainCardHeight = mainCard.getHeight();
            return mainCardHeight+glassCardChildHeight+MARGIN+70;
        }


        public void setMode(int mode,final boolean animate){
            //if(getNote()!=null)getNote().setIsEditable(mode==MODE_EDIT);
            if(mode == this.mode)return;
            this.mode = mode;
            switch (mode){
                case MODE_VIEW:
                    setBackgroundColor(Color.TRANSPARENT);
                    if(getNote()!=null)getNote().setIsEditable(false);
                    if(mainCard.getParent()==null)addView(mainCard);
                    //if(glassCard.getParent()!=null)removeView(glassCard);
                    if(animate)
                        XAnimation.changeDimension(glassCard,500,XAnimation.DIMENSION_HEIGHT,calculateGlassCardHeight(),0);
                    else{
                        glassCard.getLayoutParams().height = 0;
                        requestLayout();
                    }

                    break;

                case MODE_EDIT:
                    setBackgroundColor(Color.parseColor("#06000000"));
                    if(getNote()!=null){
                        getNote().setIsEditable(true);
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
            if(!(mode==MODE_EDIT)||getNote()==null)return;

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


