package com.example.sadeep.winternightd.activities;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;

import com.example.sadeep.winternightd.R;
import com.example.sadeep.winternightd.animation.XAnimation;
import com.example.sadeep.winternightd.attachbox.AttachBoxManager;
import com.example.sadeep.winternightd.attachbox.OnAttachBoxItemClick;
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
import com.example.sadeep.winternightd.misc.NoteContainingActivityRootView;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;


public class NotebookActivity extends NoteContainingActivity {

    public static long classContextSessionId;   //used for the GC of the activity
    public long contextSessionId;               //   ''

    private Notebook notebook;
    public BottomBarCombined newNoteBottomBar;
    private Note newNote;
    private Note activeNote;

    private String notebookUUID="";
    private String title="";

    LinearLayout bottombarSpace;
    private LinearLayout notebookSpace;

    public NoteContainingActivityRootView rootView;

    private CountDownTimer notebookActivityTimer;

    public NotebookActivity(){
        contextSessionId=new java.util.Random().nextLong();
        classContextSessionId=contextSessionId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getIntent().getExtras();
        if(b != null) {
            notebookUUID = b.getString("notebookUUID");
            title = b.getString("title");
        }
        else {
            notebookUUID = "b237e56fe938a4e80b75b1be38f58b06e";
            title = "Home";
        }

        setTheme(R.style.notebook_activity_theme);
        setContentView(R.layout.notebook_activity);

        bottombarSpace = (LinearLayout) findViewById(R.id.bottombar_space);
        notebookSpace = (LinearLayout) findViewById(R.id.notebook_space);
        rootView = (NoteContainingActivityRootView)notebookSpace.getParent();

        Globals.initialize(this);
        DataConnection.initialize(this);
        XClipboard.initialize(this);


        newNoteBottomBar = new BottomBarCombined(this){
            @Override
            protected void onAttachClick(View v) {
                if(!((AttachBoxOpener)v).isAttachboxOpen()) {
                    ((AttachBoxOpener) v).setAttachboxOpened(true);
                    AttachBoxManager.display(v,((NotebookActivity)v.getContext()).rootView.bottomLeftMarker, new OnAttachBoxItemClick() {
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

        bottombarSpace.addView(newNoteBottomBar);

        notebook = new Notebook(this,notebookUUID, newNoteBottomBar);
        notebook.setClipToPadding(false);
        notebookSpace.addView(notebook);


        newNote = newNoteBottomBar.getNote();
        activeNote = newNote;

        //getWindow().setBackgroundDrawableResource(R.drawable.yyy);
        setActionBarMode(NoteContainingActivity.ACTIONBAR_NORMAL);


        notebookActivityTimer =new CountDownTimer(2000000000, 500)
        {
            public void onTick(long millisUntilFinished)
            {
                onNotebookScrolled(-1);
                if(contextSessionId!=NotebookActivity.classContextSessionId)this.cancel();
                if(notebook.editor.activeNote==null){
                    if(!newNoteBottomBar.layoutShown) {
                        XAnimation.addAndExpand(newNoteBottomBar,bottombarSpace,0,300,XAnimation.DIMENSION_HEIGHT,0,newNoteBottomBar.storedHeight,WRAP_CONTENT);
                        newNoteBottomBar.layoutShown=true;
                        onNotebookScrolled(-1);
                    }
                }else {
                    if(newNoteBottomBar.layoutShown) {
                        newNoteBottomBar.layoutShown=false;
                        newNoteBottomBar.storedHeight=newNoteBottomBar.getHeight();
                        XAnimation.squeezeAndRemove(newNoteBottomBar,300,XAnimation.DIMENSION_HEIGHT,0);
                    }
                }
            }

            public void onFinish(){}
        };

        newNoteBottomBar.postDelayed(new Runnable() {
            @Override
            public void run() {
                notebookActivityTimer.start();
            }
        },200);
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
        return new ColorDrawable(0xff449944);
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
            notebook.getNotebookDataHandler().addNewNote(newNote);
            //if(i%100==0)d.p(i);
        //}
        notebook.refresh();
        notebook.scrollToPosition(0);
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
        if(notebook.editor.activeNote ==null) {
            if (dy < 0  && ((LinearLayoutManager) notebook.getLayoutManager()).findFirstCompletelyVisibleItemPosition() != 0 ) {
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

    public Notebook getNotebook() {
        return notebook;
    }
}
