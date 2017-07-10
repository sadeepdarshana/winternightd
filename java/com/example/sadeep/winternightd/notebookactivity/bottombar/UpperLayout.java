package com.example.sadeep.winternightd.notebookactivity.bottombar;

import android.content.Context;
import android.graphics.Color;
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

public class UpperLayout {

    private Context context;
    private LinearLayout upperLayout; //the android View of the Lower Layout generated, (notice this class extends nothing)

    private View attach,send,toolbar;

    private int attachWidth,sendWidth,toolbarHeight; //widths and heights of the Views WHEN THEY ARE SHOWN

    private boolean buttonVisibility = true;
    private boolean toolbarVisibility = true;



    public UpperLayout(Context context, boolean buttonVisibility, boolean toolbarVisibility) {
        this.context = context;
        upperLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.bottombar_upperlayout,null);

        attach = upperLayout.findViewById(R.id.attach);
        send = upperLayout.findViewById(R.id.send);
        toolbar = upperLayout.findViewById(R.id.toolbar);

        attachWidth = Utils.getWidth(attach);
        sendWidth = Utils.getWidth(send);
        toolbarHeight = Utils.getHeight(toolbar);

        setButtonsVisibility(buttonVisibility,false);
        setButtonsVisibility(toolbarVisibility,false);
    }

    public LinearLayout getUpperLayout() {
        return upperLayout;
    }



    public boolean getButtonVisibility() {
        return buttonVisibility;
    }

    public void setButtonsVisibility(boolean visible, boolean animate){
        final int ANIMATION_DURATION = 300;

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



    public boolean getToolbarVisibility() {
        return toolbarVisibility;
    }

    public void setToolbarVisibility(boolean visible, boolean animate){
        final int ANIMATION_DURATION = 300;

        if(toolbarVisibility == visible)return;
        this.toolbarVisibility=visible;

        if(animate) {
            if(visible) {
                XAnimation.changeDimension(toolbar, ANIMATION_DURATION, XAnimation.DIMENSION_HEIGHT, 0, toolbarHeight);
            }else {
                XAnimation.changeDimension(toolbar, ANIMATION_DURATION, XAnimation.DIMENSION_HEIGHT, toolbarHeight, 0);
            }
        }else{
            if(visible){
                toolbar.getLayoutParams().height=toolbarHeight;
            }else{
                toolbar.getLayoutParams().height=0;
            }
            toolbar.requestLayout();
        }
    }


}
