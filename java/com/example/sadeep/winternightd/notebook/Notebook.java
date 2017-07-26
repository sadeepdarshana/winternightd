package com.example.sadeep.winternightd.notebook;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.sadeep.winternightd.localstorage.NotebookCursorReader;
import com.example.sadeep.winternightd.activities.NotebookActivity;
import com.example.sadeep.winternightd.bottombar.BottomBarCombined;
import com.example.sadeep.winternightd.localstorage.NotebookDataHandler;
import com.example.sadeep.winternightd.note.Note;
import com.example.sadeep.winternightd.notebook.NotebookViewHolderUtils.NoteHolder;

import java.util.ArrayList;

/**
 * Created by Sadeep on 10/26/2016.
 */
public class Notebook extends RecyclerView {
    private NotebookActivity notebookActivity;
    private String notebookGuid;

    private NotebookDataHandler dataHandler;
    private LinearLayoutManager layoutManager;

    public BottomBarCombined bottomBar;

    public static boolean scrollEnabled = true;

    public Editor editor;

    public NoteHolderController noteHolderController = new NoteHolderController();

    private static Handler scrollresumer = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            scrollEnabled = true;
        }
    };;

    public Notebook(NotebookActivity notebookActivity, String notebookGuid, BottomBarCombined bottomBar) {
        super(notebookActivity);
        this.notebookActivity = notebookActivity;
        this.notebookGuid = notebookGuid;
        this.bottomBar = bottomBar;

        editor = new Editor(this);

        dataHandler = new NotebookDataHandler(notebookGuid);
        setAdapter(new NotebookAdapter(notebookActivity,new NotebookCursorReader(dataHandler.getCursor()),this));

        layoutManager = new LinearLayoutManager(notebookActivity) {
            @Override
            public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
                if (Notebook.scrollEnabled)return super.scrollVerticallyBy(dy,recycler,state);
                return 0;
            }
        };

        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        setLayoutManager(layoutManager);

        postDelayed(new Runnable() {
            @Override
            public void run() {
                layoutManager.scrollToPositionWithOffset(0,500);
            }
        },2);

    }

    @Override
    public void onChildAttachedToWindow(View child) {
        super.onChildAttachedToWindow(child);//if(true)return;
        if(child instanceof NoteHolder){
            NoteHolder noteHolder = (NoteHolder)child;

            if(noteHolder.getNote().noteInfo.noteUUID.equals(editor.getActiveNoteUUID()))
                noteHolder.setMode(NoteHolderModes.MODE_EDIT,false);
            else
                noteHolder.setMode(NoteHolderModes.MODE_VIEW,false);
        }
    }

    public NotebookDataHandler getNotebookDataHandler() {
        return dataHandler;
    }

    public void refresh() {
        dataHandler = new NotebookDataHandler(notebookGuid);
        setAdapter(new NotebookAdapter(notebookActivity,new NotebookCursorReader(dataHandler.getCursor()),this));
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
        notebookActivity.onNotebookScrolled(dy);
    }


    public static void suspendScrollTemporary() {
        scrollEnabled = false;
        scrollresumer.removeCallbacksAndMessages(null);
        scrollresumer.sendEmptyMessageDelayed(0,400);
    }

    public class NoteHolderController{
        private ArrayList<NoteHolder>noteHolders = new ArrayList<>();

        public void addNoteHolder(NoteHolder noteHolder){
            noteHolders.add(noteHolder);
        }

        public void setAllNoteHoldersModeExcept(int mode,NoteHolder except,boolean animate){
            for(int i=0;i<noteHolders.size();i++){
                NoteHolder noteHolder = noteHolders.get(i);
                if(noteHolder == except)continue;
                if(noteHolder.getMode()!=mode)noteHolder.setMode(mode,animate);
            }
        }
    }

    public class Editor{
        private Notebook notebook;
        public Note activeNote;
        public NoteHolder noteHolder;


        public Editor(Notebook notebook) {
            this.notebook = notebook;
        }

        public void setActiveNote(NoteHolder noteHolder){
            if(activeNote ==noteHolder.getNote())return;

            if(getChildAdapterPosition(noteHolder)==1)
                new CountDownTimer(600, 20)
                {
                    public void onTick(long millisUntilFinished)
                    {
                        notebook.smoothScrollToPosition(0);
                    }
                    public void onFinish(){}
                }.start();

            for(int i=0;i<notebook.getChildCount();i++){
                if(notebook.getChildAt(i)!=noteHolder && notebook.getChildAt(i)instanceof NoteHolder)
                    ((NoteHolder)notebook.getChildAt(i)).setMode(NoteHolderModes.MODE_VIEW,true);
            }

            noteHolder.setMode(NoteHolderModes.MODE_EDIT,true);
            activeNote = noteHolder.getNote();

        }

        public String getActiveNoteUUID() {
            if(activeNote !=null)return activeNote.noteInfo.noteUUID;
            return null;
        }


        public void pushActiveNote() {
            if(activeNote==null)return;
            notebook.getNotebookDataHandler().addExistingNote(activeNote);
            activeNote = null;
            refresh();
            notebook.scrollToPosition(0);
        }
    }
}
