package com.example.sadeep.winternightd.notebookactivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;

import com.example.sadeep.winternightd.R;
import com.example.sadeep.winternightd.activities.ChangableActionBarActivity;
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
    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTheme(R.style.AppThemeX);
        setContentView(R.layout.notebook_activity);

        bottombarSpace = (LinearLayout)findViewById(R.id.bottombar_space);
        notebookSpace = (LinearLayout)findViewById(R.id.notebook_space);


        Globals.initialize(this);
        DataConnection.initialize(this);


        bottomBar = new BottomBar(this);
        bottombarSpace.addView(bottomBar.getBottombar());

        NotebookDataHandler.createNotebook("qwerty");
        notebook = new Notebook(this,"qwerty",bottomBar);
        notebook.setClipToPadding(false);
        notebookSpace.addView(notebook);


        note = (Note) bottomBar.getBottombar().findViewById(R.id.note);

        getWindow().setBackgroundDrawableResource(R.drawable.default_wallpaper);
        //getWindow().setBackgroundDrawable(new ColorDrawable(Color.rgb(235,235,235)));

        notebook.scrollListener = new Notebook.ScrollListener() {
            @Override
            public void onScrolled(int dx, int dy) {
                if(dy<0) {
                    bottomBar.requestToolbarHide();
                    if(note.isEmpty())bottomBar.changeBottomLLOpacity(false);
                }
                if(((LinearLayoutManager)notebook.getLayoutManager()).findFirstCompletelyVisibleItemPosition()==0){
                    bottomBar.changeBottomLLOpacity(true);
                }
            }
        };

    }

    @Override
    public void onMenuItemPressed(int menuItem) {
        switch (menuItem){
            case R.id.action_cut:
                XClipboard.copySelectionToClipboard();
                XSelection.replaceSelectionWith("");
                break;
            case R.id.action_copy:
                XClipboard.copySelectionToClipboard();

                CursorPosition cp = XSelection.getSelectionStart();
                XSelection.clearSelections();
                XSelection.getSelectedNote().setCursorPosition(cp);
                break;
            case R.id.action_paste:
                XClipboard.performPaste();
        }
    }

    public void sendClick(View view){
        XRelativeLayout.pauseLayout();
        RawFieldDataStream streams=new RawFieldDataStream(note.getFieldDataStream());
        //for(int i=0;i<100000;i++) {
            notebook.getNotebookDataHandler().addNote(streams);
            //if(i%100==0)d.p(i);
        //}
        notebook.refresh();
        note.convertToNewNoteWithOneDefaultField();
        ((SimpleIndentedField)note.getFieldAt(0)).getMainTextBox().requestFocus();

        XRelativeLayout.This.postDelayed(new Runnable() {
            @Override
            public void run() {
                XRelativeLayout.resumeLayout();
            }
        },300);
        new Handler(){
            @Override
            public void handleMessage(Message msg) {
            }
        }.sendMessageDelayed(new Message(),10000);
    }

}