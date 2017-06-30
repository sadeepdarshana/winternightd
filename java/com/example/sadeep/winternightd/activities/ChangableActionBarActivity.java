package com.example.sadeep.winternightd.activities;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.sadeep.winternightd.R;

/**
 * Created by Sadeep on 6/17/2017.
 */

public abstract class ChangableActionBarActivity extends AppCompatActivity {

    private Menu menu;
    private int mode;

    public static final int ACTIONBAR_NORMAL = 0;
    public static final int ACTIONBAR_SELECT = 1;

    protected abstract void onMenuItemPressed(int menuItem);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeActionBar(ACTIONBAR_NORMAL);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onMenuItemPressed(item.getItemId());
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();

        if(mode == ACTIONBAR_NORMAL) getMenuInflater().inflate(R.menu.notebook, menu);
        if(mode == ACTIONBAR_SELECT) getMenuInflater().inflate(R.menu.selectmenu, menu);

        return super.onPrepareOptionsMenu(menu);
    }




    public void changeActionBar(int i) {

        mode =i;

        if (i == ACTIONBAR_NORMAL) {
            this.setTitle("Home");

            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xff007700));
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayShowTitleEnabled(true);

            getSupportActionBar().setDisplayHomeAsUpEnabled(false);

            invalidateOptionsMenu();
        }
        if (i == ACTIONBAR_SELECT) {
            this.setTitle("Select");

            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xff949494));
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayShowTitleEnabled(true);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            invalidateOptionsMenu();
        }
    }
}
