package com.example.sadeep.winternightd.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.example.sadeep.winternightd.R;
import com.example.sadeep.winternightd.attachbox.AttachItemsAdapter;

public class TestActivityy extends AppCompatActivity {


    LinearLayout test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        test = new LinearLayout(this);
        setContentView(test);

        GridView grid = new GridView(this);
        grid.setAdapter(new AttachItemsAdapter(this));
        grid.setColumnWidth(60);
        grid.setNumColumns(GridView.AUTO_FIT);

        test.addView(grid);


    }
}
