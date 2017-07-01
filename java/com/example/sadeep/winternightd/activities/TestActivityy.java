package com.example.sadeep.winternightd.activities;

import android.animation.Animator;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayout;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.example.sadeep.winternightd.R;
import com.example.sadeep.winternightd.attachbox.AttachItemsAdapter;
import com.example.sadeep.winternightd.misc.Globals;

import java.io.IOException;
import java.io.InputStream;

public class TestActivityy extends AppCompatActivity implements View.OnClickListener {


    LinearLayout test;
    Button button;
    int h=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        test = new LinearLayout(this);
        test.setPadding(0,0,0,0);
        setContentView(test);

        Globals.initialize(this);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);


        GridLayout grid = (GridLayout) LayoutInflater.from(this).inflate(R.layout.activity_test_activityy,test,false);
        grid.setColumnCount(displayMetrics.widthPixels/(Globals.dp2px*55));



        button= new Button(this);
        button.setOnClickListener(this);

        test.setGravity(Gravity.BOTTOM);
        test.addView(button);
        test.addView(new EditText(this));


    }

    @Override
    public void onClick(View vdddd) {


        int heightDiff = test.getRootView().getHeight()- test.getHeight();
        LinearLayout m = new LinearLayout(this);
        m.setBackground(new ColorDrawable(Color.TRANSPARENT));

        final PopupWindow p = new PopupWindow(this);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);


        GridLayout grid = (GridLayout) LayoutInflater.from(this).inflate(R.layout.activity_test_activityy,test,false);
        grid.setColumnCount(displayMetrics.widthPixels/(Globals.dp2px*55));


        p.setContentView(grid);
        p.setBackgroundDrawable(null);

        int v[] = new int[2];
        button.getLocationInWindow(v);

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        m.setLayoutParams(new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,displayMetrics.heightPixels-(v[1]+button.getHeight())));
        p.setHeight(displayMetrics.heightPixels-(v[1]+button.getHeight()));
        p.setWidth(displayMetrics.widthPixels);


        p.showAtLocation(button,Gravity.NO_GRAVITY,0,v[1]+button.getHeight());
        if(true)return;
        final View nn = new View(this){};
        nn.setBackground(new ColorDrawable(Color.LTGRAY));
        nn.setLayoutParams(new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT));
        m.addView(nn);
        nn.setVisibility(View.INVISIBLE);

        nn.post(new Runnable() {
            @Override
            public void run() {
                nn.setVisibility(View.VISIBLE);
                final int cx = 0;
                final int cy = 0;
                final float finalRadius = (float) Math.hypot(cx, cy);
                Animator anim = ViewAnimationUtils.createCircularReveal(nn, 120, cy, 0, (float) Math.hypot(nn.getWidth(), nn.getHeight()));
                anim.setDuration(500) ;
                anim.start();
            }
        });


    }
}
