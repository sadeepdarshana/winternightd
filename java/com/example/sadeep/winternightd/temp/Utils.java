package com.example.sadeep.winternightd.temp;

import android.text.Spanned;
import android.view.View;

import com.example.sadeep.winternightd.spans.RichText;

/**
 * Created by Sadeep on 7/5/2017.
 */

public class Utils {
    public static CharSequence duplicateCharSequence(CharSequence seq){
        return RichText.generateRichText((Spanned) seq).getCharSequence();
    }

    public static int getWidth(View view){
        view.measure(View.MeasureSpec.UNSPECIFIED,View.MeasureSpec.UNSPECIFIED);
        return view.getMeasuredWidth();
    }
    public static int getHeight(View view){
        view.measure(View.MeasureSpec.UNSPECIFIED,View.MeasureSpec.UNSPECIFIED);
        return view.getMeasuredHeight();
    }
}
