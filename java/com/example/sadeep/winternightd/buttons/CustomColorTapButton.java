package com.example.sadeep.winternightd.buttons;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

/**
 * Created by Sadeep on 7/1/2017.
 */

public class CustomColorTapButton extends MultiStatusButton {
    public CustomColorTapButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomColorTapButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomColorTapButton(Context context) {
        super(context);
    }

    @Override
    protected int getBackgroundColorTouchDown() {
        return Color.argb(150,130,130,130);
    }

    @Override
    protected int[] getBackgroundColorForMode() {
        return new int[]{Color.TRANSPARENT};
    }

    @Override
    protected int[] getContentColorForMode() {
        return new int[]{Color.parseColor((String) getTag())};
    }
}
