package com.example.sadeep.winternightd.notebook;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sadeep.winternightd.dumping.FieldDataStream;
import com.example.sadeep.winternightd.localstorage.NotebookCursorReader;
import com.example.sadeep.winternightd.misc.Globals;
import com.example.sadeep.winternightd.note.Note;
import com.example.sadeep.winternightd.note.NoteFactory;
import com.example.sadeep.winternightd.selection.XSelection;

import java.util.ArrayList;

import static com.example.sadeep.winternightd.notebook.NotebookViewHolderUtils.VIEWTYPE_FOOTER;
import static com.example.sadeep.winternightd.notebook.NotebookViewHolderUtils.VIEWTYPE_HEADER;
import static com.example.sadeep.winternightd.notebook.NotebookViewHolderUtils.VIEWTYPE_NOTE_HOLDER;

/**
 * Created by Sadeep on 6/17/2017.
 */

class NotebookAdapter extends RecyclerView.Adapter <NotebookViewHolderUtils.NotebookViewHolder> {
    private ArrayList<FieldDataStream> noteStreams;
    private NotebookCursorReader cursor;

    private Context context;
    private Notebook notebook;

    private boolean usesCursor;

    public NotebookAdapter(Context context, ArrayList<FieldDataStream> noteStreams, Notebook notebook) {
        this.noteStreams = noteStreams;
        this.context = context;
        this.notebook = notebook;

        usesCursor = false;
    }

    public NotebookAdapter(Context context, NotebookCursorReader cursor, Notebook notebook) {
        this.cursor = cursor;
        this.context = context;
        this.notebook = notebook;

        usesCursor = true;
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

        try {
            Note note;
            if(!cursor.getNoteInfo(position-1).noteUUID.equals(notebook.editor.getActiveNoteUUID()))
            {
                note = NoteFactory.fromFieldDataStream(context, cursor.getFieldDataStream(position-1), false, notebook,cursor.getNoteInfo(position-1));
                ((NotebookViewHolderUtils.NoteHolder)holder.holder).bind(note);
                ((NotebookViewHolderUtils.NoteHolder)holder.holder).setMode(NoteHolderModes.MODE_VIEW,false);
            }
            else {
                ((NotebookViewHolderUtils.NoteHolder) holder.holder).setMode(NoteHolderModes.MODE_EDIT, false);
                ((NotebookViewHolderUtils.NoteHolder) holder.holder).bind(notebook.editor.getCacheNote());
            }
        }
        catch (Exception e) {
            TextView err = new TextView(context);
            err.setTextColor(Color.RED);
            err.setText("Error processing note");
            err.setTextSize(TypedValue.COMPLEX_UNIT_FRACTION, Globals.defaultFontSize * 1);
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
        if(!usesCursor)return noteStreams.size()+2;
        else return cursor.getCursor().getCount()+2;
    }
}
