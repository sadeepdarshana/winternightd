package com.example.sadeep.winternightd.notebook;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.sadeep.winternightd.R;
import com.example.sadeep.winternightd.misc.Globals;
import com.example.sadeep.winternightd.misc.NotebookItemChamber;
import com.example.sadeep.winternightd.misc.TransparentCard;

/**
 * Created by Sadeep on 7/24/2017.
 */

public class NotebookItem  extends TransparentCard{
    private Context context;

    private LinearLayout notebookItemInnerLayout;
    private NotebookItemChamber lowerChamber;
    private NotebookItemChamber upperChamber;
    private LinearLayout noteSpace;

    public NotebookItem(Context context) {
        super(context);
        this.context = context;

        setCardElevation(2f* Globals.dp2px);

        notebookItemInnerLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.notebookitem,null);
        addView(notebookItemInnerLayout);

        TransparentCard.LayoutParams params = new TransparentCard.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        notebookItemInnerLayout.setLayoutParams(params);

        lowerChamber = (NotebookItemChamber) notebookItemInnerLayout.findViewById(R.id.lowerchamber);
        upperChamber = (NotebookItemChamber) notebookItemInnerLayout.findViewById(R.id.upperchamber);
        noteSpace = (LinearLayout) notebookItemInnerLayout.findViewById(R.id.notespace);


    }


    public NotebookItemChamber getLowerChamber() {
        return lowerChamber;
    }

    public NotebookItemChamber getUpperChamber() {
        return upperChamber;
    }

    public LinearLayout getNoteSpace() {
        return noteSpace;
    }

}
