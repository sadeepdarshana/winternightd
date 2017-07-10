package com.example.sadeep.winternightd.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.sadeep.winternightd.R;
import com.example.sadeep.winternightd.misc.Globals;
import com.example.sadeep.winternightd.notebookactivity.bottombar.LowerLayout;
import com.example.sadeep.winternightd.notebookactivity.bottombar.UpperLayout;
import com.example.sadeep.winternightd.temp.QButton;

public class TestActivityy extends AppCompatActivity {


    LinearLayout test;
    EditText edit;
    UpperLayout upperLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        test = new LinearLayout(this);
        test.setOrientation(LinearLayout.VERTICAL);
        setContentView(R.layout.test);
        getWindow().setBackgroundDrawableResource(R.drawable.default_wallpaper);

        Globals.initialize(this);

        upperLayout = new UpperLayout(this,true,true);

        ((ViewGroup)findViewById(R.id.bottombar_space)).addView(upperLayout.getUpperLayout());
        ((ViewGroup)findViewById(R.id.notebook_space)).addView(new QButton(this){

            @Override
            public void onClick(View v) {
                upperLayout.setButtonsVisibility(!upperLayout.getButtonVisibility(),true);
            }
        });
        ((ViewGroup)findViewById(R.id.notebook_space)).addView(new QButton(this){

            @Override
            public void onClick(View v) {
                upperLayout.setToolbarVisibility(!upperLayout.getToolbarVisibility(),true);
            }
        });
    }

    private void Click(View v) {

    }

}
