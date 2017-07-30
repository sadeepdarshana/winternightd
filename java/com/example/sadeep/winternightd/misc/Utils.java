package com.example.sadeep.winternightd.misc;

import android.app.Activity;
import android.content.Context;
import android.text.Spanned;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.sadeep.winternightd.spans.RichText;

/**
 * Created by Sadeep on 7/5/2017.
 */

public class Utils {
    public static CharSequence duplicateCharSequence(CharSequence seq){
        return RichText.generateRichText((Spanned) seq).getCharSequence();
    }

    public static int getWidth(View view){
        if(view.getWidth()!=0)return view.getWidth();
        view.measure(View.MeasureSpec.UNSPECIFIED,View.MeasureSpec.UNSPECIFIED);
        return view.getMeasuredWidth();
    }
    public static int getHeight(View view){
        if(view.getHeight()!=0)return view.getHeight();
        view.measure(View.MeasureSpec.UNSPECIFIED,View.MeasureSpec.UNSPECIFIED);
        return view.getMeasuredHeight();
    }

    public static void hideKeyboard(Context activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = ((Activity)activity).getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
