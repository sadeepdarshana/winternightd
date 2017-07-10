package com.example.sadeep.winternightd.notebook;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.sadeep.winternightd.misc.Globals;
import com.example.sadeep.winternightd.notebookactivity.bottombar.CombinedBottomBar;

/**
 * Created by Sadeep on 6/17/2017.
 */

class XViewHolder extends RecyclerView.ViewHolder {

    public ViewGroup holdingParent;

    public static final int VIEWTYPE_LINEARLAYOUT_HEADER = 0;
    public static final int VIEWTYPE_CARDVIEW = 1;
    public static final int VIEWTYPE_LINEARLAYOUT_FOOTER = 2;



    XViewHolder(ViewGroup holdingParent) {
        super(holdingParent);
        this.holdingParent = holdingParent;
    }

    static ViewGroup generateHoldingParent(Context context, int viewType){
        if(viewType==VIEWTYPE_LINEARLAYOUT_HEADER)return createLinearLayoutHeader(context);
        if(viewType==VIEWTYPE_CARDVIEW)return createCardView(context);
        if(viewType==VIEWTYPE_LINEARLAYOUT_FOOTER)return createLinearLayoutFooter(context);
        return  null;
    }

    private static CardView createCardView(final Context context){

        final CardView[] ref = new CardView[1];

        final GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                CombinedBottomBar combinedBottomBar = new CombinedBottomBar(context);
                ((LinearLayout.LayoutParams) combinedBottomBar.attach0.getLayoutParams()).setMargins(0,0,0,0);
                ((LinearLayout.LayoutParams) combinedBottomBar.send0.getLayoutParams()).setMargins(0,0,0,0);
                //combinedBottomBar.timer.cancel();
                //combinedBottomBar.changeMode(true);
                //combinedBottomBar.setToolbarVisibility(true);
                //combinedBottomBar.changeBottomLLMode(false);
                ((ViewGroup)ref[0].getChildAt(0)).addView(combinedBottomBar.getBottombar());
                return true;
            }

        });

        final CardView card = new CardView(context){
            @Override
            public boolean dispatchTouchEvent(MotionEvent ev) {
                gestureDetector.onTouchEvent(ev);
                super.dispatchTouchEvent(ev);
                return true;
            }
        };
        ref[0]=card;

        card.setCardBackgroundColor(Color.WHITE);
        card.setCardElevation(Globals.dp2px * 2f);
        card.setRadius(Globals.dp2px * 2);
        card.setMinimumHeight(Globals.dp2px * 100);
        card.setContentPadding(Globals.dp2px * 7, Globals.dp2px * 7, Globals.dp2px * 7, Globals.dp2px * 7);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        params.setMargins(Globals.dp2px * 6, Globals.dp2px * 6, Globals.dp2px * 6, Globals.dp2px * 6);
        card.setLayoutParams(params);

        LinearLayout container = new LinearLayout(context);
        container.setLayoutParams(new LinearLayout.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        card.addView(container);
        container.setOrientation(LinearLayout.VERTICAL);


        return card;
    }


    private static LinearLayout createLinearLayoutHeader(Context context) {
        LinearLayout header = new LinearLayout(context);

        header.setPadding(0, 0, 0, 0);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, Globals.dp2px * 6);
        params.setMargins(0, 0, 0, 0);
        header.setLayoutParams(params);

        return header;
    }

    private static LinearLayout createLinearLayoutFooter(Context context){
        LinearLayout footer = new LinearLayout(context);

        footer.setPadding(0,0,0,0);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,0,0,0);
        footer.setLayoutParams(params);

        return footer;
    }
}
