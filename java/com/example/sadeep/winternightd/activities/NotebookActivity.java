package com.example.sadeep.winternightd.activities;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.sadeep.winternightd.R;
import com.example.sadeep.winternightd.attachbox.AttachBoxManager;
import com.example.sadeep.winternightd.attachbox.OnAttachBoxItemClick;
import com.example.sadeep.winternightd.bottombar.UpperLayout;
import com.example.sadeep.winternightd.buttons.customizedbuttons.AttachBoxOpener;
import com.example.sadeep.winternightd.clipboard.XClipboard;
import com.example.sadeep.winternightd.dumping.RawFieldDataStream;
import com.example.sadeep.winternightd.field.fields.SimpleIndentedField;
import com.example.sadeep.winternightd.localstorage.DataConnection;
import com.example.sadeep.winternightd.note.Note;
import com.example.sadeep.winternightd.misc.Globals;
import com.example.sadeep.winternightd.notebook.Notebook;
import com.example.sadeep.winternightd.bottombar.BottomBarCombined;
import com.example.sadeep.winternightd.selection.XSelection;
import com.example.sadeep.winternightd.temp.NoteContainingActivityRootView;


public class NotebookActivity extends NoteContainingActivity {

    private Notebook notebook;
    private BottomBarCombined newNoteBottomBar;
    private UpperLayout editNoteBottomBar;
    private Note newNote;
    private Note activeNote;
    public StatusController statusController;

    private String notebookUUID="";
    private String title="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getIntent().getExtras();
        if(b != null)notebookUUID = b.getString("notebookUUID");
        if(b != null)title = b.getString("title");

        setTheme(R.style.notebook_activity_theme);
        setContentView(R.layout.notebook_activity);

        LinearLayout bottombarSpace = (LinearLayout) findViewById(R.id.bottombar_space);
        LinearLayout notebookSpace = (LinearLayout) findViewById(R.id.notebook_space);


        Globals.initialize(this);
        DataConnection.initialize(this);
        XClipboard.initialize(this);


        newNoteBottomBar = new BottomBarCombined(this){
            @Override
            protected void onAttachClick(View v) {
                if(!((AttachBoxOpener)v).isAttachboxOpen()) {
                    ((AttachBoxOpener) v).setAttachboxOpened(true);
                    AttachBoxManager.display(v, new OnAttachBoxItemClick() {
                        @Override
                        public void buttonClicked(int attachButtonId) {
                            newNote.attachboxRequests(attachButtonId);
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


        bottombarSpace.addView(newNoteBottomBar.getBottomBar());

        notebook = new Notebook(this,notebookUUID, newNoteBottomBar);
        notebook.setClipToPadding(false);
        notebookSpace.addView(notebook);


        newNote = newNoteBottomBar.getNote();
        activeNote = newNote;

        statusController = new StatusController();

        getWindow().setBackgroundDrawableResource(R.drawable.default_wallpaper);
        setActionBarMode(NoteContainingActivity.ACTIONBAR_NORMAL);
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

    @Override
    protected Drawable getActionBarDrawable() {
        return new ColorDrawable(0xff007700);
    }
    @Override
    protected String getActionBarTitle() {
        return title;
    }

    @Override
    public void onRootLayoutSizeChanged() {
        AttachBoxManager.tryDismiss();
    }


    public void sendClick(View view){
        if(newNote.isEmpty())return;
        XSelection.clearSelections();
        NoteContainingActivityRootView.pauseLayout();
        RawFieldDataStream streams=new RawFieldDataStream(newNote.getFieldDataStream());
        //for(int i=0;i<100000;i++) {
            notebook.getNotebookDataHandler().addNote(streams);
            //if(i%100==0)d.p(i);
        //}
        notebook.refresh();
        newNote.convertToNewNoteWithOneDefaultField();
        ((SimpleIndentedField) newNote.getFieldAt(0)).getMainTextBox().requestFocus();

        NoteContainingActivityRootView.This.postDelayed(new Runnable() {
            @Override
            public void run() {
                NoteContainingActivityRootView.resumeLayout();
            }
        },300);
    }

    public Note getActiveNote() {
        return activeNote;
    }

    public void onNotebookScrolled(int dy) {
        if(statusController.isActiveNoteAtNotebook()) {

        }
        else {
            if (dy < 0) {
                if (newNote.isEmpty()) {
                    newNoteBottomBar.setGlassModeEnabled(true);
                    newNoteBottomBar.setToolbarVisibility(false);
                }
            }
            if (((LinearLayoutManager) notebook.getLayoutManager()).findFirstCompletelyVisibleItemPosition() == 0) {
                newNoteBottomBar.setGlassModeEnabled(false);
            }
        }
    }

    public class StatusController{
        private boolean activeNoteAtNotebook; //false for active note at BottomBar as the editBoxNote
        private boolean embeddedToolbarVisible;

        private Note activeNote;

       public void setNotebookActiveNote(Note note){
           activeNoteAtNotebook = true;
           activeNote = note;
           embeddedToolbarVisible = true;
           if(newNoteBottomBar.getBottomBar().getParent()!=null)((ViewGroup) newNoteBottomBar.getBottomBar().getParent()).removeView(newNoteBottomBar.getBottomBar());
       }

        public boolean isActiveNoteAtNotebook() {
            return activeNoteAtNotebook;
        }
    }

}
