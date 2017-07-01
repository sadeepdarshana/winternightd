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
import com.example.sadeep.winternightd.misc.Globals;

/**
 * Created by Sadeep on 7/1/2017.
 */
final public class AttachBox {
    private AttachBox() {}







    public static void display(final View button) {

        Activity context= (Activity) button.getContext();

        int buttonCoords[] = new int[2];
        button.getLocationInWindow(buttonCoords);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth=displayMetrics.widthPixels;
        int screenHeight=displayMetrics.heightPixels;

        LinearLayout gridParent = new LinearLayout(context);
        gridParent.setBackground(new ColorDrawable(Color.TRANSPARENT));
        gridParent.setLayoutParams(new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,screenHeight-(buttonCoords[1]+button.getHeight())));

        final GridLayout grid = (GridLayout) LayoutInflater.from(context).inflate(R.layout.activity_test_activityy,(ViewGroup) button.getParent(),false);
        grid.setColumnCount(screenWidth/(Globals.dp2px*55));

        final PopupWindow popupWindow = new PopupWindow(context);
        popupWindow.setBackgroundDrawable(null);
        popupWindow.setHeight(screenHeight-(buttonCoords[1]+button.getHeight()));
        popupWindow.setWidth(screenWidth);

        popupWindow.setContentView(grid);
        popupWindow.showAtLocation(button, Gravity.NO_GRAVITY,0,buttonCoords[1]+button.getHeight());

        button.post(new Runnable() {
            @Override
            public void run() {
                grid.setVisibility(View.VISIBLE);
                Animator anim = ViewAnimationUtils.createCircularReveal(grid, button.getLeft()+button.getWidth()/2, 0, 0, (float) Math.hypot(grid.getWidth(), grid.getHeight()));
                anim.setDuration(500) ;
                anim.start();
            }
        });


    }








}
