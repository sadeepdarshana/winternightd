package com.example.sadeep.winternightd.notebookactivity.bottombar;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.example.sadeep.winternightd.R;
import com.example.sadeep.winternightd.animation.XAnimation;
import com.example.sadeep.winternightd.attachbox.AttachBoxManager;
import com.example.sadeep.winternightd.attachbox.OnAttachBoxItemClick;
import com.example.sadeep.winternightd.buttons.customizedbuttons.AttachBoxOpener;
import com.example.sadeep.winternightd.note.Note;
import com.example.sadeep.winternightd.note.NoteFactory;
import com.example.sadeep.winternightd.toolbar.ToolbarController;

public class CombinedBottomBar {

    public ViewGroup getBottombar() {
        return bottombar;
    }

    private ViewGroup bottombar;

    public View send0, attach0, send1, attach1;

    private Note note;
    private HorizontalScrollView toolbar;
    private ScrollView noteScroll;

    private int sendWidth, attachWidth;
    private int noteOriginalHeight;

    private boolean expanded = false;

    private int toolbarHeight;
    private boolean toolbarVisible = false;
    private boolean bottomLLOpacity = true;
    public CountDownTimer timer;

    private LinearLayout bottomLL;

    public void changeMode(boolean expanded){
        final int DURATION = 400;

        if(this.expanded==expanded)return;
        this.expanded=expanded;

        if(expanded){
            XAnimation.changeDimension(send0, DURATION, XAnimation.DIMENSION_WIDTH, 0, sendWidth,0);
            XAnimation.changeDimension(attach0, DURATION, XAnimation.DIMENSION_WIDTH, 0, attachWidth,0);

            XAnimation.changeDimension(send1, DURATION, XAnimation.DIMENSION_WIDTH, sendWidth, 0);
            XAnimation.changeDimension(attach1, DURATION, XAnimation.DIMENSION_WIDTH, attachWidth, 0);


            XAnimation.scroll(toolbar,DURATION, attachWidth,0);


        }else {
            XAnimation.changeDimension(send0, DURATION, XAnimation.DIMENSION_WIDTH, sendWidth, 0);
            XAnimation.changeDimension(attach0, DURATION, XAnimation.DIMENSION_WIDTH, attachWidth, 0);

            XAnimation.changeDimension(send1, DURATION, XAnimation.DIMENSION_WIDTH, 0, sendWidth);
            XAnimation.changeDimension(attach1, DURATION, XAnimation.DIMENSION_WIDTH, 0, attachWidth);

            XAnimation.scroll(toolbar,DURATION,-attachWidth);
        }

    }

    public CombinedBottomBar(Context context) {

        bottombar = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.bottombar,null);
        //bottombar.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        toolbar = (HorizontalScrollView)bottombar.findViewById(R.id.toolbar);
        ToolbarController.initialize(toolbar);

        send0 = bottombar.findViewById(R.id.send0);
        attach0 = bottombar.findViewById(R.id.attach0);
        send1 = bottombar.findViewById(R.id.send1);
        attach1 = bottombar.findViewById(R.id.attach1);
        noteScroll = (ScrollView) bottombar.findViewById(R.id.notescroll);
        bottomLL = (LinearLayout)bottombar.findViewById(R.id.bottomLL);

        note = NoteFactory.createNewNote(context,true,noteScroll);
        note.setLayoutParams(new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        noteScroll.addView(note);

        sendWidth =send1.getWidth();
        attachWidth =attach1.getWidth();

        note.setScrollableParent(noteScroll);

        note.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener()
        {
            @Override
            public boolean onPreDraw()
            {
                noteOriginalHeight = note.getHeight();
                note.getViewTreeObserver().removeOnPreDrawListener(this);
                return false;
            }
        });
        send1.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener()
        {
            @Override
            public boolean onPreDraw()
            {
                send1.getViewTreeObserver().removeOnPreDrawListener(this);
                sendWidth = send1.getWidth();
                return false;
            }
        });

        attach1.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener()
        {
            @Override
            public boolean onPreDraw()
            {
                attach1.getViewTreeObserver().removeOnPreDrawListener(this);
                attachWidth = attach1.getWidth();
                return false;
            }
        });

        note.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (noteOriginalHeight == 0) return;
                if (note.getHeight() > 1.5 * noteOriginalHeight) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            changeMode(true);
                        }
                    }, 400);
                }
            }
        });

        attach0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!((AttachBoxOpener)v).isAttachboxOpen()) {
                    ((AttachBoxOpener) v).setAttachboxOpened(true);
                    AttachBoxManager.display(v, new OnAttachBoxItemClick() {
                        @Override
                        public void buttonClicked(int attachButtonId) {
                            note.attachboxRequests(attachButtonId);
                        }
                    });
                }else{
                    try {
                        AttachBoxManager.popupWindow.dismiss();
                    }catch (Exception e){}
                }
            }
        });
        attach1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!((AttachBoxOpener)v).isAttachboxOpen()) {
                    ((AttachBoxOpener) v).setAttachboxOpened(true);
                    AttachBoxManager.display(v, new OnAttachBoxItemClick() {
                        @Override
                        public void buttonClicked(int attachButtonId) {
                            note.attachboxRequests(attachButtonId);
                        }
                    });
                }else{
                    try {
                        AttachBoxManager.popupWindow.dismiss();
                    }catch (Exception e){}
                }
            }
        });

         timer=new CountDownTimer(2000000000, 500)
        {
            public void onTick(long millisUntilFinished)
            {
                if(note.isEmpty())changeMode(false);

            }
            public void onFinish(){}
        }.start();

        note.focusListener = new Note.FocusListener() {
            @Override
            public void onFocused() {
                setToolbarVisibility(true);
            }
        };


        View toolbartmp = LayoutInflater.from(context).inflate(R.layout.toolbar,null); //determine
        toolbartmp.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);//the height
        toolbarHeight = toolbartmp.getMeasuredHeight();                                     //of toolbar
    }

    public void setToolbarVisibility(boolean visible) {
        if(toolbarVisible==visible)return;
        toolbarVisible = visible;

        if(visible) {
            XAnimation.changeDimension(toolbar, 300, XAnimation.DIMENSION_HEIGHT, 0, toolbarHeight);
        }
        else {
            XAnimation.changeDimension(toolbar, 300, XAnimation.DIMENSION_HEIGHT, toolbarHeight, 0);
        }
        changeBottomLLOpacity(visible);
    }

    public void requestToolbarHide() {
        if(!toolbarVisible)return;
        if(note.isEmpty())setToolbarVisibility(false);

    }

    public void changeBottomLLOpacity(boolean opaque){
        if(bottomLLOpacity == opaque)return;
        bottomLLOpacity = opaque;

        int colorOpaq = Color.argb(246,252,252,252);
        int colorInt = Color.argb(75,150,150,150);
        int colorTrnsprt = Color.argb(50,50,50,50);

        int colorFrom ;
        int colorTo ;

        if(opaque){
            colorFrom = colorTrnsprt;
            colorTo = colorOpaq;
        }else {
            colorTo = colorTrnsprt;
            colorFrom = colorOpaq;
        }
        int time = 30;
        ValueAnimator colorAnimation1 = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorInt);
        colorAnimation1.setDuration(time); // milliseconds
        colorAnimation1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                bottomLL.setBackgroundColor((int) animator.getAnimatedValue());
            }

        });
        colorAnimation1.setInterpolator(new AccelerateInterpolator(.8f));
        colorAnimation1.start();


        ValueAnimator colorAnimation2 = ValueAnimator.ofObject(new ArgbEvaluator(), colorInt, colorTo);
        colorAnimation2.setDuration(time); // milliseconds
        colorAnimation2.setStartDelay(time);
        colorAnimation2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                bottomLL.setBackgroundColor((int) animator.getAnimatedValue());
            }

        });
        colorAnimation2.setInterpolator(new DecelerateInterpolator(.8f));
        colorAnimation2.start();


    }

    public void changeBottomLLMode(boolean visible){
        if(visible)bottomLL.setVisibility(View.VISIBLE);
        else bottomLL.setVisibility(View.GONE);
    }

    public Note getNote() {
        return note;
    }
}
