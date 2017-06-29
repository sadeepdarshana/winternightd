package com.example.sadeep.winternightd.notebook;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Sadeep on 6/17/2017.
 */

class CardViewHolder extends RecyclerView.ViewHolder {

    public CardView card;
    public LinearLayout ll;
    public CardViewHolder(CardView card) {
        super(card);
        this.card = card;
    }
    public CardViewHolder(LinearLayout ll) {
        super(ll);
        this.ll = ll;
    }
}
