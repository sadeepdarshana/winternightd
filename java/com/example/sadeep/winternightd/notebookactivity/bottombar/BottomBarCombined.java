package com.example.sadeep.winternightd.notebookactivity.bottombar;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.example.sadeep.winternightd.R;
import com.example.sadeep.winternightd.note.Note;

/**
 * Created by Sadeep on 7/10/2017.
 */

public class BottomBarCombined {

    private Context context;
    private LinearLayout bottomBar;

    private UpperLayout _UpperLayout;
    private LowerLayout _LowerLayout;

    private Note note;


    public BottomBarCombined(Context context) {
        this.context = context;

        bottomBar = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.bottombar_combined,null);

        _UpperLayout = new UpperLayout(context,false,false);
        _LowerLayout = new LowerLayout(context,true,true){
            @Override
            protected void onNoteHeightMatured() {
                _UpperLayout.setButtonsVisibility(true,true);
                _LowerLayout.setButtonsVisibility(false,true);
            }
        };

        bottomBar.addView(_UpperLayout.getUpperLayout(),0);
        bottomBar.addView(_LowerLayout.getLowerLayout(),3);

        note = _LowerLayout.getNote();
    }

    public LinearLayout getBottomBar() {
        return bottomBar;
    }
}
