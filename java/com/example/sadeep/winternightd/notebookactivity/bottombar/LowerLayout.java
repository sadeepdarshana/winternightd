package com.example.sadeep.winternightd.notebookactivity.bottombar;

import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.example.sadeep.winternightd.R;
import com.example.sadeep.winternightd.animation.XAnimation;
import com.example.sadeep.winternightd.note.Note;
import com.example.sadeep.winternightd.note.NoteFactory;
import com.example.sadeep.winternightd.temp.Utils;

/**
 * Created by Sadeep on 7/10/2017.
 */

public class LowerLayout {

    private Context context;
    private LinearLayout lowerLayout; //the android View of the Lower Layout generated, (notice this class extends nothing)

    private View attach,send;
    private Note note;

    private ViewGroup noteScroll; //the immediate parent of the Note

    private int attachWidth,sendWidth, emptyNoteHeight; //widths of the buttons WHEN THEY ARE SHOWN

    private boolean buttonVisibility = true;
    private boolean glassModeEnabled = false;


    public LowerLayout(Context context, boolean buttonVisibility,boolean glassModeEnabled) {
        this.context = context;
        lowerLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.bottombar_lowerlayout,null);

        attach = lowerLayout.findViewById(R.id.attach);
        send = lowerLayout.findViewById(R.id.send);
        noteScroll = (ViewGroup) lowerLayout.findViewById(R.id.notescroll);

        note = NoteFactory.createNewNote(context,true, noteScroll);
        note.setLayoutParams(new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        note.focusListener = new Note.FocusListener() {
            @Override
            public void onFocused() {
                onNoteFocused();
            }
        };
        noteScroll.addView(note);

        emptyNoteHeight = Utils.getHeight(note);
        attachWidth = Utils.getWidth(attach);
        sendWidth = Utils.getWidth(send);

        setButtonsVisibility(buttonVisibility,false);
        setGlassModeEnabled(glassModeEnabled,false);


        attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAttachClick(v);
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSendClick(v);
            }
        });

        new CountDownTimer(2000000000, 500)
        {
            public void onTick(long millisUntilFinished)
            {
                if(note.isEmpty())onNoteIsEmpty();
                if (note.getHeight() > 1.5 * emptyNoteHeight && emptyNoteHeight!=0)onNoteHeightMatured();
            }


            public void onFinish(){}
        }.start();
    }

    protected void onNoteFocused() {

    }

    protected void onNoteIsEmpty() {

    }

    protected void onNoteHeightMatured() {

    }

    public LinearLayout getLowerLayout() {
        return lowerLayout;
    }

    public Note getNote() {
        return note;
    }




    protected void onAttachClick(View v) {

    }

    protected void onSendClick(View v) {

    }



    public boolean getButtonVisibility() {
        return buttonVisibility;
    }

    public void setButtonsVisibility(boolean visible, boolean animate){
        final int ANIMATION_DURATION = 400;

        if(buttonVisibility == visible)return;
        this.buttonVisibility=visible;

        if(animate) {
            if(visible) {
                XAnimation.changeDimension(attach, ANIMATION_DURATION, XAnimation.DIMENSION_WIDTH, 0, attachWidth);
                XAnimation.changeDimension(send, ANIMATION_DURATION, XAnimation.DIMENSION_WIDTH, 0, sendWidth);
            }else {
                XAnimation.changeDimension(attach, ANIMATION_DURATION, XAnimation.DIMENSION_WIDTH, attachWidth, 0);
                XAnimation.changeDimension(send, ANIMATION_DURATION, XAnimation.DIMENSION_WIDTH, sendWidth, 0);
            }
        }else{
            if(visible){
                attach.getLayoutParams().width=attachWidth;
                send.getLayoutParams().width=sendWidth;
            }else{
                attach.getLayoutParams().width=0;
                send.getLayoutParams().width=0;
            }
            attach.requestLayout();
            send.requestLayout();
        }
    }



    public boolean getGlassModeEnabled() {
        return glassModeEnabled;
    }

    public void setGlassModeEnabled(boolean glassModeEnabled,boolean animate) {
        final int ANIMATION_DURATION = 50;

        if(glassModeEnabled == this.glassModeEnabled)return;
        this.glassModeEnabled = glassModeEnabled;

        final int colorOpaque = Color.argb(246,252,252,252);        //color when glass mode disabled
        final int colorIntermediate = Color.argb(75,150,150,150);   //transition between the two colors goes through this color
        final int colorTransparent = Color.argb(50,50,50,50);       //color when in glass mode

        if(animate) {
            if (glassModeEnabled) {
                XAnimation.changeBackgroundColor(lowerLayout, ANIMATION_DURATION, colorOpaque, colorIntermediate, 0);
                XAnimation.changeBackgroundColor(lowerLayout, ANIMATION_DURATION, colorIntermediate, colorTransparent, ANIMATION_DURATION);
            }
            else {
                XAnimation.changeBackgroundColor(lowerLayout, ANIMATION_DURATION, colorTransparent, colorIntermediate, 0);
                XAnimation.changeBackgroundColor(lowerLayout, ANIMATION_DURATION, colorIntermediate, colorOpaque, ANIMATION_DURATION);
            }
        }else{
            if (glassModeEnabled)   lowerLayout.setBackgroundColor(colorTransparent);
            else                    lowerLayout.setBackgroundColor(colorOpaque);
        }
    }
}