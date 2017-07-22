package com.example.sadeep.winternightd.activities;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

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
import com.example.sadeep.winternightd.misc.NoteContainingActivityRootView;
import com.example.sadeep.winternightd.temp.d;


public class NotebookActivity extends NoteContainingActivity {

    private Notebook notebook;
    public BottomBarCombined newNoteBottomBar;
    private CardView editNoteBottomBar;
    private Note newNote;
    private Note activeNote;
    public StatusController statusController;

    private String notebookUUID="";
    private String title="";

    LinearLayout bottombarSpace;
    private LinearLayout notebookSpace;


    private Handler handler;
    private NoteContainingActivityRootView rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getIntent().getExtras();
        if(b != null)notebookUUID = b.getString("notebookUUID");
        if(b != null)title = b.getString("title");

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

        editNoteBottomBar = new CardView(this);
        RelativeLayout.LayoutParams cardparams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        editNoteBottomBar.setLayoutParams(cardparams);
        editNoteBottomBar.setCardBackgroundColor(Color.TRANSPARENT);
        editNoteBottomBar.setRadius(Globals.dp2px*23);
        editNoteBottomBar.setCardBackgroundColor(Color.parseColor("#ddffffff"));
        cardparams.setMargins(2*Globals.dp2px,3*Globals.dp2px,2*Globals.dp2px,0*Globals.dp2px);

        UpperLayout upper =new UpperLayout(this,true,true);
        CardView.LayoutParams upperlayoutparams = new CardView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        cardparams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        upper.getUpperLayout().setLayoutParams(upperlayoutparams);
        upperlayoutparams.setMargins(2*Globals.dp2px,2*Globals.dp2px,2*Globals.dp2px,1*Globals.dp2px);
        editNoteBottomBar.addView(upper.getUpperLayout());
        //upperlayoutparams.gravity = Gravity.BOTTOM;


        bottombarSpace.addView(newNoteBottomBar.getBottomBar());

        notebook = new Notebook(this,notebookUUID, newNoteBottomBar);
        notebook.setClipToPadding(false);
        notebookSpace.addView(notebook);


        newNote = newNoteBottomBar.getNote();
        activeNote = newNote;

        statusController = new StatusController();

        getWindow().setBackgroundDrawableResource(R.drawable.default_wallpaper);
        setActionBarMode(NoteContainingActivity.ACTIONBAR_NORMAL);

        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(notebook.editor.cacheNote!=null){
                    int[] xyupperlayout = new int[2];
                    notebook.editor.noteHolder.upper.getUpperLayout().getLocationInWindow(xyupperlayout);
                    int[] xybottommarker = new int[2];
                    rootView.bottomLeftMarker.getLocationInWindow(xybottommarker);
                    d.p(xyupperlayout[1]," ",xybottommarker[1]);
                    int y=xyupperlayout[1];
                    if(y>0 && y<xybottommarker[1]&&notebook.editor.cacheNote==notebook.editor.noteHolder.getNote())editNoteBottomBar.setVisibility(View.INVISIBLE);
                    else editNoteBottomBar.setVisibility(View.VISIBLE);
                }
                handler.sendEmptyMessageDelayed(0,400);
            }
        };
        handler.sendEmptyMessage(0);

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


        if(!statusController.isActiveNoteAtNotebook() &&((LinearLayoutManager)notebook.getLayoutManager()).findFirstVisibleItemPosition()<=4)notebook.scrollToPosition(0);
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
        /**
         * mode = 0 : newnote in bottombar active, newNoteBottomBar visible
         * mode = 1 : a note in notebook active, NoteHolder's built in bottombar visible
         * mode = 2 : a note in notebook active, NoteHolder's built in bottombar out of screen, editNoteBottomBar visible
         */

        private int mode;

       public void setNotebookActiveNote(Note note){
           activeNote = note;
           if(newNoteBottomBar.getBottomBar().getParent()!=null)((ViewGroup) newNoteBottomBar.getBottomBar().getParent()).removeView(newNoteBottomBar.getBottomBar());
           if(editNoteBottomBar.getParent()==null)((ViewGroup)bottombarSpace.getParent()).addView(editNoteBottomBar);
           mode = 1;
           editNoteBottomBar.setVisibility(View.INVISIBLE);
           //handler.sendEmptyMessage(0);
       }

        public boolean isActiveNoteAtNotebook() {
            return mode==1 || mode ==2;
        }

        public void setEditNoteAsActiveNote() {
            if(editNoteBottomBar.getParent()!=null)((ViewGroup) editNoteBottomBar.getParent()).removeView(editNoteBottomBar);
            if(newNoteBottomBar.getBottomBar().getParent()==null)bottombarSpace.addView(newNoteBottomBar.getBottomBar());
            mode = 0;
        }
    }

}
