package com.example.sadeep.winternightd.attachbox;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;

import java.util.Arrays;
import java.util.Collections;

/**
 * Created by Sadeep on 6/30/2017.
 */

public class AttachItemsAdapter extends BaseAdapter {
    int count=500;
    View[] items = new View[count];
    public AttachItemsAdapter(Context context) {
        for(int i=0;i<count;i++){
            items[i]=new EditText(context);
            items[i].setLayoutParams(new GridView.LayoutParams(200,200));
            ((EditText)items[i]).setText(Integer.toString(i));
        }
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Object getItem(int position) {
        return items[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;//position/3;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return items[position];
    }
}
