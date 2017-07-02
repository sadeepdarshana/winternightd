package com.example.sadeep.winternightd.buttons.customizedbuttons;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import com.example.sadeep.winternightd.buttons.MultiStatusButton;

/**
 * Created by Sadeep on 6/18/2017.
 */

public class AttachButton extends MultiStatusButton implements AttachBoxOpener {
    public AttachButton(Context context) {
        super(context);
    }

    public AttachButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getBackgroundColorTouchDown() {
        return Color.parseColor("#EEEEEE");
    }

    @Override
    protected int[] getBackgroundColorForMode() {
        return new int[]{ Color.parseColor("#000000ff")        , Color.argb(20, 0, 0, 0)};
    }

    @Override
    protected int[] getContentColorForMode() {
        return  new int[]{Color.parseColor("#00AC00"), Color.parseColor("#1EE44A")   };
    }
}
