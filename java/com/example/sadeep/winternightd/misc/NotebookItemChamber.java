package com.example.sadeep.winternightd.misc;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Sadeep on 7/24/2017.
 */

public class NotebookItemChamber extends LinearLayout {
    public NotebookItemChamber(Context context) {
        super(context);
    }

    public NotebookItemChamber(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NotebookItemChamber(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public NotebookItemChamber(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setChamberContent(View content, boolean animate){
        removeAllViews();
        addView(content);
        //// TODO: 7/24/2017 animate this
    }
    public void emptyChamber(boolean animate){
        removeAllViews();
        //// TODO: 7/24/2017 animate
    }

    public View getChamberContent(){
        if(getChildCount()!=0)return getChildAt(0);
        return null;
    }
}
