package com.example.sadeep.winternightd.notebookactivity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;

import com.example.sadeep.winternightd.R;
import com.example.sadeep.winternightd.activities.ChangableActionBarActivity;
import com.example.sadeep.winternightd.attachbox.AttachBoxManager;
import com.example.sadeep.winternightd.clipboard.XClipboard;
import com.example.sadeep.winternightd.dumping.RawFieldDataStream;
import com.example.sadeep.winternightd.field.fields.SimpleIndentedField;
import com.example.sadeep.winternightd.localstorage.DataConnection;
import com.example.sadeep.winternightd.localstorage.NotebookDataHandler;
import com.example.sadeep.winternightd.note.Note;
import com.example.sadeep.winternightd.misc.Globals;
import com.example.sadeep.winternightd.notebook.Notebook;
import com.example.sadeep.winternightd.selection.CursorPosition;
import com.example.sadeep.winternightd.selection.XSelection;
import com.example.sadeep.winternightd.temp.XRelativeLayout;


public class NotebookActivity extends ChangableActionBarActivity {

    private LinearLayout bottombarSpace;
    private LinearLayout notebookSpace;
    private Notebook notebook;
    private BottomBar bottomBar;
    private Note editboxNote;


    private Note activeNote;
    NotebookActivity This;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        This = this;


        setTheme(R.style.AppThemeX);
        setContentView(R.layout.notebook_activity);

        bottombarSpace = (LinearLayout)findViewById(R.id.bottombar_space);
        notebookSpace = (LinearLayout)findViewById(R.id.notebook_space);


        Globals.initialize(this);
        DataConnection.initialize(this);
        XClipboard.initialize(this);


        bottomBar = new BottomBar(this);
        bottombarSpace.addView(bottomBar.getBottombar());

        NotebookDataHandler.createNotebook("qwerty");
        notebook = new Notebook(this,"qwerty",bottomBar);
        notebook.setClipToPadding(false);
        notebookSpace.addView(notebook);


        editboxNote = (Note) bottomBar.getBottombar().findViewById(R.id.note);
        activeNote = editboxNote;

        getWindow().setBackgroundDrawableResource(R.drawable.default_wallpaper);
        //getWindow().setBackgroundDrawable(new ColorDrawable(Color.rgb(235,235,235)));

        notebook.scrollListener = new Notebook.ScrollListener() {
            @Override
            public void onScrolled(int dx, int dy) {
                if(dy<0) {
                    bottomBar.requestToolbarHide();
                    if(editboxNote.isEmpty())bottomBar.changeBottomLLOpacity(false);
                }
                if(((LinearLayoutManager)notebook.getLayoutManager()).findFirstCompletelyVisibleItemPosition()==0){
                    bottomBar.changeBottomLLOpacity(true);
                }
            }
        };


        final XRelativeLayout root = (XRelativeLayout) notebookSpace.getParent();
        root.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                try{
                    AttachBoxManager.popupWindow.dismiss();}catch (Exception e){}

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

}
