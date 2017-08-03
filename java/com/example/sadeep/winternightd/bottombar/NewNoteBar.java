package com.example.sadeep.winternightd.bottombar;

import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.example.sadeep.winternightd.R;
import com.example.sadeep.winternightd.activities.NotebookActivity;
import com.example.sadeep.winternightd.animation.XAnimation;
import com.example.sadeep.winternightd.note.Note;
import com.example.sadeep.winternightd.note.NoteFactory;
import com.example.sadeep.winternightd.misc.Utils;

/**
 * Created by Sadeep on 7/10/2017.
 */

public class NewNoteBar  extends LinearLayout{

    private View attach,send;
    private Note note;

    private ViewGroup noteScroll; //the immediate parent of the Note

    private int attachWidth,sendWidth, emptyNoteHeight; //widths of the buttons WHEN THEY ARE SHOWN

    private boolean buttonVisibility = true;


    public NewNoteBar(Context context, boolean buttonVisibility) {
        super(context);
        setBackgroundColor(0xffffffff);

        LayoutInflater.from(context).inflate(R.layout.new_note_bar,this,true);

        attach = findViewById(R.id.attach);
        send = findViewById(R.id.send);
        noteScroll = (ViewGroup) findViewById(R.id.notescroll);

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
                if(((NotebookActivity)getContext()).contextSessionId!=NotebookActivity.classContextSessionId)this.cancel();
                if(note.isEmpty())onNoteIsEmpty();
                if (note.getHeight() > 1.5 * emptyNoteHeight && emptyNoteHeight!=0)onNoteHeightMatured();
                if(!note.isEmpty())onNoteHasContent();
            }

            public void onFinish(){}
        }.start();
    }

    protected void onNoteHasContent() {

    }

    protected void onNoteFocused() {

    }

    protected void onNoteIsEmpty() {

    }

    protected void onNoteHeightMatured() {

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

}
