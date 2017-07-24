package com.example.sadeep.winternightd.notebook;

import android.content.ContentValues;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.sadeep.winternightd.R;
import com.example.sadeep.winternightd.misc.NotebookItemChamber;
import com.example.sadeep.winternightd.misc.TransparentCard;

/**
 * Created by Sadeep on 7/24/2017.
 */

public class NotebookItem {
    private Context context;
    private TransparentCard notebookItem;
    private NotebookItemChamber lowerChamber;
    private NotebookItemChamber upperChamber;

    public NotebookItem(Context context) {
        this.context = context;
        notebookItem = (TransparentCard) LayoutInflater.from(context).inflate(R.layout.notebookitem,null);
        lowerChamber = (NotebookItemChamber) notebookItem.findViewById(R.id.lowerchamber);
        upperChamber = (NotebookItemChamber) notebookItem.findViewById(R.id.upperchamber);
    }

    public TransparentCard getNotebookItem() {
        return notebookItem;
    }

    public NotebookItemChamber getLowerChamber() {
        return lowerChamber;
    }

    public NotebookItemChamber getUpperChamber() {
        return upperChamber;
    }

    public Context getContext() {
        return context;
    }
}
