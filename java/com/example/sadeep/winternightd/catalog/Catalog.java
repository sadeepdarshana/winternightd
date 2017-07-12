package com.example.sadeep.winternightd.catalog;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sadeep.winternightd.misc.Globals;
import com.example.sadeep.winternightd.notebook.NotebookInfo;

import java.util.ArrayList;

/**
 * Created by Sadeep on 7/13/2017.
 */

public class Catalog extends RecyclerView {
    public Catalog(Context context) {
        super(context);
    }

    public class MyAdapter extends RecyclerView.Adapter
    {
        ArrayList<NotebookInfo> notebookinfolist;
        Context context;
        public MyAdapter(ArrayList<NotebookInfo> data,Context context)
        {
            this.context = context;
            notebookinfolist = data;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LinearLayout ll = new LinearLayout(context);
            ll.setOrientation(LinearLayout.VERTICAL);
            ll.setBackgroundColor(Color.WHITE);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            ll.setLayoutParams(lp);
            ll.setPadding(Globals.dp2px*20,Globals.dp2px*20,Globals.dp2px*20,Globals.dp2px*20);
            ll.addView(new TextView(context));
            ll.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            return new MyViewHolder(ll);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position)
        {

            // Replace the contents of the view with that element
            MyViewHolder holder = (MyViewHolder) viewHolder;
            holder.title.setText(notebookinfolist.get(position).title);
        }

        @Override
        public int getItemCount() {
            return notebookinfolist.size();
        }


        public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView title;
        public LinearLayout ll;

        public MyViewHolder(LinearLayout v)
        {
            super(v);
            ll = v;
            title = (TextView) v.getChildAt(0);
        }
    }
}
}
