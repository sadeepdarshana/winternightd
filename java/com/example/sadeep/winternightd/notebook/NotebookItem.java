package com.example.sadeep.winternightd.notebook;

import android.content.ContentValues;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.sadeep.winternightd.R;
import com.example.sadeep.winternightd.misc.TransparentCard;

/**
 * Created by Sadeep on 7/24/2017.
 */

public class NotebookItem {
    private Context context;
    private TransparentCard notebookItem;
    private ViewGroup lowerChamber;
    private ViewGroup upperChamber;

    public NotebookItem(Context context) {
        this.context = context;
        notebookItem = (TransparentCard) LayoutInflater.from(context).inflate(R.layout.notebookitem,null);
        lowerChamber = (ViewGroup) notebookItem.findViewById(R.id.lowerchamber);
        upperChamber = (ViewGroup) notebookItem.findViewById(R.id.upperchamber);
    }

    public TransparentCard getNotebookItem() {
        return notebookItem;
    }

    public ViewGroup getLowerChamber() {
        return lowerChamber;
    }

    public ViewGroup getUpperChamber() {
        return upperChamber;
    }
}
