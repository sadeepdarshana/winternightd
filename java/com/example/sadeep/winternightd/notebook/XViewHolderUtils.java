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
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.sadeep.winternightd.misc.Globals;
import com.example.sadeep.winternightd.note.Note;
import com.example.sadeep.winternightd.notebookactivity.bottombar.UpperLayout;

/**
 * Created by Sadeep on 6/17/2017.
 */

final class XViewHolderUtils {
    private XViewHolderUtils(){}//static class

    public static final int VIEWTYPE_HEADER = 0;
    public static final int VIEWTYPE_NOTE_HOLDER = 1;
    public static final int VIEWTYPE_FOOTER = 2;

    static class XViewHolder extends RecyclerView.ViewHolder{
        public ViewGroup holder;
        public XViewHolder(ViewGroup holder) {
            super(holder);
            this.holder = holder;
        }
    }


    static class NoteHolder extends FrameLayout{
        public static final int MODE_VIEW=0;
        public static final int MODE_EDIT=1;

        private CardView mainCard;
        private CardView glassCard;

        private Context context;

        public NoteHolder(Context context) {
            super(context);
            this.context = context;
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

            setMode(MODE_VIEW);

        }

        protected void onMainCardDoubleTap() {

        }

        public void setMode(int mode){
            switch (mode){
                case MODE_VIEW:
                    mainCard.setCardBackgroundColor(Color.WHITE);
                    mainCard.setCardElevation(Globals.dp2px * 2f);
                    mainCard.setRadius(Globals.dp2px * 0);

                    FrameLayout.LayoutParams maincardparams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                    maincardparams.setMargins(Globals.dp2px * 6, Globals.dp2px * 6, Globals.dp2px * 6, Globals.dp2px * 6);
                    mainCard.setLayoutParams(maincardparams);

                    mainCard.setMinimumHeight(Globals.dp2px * 100);
                    mainCard.setContentPadding(Globals.dp2px * 7, Globals.dp2px * 7, Globals.dp2px * 7, Globals.dp2px * 7);

                    if(mainCard.getParent()==null)addView(mainCard);
                    if(glassCard.getParent()!=null)removeView(glassCard);

                    break;

                case MODE_EDIT:
                    glassCard.setCardBackgroundColor(Color.TRANSPARENT);
                    glassCard.setCardElevation(Globals.dp2px * 2f);
                    glassCard.setRadius(Globals.dp2px*23);

                    FrameLayout.LayoutParams glasscardparams = new FrameLayout.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,400 );
                    glasscardparams.setMargins(Globals.dp2px * 4, Globals.dp2px * 0, Globals.dp2px * 4, Globals.dp2px * 6);
                    glassCard.setLayoutParams(glasscardparams);

                    if(glassCard.getParent()==null)addView(glassCard);



                    UpperLayout upper = new UpperLayout(context,true,true);

                    CardView.LayoutParams upperlayoutparams = new CardView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
                    upper.getUpperLayout().setLayoutParams(upperlayoutparams);
                    upperlayoutparams.setMargins(2*Globals.dp2px,0*Globals.dp2px,2*Globals.dp2px,1*Globals.dp2px);

                    upperlayoutparams.gravity = Gravity.BOTTOM;
                    glassCard.addView(upper.getUpperLayout());

                    break;
            }
        }

        public void bindNote(Note note){
            mainCard.removeAllViews();
            mainCard.addView(note);
        }

        public Note getNote() {
            if(mainCard.getChildCount()!=0 && mainCard.getChildAt(0)instanceof Note)return (Note) mainCard.getChildAt(0);
            return null;
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

        public Footer(Context context, Notebook notebook) {
            super(context);
            setPadding(0,0,0,0);
            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
            params.setMargins(0,0,0,0);
            setLayoutParams(params);

            if(notebook._BottomBar==null)return;
            notebook._BottomBar.getBottomBar().addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View xv, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    {
                        Footer.this.getLayoutParams().height = bottom;
                        Footer.this.requestLayout();
                    }
                }
            });
        }
    }
}


    final View v = new View(context);
v.setPadding(0,0,0,0);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, 2*Globals.dp2px);
        params.setMargins(0,0,0,0);
        v.setLayoutParams(params);
        holder.holdingParent.addView(v);

        notebook.bottomBar.getBottombar().addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
@Override
public void onLayoutChange(View xv, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

        {
        v.getLayoutParams().height = bottom;
        v.requestLayout();
        }
        }
        });