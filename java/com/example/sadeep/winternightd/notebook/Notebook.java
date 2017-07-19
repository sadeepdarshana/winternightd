package com.example.sadeep.winternightd.notebook;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.sadeep.winternightd.localstorage.NotebookCursorReader;
import com.example.sadeep.winternightd.activities.NotebookActivity;
import com.example.sadeep.winternightd.bottombar.BottomBarCombined;
import com.example.sadeep.winternightd.localstorage.NotebookDataHandler;
import com.example.sadeep.winternightd.note.Note;
import com.example.sadeep.winternightd.temp.d;

/**
 * Created by Sadeep on 10/26/2016.
 */
public class Notebook extends RecyclerView {
    private NotebookActivity notebookActivity;
    private String notebookGuid;

    private NotebookDataHandler dataHandler;
    private LinearLayoutManager layoutManager;

    public BottomBarCombined _BottomBar;

    public static boolean scrollEnabled = true;

    Editor editor;

    public Notebook(NotebookActivity notebookActivity, String notebookGuid, BottomBarCombined _BottomBar) {
        super(notebookActivity);
        this.notebookActivity = notebookActivity;
        this.notebookGuid = notebookGuid;
        this._BottomBar = _BottomBar;

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
        setLayoutManager(layoutManager);
        layoutManager.scrollToPositionWithOffset(0,500);

    }

    @Override
    public void onChildAttachedToWindow(View child) {
        super.onChildAttachedToWindow(child);//if(true)return;
        if(child instanceof NotebookViewHolderUtils.NoteHolder){
            NotebookViewHolderUtils.NoteHolder noteHolder = (NotebookViewHolderUtils.NoteHolder)child;

            if(noteHolder.getNote().noteInfo.noteUUID.equals(editor.getActiveNoteUUID()))
                noteHolder.setMode(NotebookViewHolderUtils.NoteHolder.MODE_EDIT,false);
            else
                noteHolder.setMode(NotebookViewHolderUtils.NoteHolder.MODE_VIEW,false);
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
        Handler handler = new Handler();
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                scrollEnabled = true;
            }
        },100);
    }

    public class Editor{
        private Notebook notebook;
        private String activeNoteUUID;
        private Note cacheNote;

        public Editor(Notebook notebook) {
            this.notebook = notebook;
        }

        public void setActiveNote(NotebookViewHolderUtils.NoteHolder noteHolder){
            for(int i=0;i<notebook.getChildCount();i++){
                if(notebook.getChildAt(i)!=noteHolder && notebook.getChildAt(i)instanceof NotebookViewHolderUtils.NoteHolder)
                    ((NotebookViewHolderUtils.NoteHolder)notebook.getChildAt(i)).setMode(NotebookViewHolderUtils.NoteHolder.MODE_VIEW,true);
            }

            noteHolder.setMode(NotebookViewHolderUtils.NoteHolder.MODE_EDIT,true);
            activeNoteUUID = noteHolder.getNote().noteInfo.noteUUID;
            cacheNote = noteHolder.getNote();

            ((NotebookActivity)getContext()).statusController.setNotebookActiveNote(noteHolder.getNote());
        }

        public String getActiveNoteUUID() {
            return activeNoteUUID;
        }

        public Note getCacheNote() {
            return cacheNote;
        }
    }
}
