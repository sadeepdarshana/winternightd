package com.example.sadeep.winternightd.notebook;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.example.sadeep.winternightd.note.Note;
import com.example.sadeep.winternightd.selection.XSelection;

/**
 * Created by Sadeep on 6/17/2017.
 */

class CardViewHolder extends RecyclerView.ViewHolder {

    public CardView card;
    public LinearLayout ll;
    public CardViewHolder(final CardView card) {
        super(card);
        this.card = card;

        GestureDetector gestureDetector = new GestureDetector(card.getContext(), new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                ((Note)card.getChildAt(0)).setIsEditable(true);
                return true;
            }

        });
        

    }
    public CardViewHolder(LinearLayout ll) {
        super(ll);
        this.ll = ll;
    }
}
