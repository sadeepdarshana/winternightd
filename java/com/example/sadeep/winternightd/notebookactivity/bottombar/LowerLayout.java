package com.example.sadeep.winternightd.notebookactivity.bottombar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.example.sadeep.winternightd.R;
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

    private int attachWidth,sendWidth; //widths of the buttons WHEN THEY ARE SHOWN

    private boolean buttonVisibility = true;


    public LowerLayout(Context context, boolean buttonsVisible) {
        this.context = context;
        lowerLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.bottombar_lowerlayout,null);

        attach = lowerLayout.findViewById(R.id.attach);
        send = lowerLayout.findViewById(R.id.send);
        noteScroll = (ViewGroup) lowerLayout.findViewById(R.id.notescroll);

        attachWidth = Utils.getWidth(attach);
        sendWidth = Utils.getWidth(send);

        note = NoteFactory.createNewNote(context,true, noteScroll);
        note.setLayoutParams(new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        noteScroll.addView(note);
        note.setScrollableParent(noteScroll);
    }

    public LinearLayout getLowerLayout() {
        return lowerLayout;
    }

    public void setButtonsVisibility(boolean visible, boolean animate){
        if(buttonVisibility == visible)return;
        //// TODO: 7/10/2017
    }

}
