package com.example.sadeep.winternightd.notebookactivity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;

import com.example.sadeep.winternightd.R;
import com.example.sadeep.winternightd.activities.ChangableActionBarActivity;
import com.example.sadeep.winternightd.attachbox.AttachBoxManager;
import com.example.sadeep.winternightd.attachbox.OnAttachBoxItemClick;
import com.example.sadeep.winternightd.buttons.customizedbuttons.AttachBoxOpener;
import com.example.sadeep.winternightd.clipboard.XClipboard;
import com.example.sadeep.winternightd.dumping.RawFieldDataStream;
import com.example.sadeep.winternightd.field.fields.SimpleIndentedField;
import com.example.sadeep.winternightd.localstorage.DataConnection;
import com.example.sadeep.winternightd.localstorage.NotebookDataHandler;
import com.example.sadeep.winternightd.note.Note;
import com.example.sadeep.winternightd.misc.Globals;
import com.example.sadeep.winternightd.notebook.Notebook;
import com.example.sadeep.winternightd.notebookactivity.bottombar.BottomBarCombined;
import com.example.sadeep.winternightd.notebookactivity.bottombar.CombinedBottomBar;
import com.example.sadeep.winternightd.selection.XSelection;
import com.example.sadeep.winternightd.temp.XRelativeLayout;


public class NotebookActivity extends ChangableActionBarActivity {

    private LinearLayout bottombarSpace;
    private LinearLayout notebookSpace;
    private Notebook notebook;
    private BottomBarCombined _BottomBar;
    private Note editboxNote;


    private Note activeNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTheme(R.style.AppThemeX);
        setContentView(R.layout.notebook_activity);

        bottombarSpace = (LinearLayout)findViewById(R.id.bottombar_space);
        notebookSpace = (LinearLayout)findViewById(R.id.notebook_space);


        Globals.initialize(this);
        DataConnection.initialize(this);
        XClipboard.initialize(this);


        _BottomBar = new BottomBarCombined(this){
            @Override
            protected void onAttachClick(View v) {
                if(!((AttachBoxOpener)v).isAttachboxOpen()) {
                    ((AttachBoxOpener) v).setAttachboxOpened(true);
                    AttachBoxManager.display(v, new OnAttachBoxItemClick() {
                        @Override
                        public void buttonClicked(int attachButtonId) {
                            editboxNote.attachboxRequests(attachButtonId);
                        }
                    });
                }else{
                    try {
                        AttachBoxManager.popupWindow.dismiss();
                    }catch (Exception e){}
                }
            }

            @Override
            protected void onSendClick(View v) {
                sendClick(v);
            }
        };
        bottombarSpace.addView(_BottomBar.getBottomBar());

        NotebookDataHandler.createNotebook("qwerty");
        notebook = new Notebook(this,"qwerty", _BottomBar);
        notebook.setClipToPadding(false);
        notebookSpace.addView(notebook);


        editboxNote = _BottomBar.getNote();
        activeNote = editboxNote;

        getWindow().setBackgroundDrawableResource(R.drawable.default_wallpaper);


        final XRelativeLayout root = (XRelativeLayout) notebookSpace.getParent();
        root.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                try{
                    AttachBoxManager.popupWindow.dismiss();
                }
                catch (Exception e){}

            }
        });

    }

    @Override
    public void onMenuItemPressed(int menuItem) {
        switch (menuItem){
            case R.id.action_cut:
                XClipboard.requestCut();
                break;
            case R.id.action_copy:
                XClipboard.requestCopy();
                break;
            case R.id.action_paste:
                XClipboard.requestPaste(this);
                break;
            case android.R.id.home:
                XSelection.clearSelections();
        }
    }

    public void sendClick(View view){
        if(editboxNote.isEmpty())return;
        XSelection.clearSelections();
        XRelativeLayout.pauseLayout();
        RawFieldDataStream streams=new RawFieldDataStream(editboxNote.getFieldDataStream());
        //for(int i=0;i<100000;i++) {
            notebook.getNotebookDataHandler().addNote(streams);
            //if(i%100==0)d.p(i);
        //}
        notebook.refresh();
        editboxNote.convertToNewNoteWithOneDefaultField();
        ((SimpleIndentedField) editboxNote.getFieldAt(0)).getMainTextBox().requestFocus();

        XRelativeLayout.This.postDelayed(new Runnable() {
            @Override
            public void run() {
                XRelativeLayout.resumeLayout();
            }
        },300);
    }

    public Note getActiveNote() {
        return activeNote;
    }

    public void onNotebookScrolled(int dx, int dy) {

        if(dy<0) {
            if(editboxNote.isEmpty()) {
                _BottomBar.setGlassModeEnabled(true);
                _BottomBar.setToolbarVisibility(false);
            }
        }
        if(((LinearLayoutManager)notebook.getLayoutManager()).findFirstCompletelyVisibleItemPosition()==0){
            _BottomBar.setGlassModeEnabled(false);
        }

    }
}
