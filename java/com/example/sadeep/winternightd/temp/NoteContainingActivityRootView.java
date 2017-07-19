package com.example.sadeep.winternightd.temp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.sadeep.winternightd.activities.NoteContainingActivity;

/**
 * Created by Sadeep on 6/22/2017.
 */

public class NoteContainingActivityRootView extends RelativeLayout {

    private static boolean layoutEnabled = true;
    public static NoteContainingActivityRootView This = null;


    public NoteContainingActivityRootView(Context context, AttributeSet attrs) {
        super(context, attrs);
        This = this;

        addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                ((NoteContainingActivity)getContext()).onRootLayoutSizeChanged();
            }
        });
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
