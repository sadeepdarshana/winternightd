package com.example.sadeep.winternightd.activities;

import android.animation.Animator;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayout;
import android.util.DisplayMetrics;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sadeep.winternightd.R;
import com.example.sadeep.winternightd.clipboard.XClipboard;
import com.example.sadeep.winternightd.misc.Globals;
import com.example.sadeep.winternightd.notebookactivity.bottombar.LowerLayout;
import com.example.sadeep.winternightd.selection.CursorPosition;
import com.example.sadeep.winternightd.selection.XSelection;
import com.example.sadeep.winternightd.temp.QButton;
import com.example.sadeep.winternightd.temp.d;

import java.lang.reflect.Field;

public class TestActivityy extends AppCompatActivity {


    LinearLayout test;
    EditText edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        test = new LinearLayout(this);
        test.setOrientation(LinearLayout.VERTICAL);
        setContentView(R.layout.test);

        Globals.initialize(this);

        ((ViewGroup)findViewById(R.id.bottombar_space)).addView(new LowerLayout(this,true).getLowerLayout());
        test.addView(new QButton(this){

            @Override
            public void onClick(View v) {
                d.wow(TestActivityy.this);
            }
        });
    }

    private void Click(View v) {

    }

}
