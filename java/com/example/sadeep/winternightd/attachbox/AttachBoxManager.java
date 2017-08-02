package com.example.sadeep.winternightd.attachbox;

import android.animation.Animator;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.GridLayout;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.example.sadeep.winternightd.R;
import com.example.sadeep.winternightd.buttons.customizedbuttons.AttachBoxButton;
import com.example.sadeep.winternightd.buttons.customizedbuttons.AttachBoxOpener;
import com.example.sadeep.winternightd.buttons.customizedbuttons.AttachButton;
import com.example.sadeep.winternightd.buttons.customizedbuttons.AttachCircularButton;
import com.example.sadeep.winternightd.misc.Globals;

/**
 * Created by Sadeep on 7/1/2017.
 */
final public class AttachBoxManager {
    private AttachBoxManager() {}


    public static final int ATTACH_BUTTON_ID_CHECKEDFIELD = 0;
    public static final int ATTACH_BUTTON_ID_BULLETEDFIELD = 1;
    public static final int ATTACH_BUTTON_ID_NUMBEREDFIELD = 2;
    public static final int ATTACH_BUTTON_ID_CAMERA = 3;

    public static PopupWindow popupWindow;


    public static void display(final View button,View anchor, final OnAttachBoxItemClick listener) {

        tryDismiss();

        int ATTACHBOX_BUTTON_MINIMUM_WIDTH = Globals.dp2px*55;

        Activity context= (Activity) button.getContext();

        int anchorCoords[] = new int[2];
        anchor.getLocationInWindow(anchorCoords);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth=displayMetrics.widthPixels;
        int screenHeight=displayMetrics.heightPixels;

        int attachboxHeight = screenHeight-(anchorCoords[1]);
        int attachboxWidth = screenWidth;
        int posX = 0;
        int posY = anchorCoords[1];
        int _rippleX = button.getWidth()/2;
        int _rippleY = 0;

        if(attachboxHeight<400){
            attachboxHeight=500;
            posY = anchorCoords[1]-attachboxHeight;
            _rippleY = attachboxHeight;
        }

        final int rippleX = _rippleX;
        final int rippleY = _rippleY;


        final LinearLayout gridParent = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.attachbox,(ViewGroup) button.getParent(),false);
        ((GridLayout)gridParent.getChildAt(1)).setColumnCount(screenWidth/(ATTACHBOX_BUTTON_MINIMUM_WIDTH));


        final PopupWindow popupWindow = new PopupWindow(context);
        AttachBoxManager.popupWindow = popupWindow;
        popupWindow.setHeight(attachboxHeight);
        popupWindow.setWidth(screenWidth);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        popupWindow.setContentView(gridParent);
        popupWindow.showAtLocation(button, Gravity.NO_GRAVITY,posX,posY);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                try {
                    ((AttachButton)button).setMode(0);
                }catch (Exception e){}
                try {
                    ((AttachCircularButton)button).setColor(AttachCircularButton.colorNormal);
                }catch (Exception e){}
                button.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ((AttachBoxOpener)button).setAttachboxOpened(false);
                    }
                },10);
            }
        });

        for(int i=0;i<((GridLayout)gridParent.getChildAt(1)).getChildCount();i++){
            ((GridLayout)gridParent.getChildAt(1)).getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    if(!(v instanceof AttachBoxButton))return;

                    button.post(new Runnable() {
                        @Override
                        public void run() {
                            Animator anim = ViewAnimationUtils.createCircularReveal(gridParent,rippleX,rippleY, (float) Math.hypot(gridParent.getWidth(), gridParent.getHeight()),0);
                            anim.setDuration(200) ;
                            anim.start();
                        }
                    });

                    button.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            popupWindow.dismiss();
                            listener.buttonClicked(((AttachBoxButton)v).getAttachButtonId());
                        }
                    },201);
                }
            });
        }

        button.post(new Runnable() {
            @Override
            public void run() {
                Animator anim = ViewAnimationUtils.createCircularReveal(gridParent, rippleX, rippleY, 0, (float) Math.hypot(gridParent.getWidth(), gridParent.getHeight()));
                anim.setDuration(500) ;
                anim.start();
            }
        });


    }


    public static void tryDismiss() {
        try{
            AttachBoxManager.popupWindow.dismiss();
        }
        catch (Exception e){}
    }
}
