package com.example.sadeep.winternightd.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.sadeep.winternightd.R;
import com.example.sadeep.winternightd.misc.Globals;
import com.example.sadeep.winternightd.notebookactivity.bottombar.BottomBarCombined;
import com.example.sadeep.winternightd.notebookactivity.bottombar.LowerLayout;
import com.example.sadeep.winternightd.notebookactivity.bottombar.UpperLayout;
import com.example.sadeep.winternightd.temp.QButton;

public class TestActivityy extends AppCompatActivity {


    LinearLayout test;
    EditText edit;
    BottomBarCombined combined;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        test = new LinearLayout(this);
        test.setOrientation(LinearLayout.VERTICAL);
        setContentView(R.layout.test);
        getWindow().setBackgroundDrawableResource(R.drawable.default_wallpaper);

        Globals.initialize(this);

        combined = new BottomBarCombined(this){
            @Override
            protected void onSendClick(View v) {

            }
        };

        ((ViewGroup)findViewById(R.id.bottombar_space)).addView(combined.getBottomBar());
    }

    private void Click(View v) {

    }

}
