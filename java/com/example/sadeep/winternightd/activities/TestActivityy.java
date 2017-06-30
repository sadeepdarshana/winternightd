package com.example.sadeep.winternightd.activities;

import android.animation.Animator;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.example.sadeep.winternightd.R;
import com.example.sadeep.winternightd.attachbox.AttachItemsAdapter;

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
        button= new Button(this);
        button.setOnClickListener(this);

        test.setGravity(Gravity.BOTTOM);
        test.addView(button);
        test.addView(new EditText(this));

/*
        GridView grid = new GridView(this);
        grid.setAdapter(new AttachItemsAdapter(this));
        grid.setColumnWidth(60);
        grid.setNumColumns(GridView.AUTO_FIT);

        test.addView(grid);*/



        test.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(){
            public void onGlobalLayout(){
                int heightDiff = test.getRootView().getHeight()- test.getHeight();
                h = Math.max(heightDiff,h);
            }
        });

    }

    @Override
    public void onClick(View vdddd) {


        int heightDiff = test.getRootView().getHeight()- test.getHeight();
        LinearLayout m = new LinearLayout(this);
        m.setBackground(new ColorDrawable(Color.TRANSPARENT));

        final PopupWindow p = new PopupWindow(this);
        p.setContentView(m);
        p.setBackgroundDrawable(null);

        int v[] = new int[2];
        button.getLocationInWindow(v);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        m.setLayoutParams(new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,displayMetrics.heightPixels-(v[1]+button.getHeight())));
        p.setHeight(displayMetrics.heightPixels-(v[1]+button.getHeight()));
        p.setWidth(displayMetrics.widthPixels);


        p.showAtLocation(button,Gravity.NO_GRAVITY,0,v[1]+button.getHeight());

        final View nn = new View(this){};
        nn.setBackground(new ColorDrawable(Color.LTGRAY));
        nn.setLayoutParams(new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT));
        m.addView(nn);
        nn.setVisibility(View.INVISIBLE);
;
        nn.postDelayed(new Runnable() {
            @Override
            public void run() {
                p.dismiss();
            }
        },40000);

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
