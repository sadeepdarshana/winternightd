package com.example.sadeep.winternightd.notebook;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.example.sadeep.winternightd.localstorage.NotebookCursorReader;
import com.example.sadeep.winternightd.note.Note;
import com.example.sadeep.winternightd.note.NoteFactory;
import com.example.sadeep.winternightd.selection.XSelection;

import static com.example.sadeep.winternightd.notebook.NoteHolderModes.MODE_EDIT;
import static com.example.sadeep.winternightd.notebook.NoteHolderModes.MODE_VIEW;
import static com.example.sadeep.winternightd.notebook.NotebookViewHolderUtils.VIEWTYPE_FOOTER;
import static com.example.sadeep.winternightd.notebook.NotebookViewHolderUtils.VIEWTYPE_HEADER;
import static com.example.sadeep.winternightd.notebook.NotebookViewHolderUtils.VIEWTYPE_NOTE_HOLDER;

/**
 * Created by Sadeep on 6/17/2017.
 */

class NotebookAdapter extends RecyclerView.Adapter <NotebookViewHolderUtils.NotebookViewHolder> {
    private NotebookCursorReader cursor;

    private Context context;
    private Notebook notebook;


    public NotebookAdapter(Context context, NotebookCursorReader cursor, Notebook notebook) {
        this.cursor = cursor;
        this.context = context;
        this.notebook = notebook;
    }


    @Override
    public NotebookViewHolderUtils.NotebookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == VIEWTYPE_HEADER)return new NotebookViewHolderUtils.NotebookViewHolder(new NotebookViewHolderUtils.Header(context));
        if(viewType == VIEWTYPE_NOTE_HOLDER)return  new NotebookViewHolderUtils.NotebookViewHolder(new NotebookViewHolderUtils.NoteHolder(context,notebook));
        if(viewType == VIEWTYPE_FOOTER)return  new NotebookViewHolderUtils.NotebookViewHolder(new NotebookViewHolderUtils.Footer(context,notebook));

        return null;
    }

    @Override
    public void onBindViewHolder(NotebookViewHolderUtils.NotebookViewHolder holder, int position) {

        if(getItemViewType(position)!=VIEWTYPE_NOTE_HOLDER)return;

        Note note;
        NotebookViewHolderUtils.NoteHolder noteHolder = (NotebookViewHolderUtils.NoteHolder) holder.holder;

        if(cursor.getNoteInfo(position-1).noteUUID.equals(notebook.editor.getActiveNoteUUID()))//if (currently editing note)
        {
            noteHolder.setMode(MODE_EDIT, false);
            noteHolder.bind(notebook.editor.getActiveNote(),MODE_EDIT);

        }
        else {
            note = NoteFactory.fromFieldDataStream(context, cursor.getFieldDataStream(position-1), false, notebook,cursor.getNoteInfo(position-1));
            noteHolder.bind(note,MODE_VIEW);
            noteHolder.setMode(MODE_VIEW,false);
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
        if(position==0)return VIEWTYPE_FOOTER;
        if(position==getItemCount()-1)return  VIEWTYPE_HEADER;
        return VIEWTYPE_NOTE_HOLDER;
    }

    @Override
    public int getItemCount() {
        return cursor.getCursor().getCount()+2;
    }
}
