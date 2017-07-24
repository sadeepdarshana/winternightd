package com.example.sadeep.winternightd.bottombar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.example.sadeep.winternightd.R;
import com.example.sadeep.winternightd.note.Note;

/**
 * Created by Sadeep on 7/10/2017.
 */

public class BottomBarCombined {

    private Context context;
    private LinearLayout bottomBar;

    public NoteActionsToolbar _NoteActionsToolbar;
    public NewNoteBar _NewNoteBar;


    private Note note;

    public static final int MODE_COLLAPSED = 0;

    public static final int MODE_EXPANDED = 1;



    public BottomBarCombined(Context context) {
        this.context = context;

        bottomBar = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.bottombar_combined,null);

        _NoteActionsToolbar = new NoteActionsToolbar(context,false,false){

            @Override
            protected void onSendClick(View v) {
                BottomBarCombined.this.onSendClick(v);
            }

            @Override
            protected void onAttachClick(View v) {
                BottomBarCombined.this.onAttachClick(v);
            }
        };
        _NewNoteBar = new NewNoteBar(context,true,false){
            @Override
            protected void onNoteFocused(){
                BottomBarCombined.this.setGlassModeEnabled(false);
                BottomBarCombined.this.setToolbarVisibility(true);
            }

            @Override
            protected void onNoteHeightMatured() {
                setNoteBoxMode(MODE_EXPANDED);
            }

            @Override
            protected void onNoteIsEmpty() {
                setNoteBoxMode(MODE_COLLAPSED);
            }

            @Override
            protected void onSendClick(View v) {
                BottomBarCombined.this.onSendClick(v);
            }

            @Override
            protected void onAttachClick(View v) {
                BottomBarCombined.this.onAttachClick(v);
            }

            @Override
            protected void onNoteHasContent() {
                BottomBarCombined.this.setToolbarVisibility(true);
                BottomBarCombined.this.setGlassModeEnabled(false);
            }
        };

        bottomBar.addView(_NoteActionsToolbar.getLayout(),0);
        bottomBar.addView(_NewNoteBar.getLowerLayout(),3);

        note = _NewNoteBar.getNote();
    }

    public LinearLayout getBottomBar() {
        return bottomBar;
    }

    public Note getNote() {
        return note;
    }




    protected void onAttachClick(View v) {

    }

    protected void onSendClick(View v) {

    }



    public void setNoteBoxMode(int mode){
        switch (mode) {
            case MODE_EXPANDED:
                _NoteActionsToolbar.setButtonsVisibility(true, true);
                _NewNoteBar.setButtonsVisibility(false, true);
                break;

            case MODE_COLLAPSED:
                _NoteActionsToolbar.setButtonsVisibility(false,true);
                _NewNoteBar.setButtonsVisibility(true,true);
                break;
        }

    }

    public void setToolbarVisibility(boolean visible){
        _NoteActionsToolbar.setToolbarVisibility(visible,true);
    }
    public void setGlassModeEnabled(boolean visible){
        _NewNoteBar.setGlassModeEnabled(visible,true);
    }
}
