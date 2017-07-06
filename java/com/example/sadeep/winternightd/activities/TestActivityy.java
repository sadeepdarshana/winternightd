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
import com.example.sadeep.winternightd.selection.CursorPosition;
import com.example.sadeep.winternightd.selection.XSelection;
import com.example.sadeep.winternightd.temp.d;

import java.lang.reflect.Field;

public class TestActivityy extends AppCompatActivity {


    LinearLayout test;
    EditText edit;
    ClipboardManager clip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        test = new LinearLayout(this);
        setContentView(test);
        edit = new EditText(this){
            @Override
            protected void onCreateContextMenu(ContextMenu menu) {
                //super.onCreateContextMenu(menu);
                menu.clear();

            }@Override
            public boolean onTextContextMenuItem(int id) {
                int x=0;
                x++;
                return true;
            }

        };

        Button button;
        button= new Button(this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click(v);
            }
        });

        test.setGravity(Gravity.BOTTOM);
        test.addView(button);
        test.addView(edit);
        clip = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

    }

    private void Click(View v) {
        ClipData data = ClipData.newPlainText("xyz","123");
        clip.setPrimaryClip(data);
        edit.getParent().showContextMenuForChild(edit);

        try {
            Field field = null;
            field =TextView.class.getDeclaredField("mEditor");
            field.setAccessible(true);
            Object o = field.get(edit);
            d.wow(this);


        } catch (Exception e) {}




        try {
            Field field = null;
            field =TextView.class.getDeclaredField("sLastCutCopyOrTextChangedTime");
            field.setAccessible(true);

            field.set(field,SystemClock.uptimeMillis());


        } catch (Exception e) {}
    }

}
