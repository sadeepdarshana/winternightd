package com.example.sadeep.winternightd.activities;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.sadeep.winternightd.R;
import com.example.sadeep.winternightd.misc.Globals;
import com.example.sadeep.winternightd.bottombar.BottomBarCombined;

public class TestActivityy extends AppCompatActivity {


    LinearLayout test;
    EditText edit;
    BottomBarCombined combined;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Globals.initialize(this);

        setContentView(R.layout.test);

        getWindow().setBackgroundDrawableResource(R.drawable.yyy);
    }

    private void Click(View v) {

    }

}
