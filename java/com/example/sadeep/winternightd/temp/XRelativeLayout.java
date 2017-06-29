package com.example.sadeep.winternightd.temp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by Sadeep on 6/22/2017.
 */

public class XRelativeLayout extends RelativeLayout {

    private static boolean layoutEnabled = true;
    public static XRelativeLayout This = null;


    public XRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        This = this;
    }

    public XRelativeLayout(Context context) {
        super(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        This=this;
        if(layoutEnabled)super.onLayout(changed, l, t, r, b);
    }
    int x,y;
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(layoutEnabled){
            x=widthMeasureSpec;
            y=heightMeasureSpec;
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
        else super.onMeasure(x, y);
    }



    public static void resumeLayout() {
        layoutEnabled =true;
        if(This!=null)This.requestLayout();

    }

    public static void pauseLayout(){
        layoutEnabled = false;
    }
}
