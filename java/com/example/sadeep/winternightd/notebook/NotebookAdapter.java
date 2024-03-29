package com.example.sadeep.winternightd.notebook;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.example.sadeep.winternightd.localstorage.NotebookCursorReader;
import com.example.sadeep.winternightd.note.Note;
import com.example.sadeep.winternightd.note.NoteFactory;
import com.example.sadeep.winternightd.selection.XSelection;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import static com.example.sadeep.winternightd.notebook.NoteHolderModes.MODE_EDIT;
import static com.example.sadeep.winternightd.notebook.NoteHolderModes.MODE_VIEW;
import static com.example.sadeep.winternightd.notebook.NotebookViewHolderUtils.VIEWTYPE_HEIGHT_BALANCER;
import static com.example.sadeep.winternightd.notebook.NotebookViewHolderUtils.VIEWTYPE_NEWNOTEBAR;
import static com.example.sadeep.winternightd.notebook.NotebookViewHolderUtils.VIEWTYPE_HEADER;
import static com.example.sadeep.winternightd.notebook.NotebookViewHolderUtils.VIEWTYPE_NOTE_HOLDER;

/**
 * Created by Sadeep on 6/17/2017.
 */

class NotebookAdapter extends RecyclerView.Adapter <NotebookViewHolderUtils.NotebookViewHolder> {
    private NotebookCursorReader cursor;

    private Context context;
    private Notebook notebook;

    private ConcurrentHashMap<String,Note> cache = new ConcurrentHashMap<>();

    public NotebookAdapter(final Context context, final NotebookCursorReader cursor, final Notebook notebook) {
        this.cursor = cursor;
        this.context = context;
        this.notebook = notebook;


        // put this to different thread


        new Thread(new Runnable() {
            final int maxNumberOfNotesToCaches = 200;
            public void run(){

                int noteCount = cursor.getCursor().getCount();

                for(int i=0;i<Math.min(noteCount,maxNumberOfNotesToCaches);i++){
                    String noteUUID = cursor.getNoteInfo(i).noteUUID;
                    if(!cache.containsKey(noteUUID)){
                        Note note = NoteFactory.fromFieldDataStream(context, cursor.getFieldDataStream(i), false, notebook, cursor.getNoteInfo(i));
                        cache.putIfAbsent(noteUUID,note);
                    }
                }

            }
        }).start();


    }


    @Override
    public NotebookViewHolderUtils.NotebookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == VIEWTYPE_HEADER)return new NotebookViewHolderUtils.NotebookViewHolder(new NotebookViewHolderUtils.Header(context));
        if(viewType == VIEWTYPE_NOTE_HOLDER)return  new NotebookViewHolderUtils.NotebookViewHolder(new NotebookViewHolderUtils.NoteHolder(context,notebook));
        if(viewType == VIEWTYPE_NEWNOTEBAR)return  new NotebookViewHolderUtils.NotebookViewHolder(new NotebookViewHolderUtils.NewNoteBarHolder(context,notebook));
        if(viewType == VIEWTYPE_HEIGHT_BALANCER)return  new NotebookViewHolderUtils.NotebookViewHolder(new NotebookViewHolderUtils.HeightBalancer(context,notebook));

        return null;
    }

    @Override
    public void onBindViewHolder(NotebookViewHolderUtils.NotebookViewHolder holder, int position) {

        if(getItemViewType(position)==VIEWTYPE_NEWNOTEBAR){
            NotebookViewHolderUtils.NewNoteBarHolder newNoteBarHolder = (NotebookViewHolderUtils.NewNoteBarHolder) holder.holder;
            newNoteBarHolder.bind();
        }
        if(getItemViewType(position)==VIEWTYPE_NOTE_HOLDER) {

            Note note;
            NotebookViewHolderUtils.NoteHolder noteHolder = (NotebookViewHolderUtils.NoteHolder) holder.holder;

            int positionInCursor = position-2;

            String noteUUID = cursor.getNoteInfo(positionInCursor).noteUUID;

            if (noteUUID.equals(notebook.editor.getActiveNoteUUID()))//if (currently editing note)
            {
                noteHolder.setMode(MODE_EDIT, false);
                noteHolder.bind(notebook.editor.getActiveNote(), MODE_EDIT);

            } else {
                if(cache.containsKey(noteUUID))note=cache.get(noteUUID);
                else {
                    note = NoteFactory.fromFieldDataStream(context, cursor.getFieldDataStream(positionInCursor), false, notebook, cursor.getNoteInfo(positionInCursor));
                    cache.putIfAbsent(noteUUID,note);
                }
                noteHolder.bind(note, MODE_VIEW);
                noteHolder.setMode(MODE_VIEW, false);
            }
        }
    }

    @Override
    public void onViewDetachedFromWindow(NotebookViewHolderUtils.NotebookViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if(holder.holder instanceof NotebookViewHolderUtils.NoteHolder &&
                ((NotebookViewHolderUtils.NoteHolder)holder.holder).getNote()== XSelection.getSelectedNote())
        {
            XSelection.clearSelections();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0)return VIEWTYPE_NEWNOTEBAR;
        if(position==1)return VIEWTYPE_HEIGHT_BALANCER;
        if(position==getItemCount()-1)return  VIEWTYPE_HEADER;
        return VIEWTYPE_NOTE_HOLDER;
    }

    @Override
    public int getItemCount() {
        return cursor.getCursor().getCount()+3;
    }
}
