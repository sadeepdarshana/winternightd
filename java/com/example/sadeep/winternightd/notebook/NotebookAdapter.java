package com.example.sadeep.winternightd.notebook;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.sadeep.winternightd.dumping.FieldDataStream;
import com.example.sadeep.winternightd.dumping.RawFieldDataStream;
import com.example.sadeep.winternightd.localstorage.CursorReader;
import com.example.sadeep.winternightd.misc.Globals;
import com.example.sadeep.winternightd.note.Note;
import com.example.sadeep.winternightd.note.NoteFactory;
import com.example.sadeep.winternightd.note.NoteInfo;
import com.example.sadeep.winternightd.selection.XSelection;

import java.util.ArrayList;

import static com.example.sadeep.winternightd.notebook.XViewHolderUtils.VIEWTYPE_FOOTER;
import static com.example.sadeep.winternightd.notebook.XViewHolderUtils.VIEWTYPE_HEADER;
import static com.example.sadeep.winternightd.notebook.XViewHolderUtils.VIEWTYPE_NOTE_HOLDER;

/**
 * Created by Sadeep on 6/17/2017.
 */

class NotebookAdapter extends RecyclerView.Adapter <XViewHolderUtils.XViewHolder> {
    private ArrayList<FieldDataStream> noteStreams;
    private CursorReader cursor;

    private Context context;
    private Notebook notebook;

    private boolean usesCursor;

    public NotebookAdapter(Context context, ArrayList<FieldDataStream> noteStreams, Notebook notebook) {
        this.noteStreams = noteStreams;
        this.context = context;
        this.notebook = notebook;

        usesCursor = false;
    }

    public NotebookAdapter(Context context, CursorReader cursor, Notebook notebook) {
        this.cursor = cursor;
        this.context = context;
        this.notebook = notebook;

        usesCursor = true;
    }


    @Override
    public XViewHolderUtils.XViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == VIEWTYPE_HEADER)return new XViewHolderUtils.XViewHolder(new XViewHolderUtils.Header(context));
        if(viewType == VIEWTYPE_NOTE_HOLDER)return  new XViewHolderUtils.XViewHolder(new XViewHolderUtils.NoteHolder(context));
        if(viewType == VIEWTYPE_FOOTER)return  new XViewHolderUtils.XViewHolder(new XViewHolderUtils.Footer(context,notebook));

        return null;
    }

    @Override
    public void onBindViewHolder(XViewHolderUtils.XViewHolder holder, int position) {

        if(getItemViewType(position)!=VIEWTYPE_NOTE_HOLDER)return;

        try {
            Note note;
            if (!usesCursor)
                note = NoteFactory.fromFieldDataStream(context, noteStreams.get(position), false, notebook,null);
            else {

                note = NoteFactory.fromFieldDataStream(context, cursor.getFieldDataStream(position-1), false, notebook,cursor.getNoteInfo(position-1));
            }
            ((XViewHolderUtils.NoteHolder)holder.holder).bindNote(note);
            ((XViewHolderUtils.NoteHolder)holder.holder).setMode(XViewHolderUtils.NoteHolder.MODE_VIEW);
        }
        catch (Exception e) {
            TextView err = new TextView(context);
            err.setTextColor(Color.RED);
            err.setText("Error processing note");
            err.setTextSize(TypedValue.COMPLEX_UNIT_FRACTION, Globals.defaultFontSize * 1);
        }
    }

    @Override
    public void onViewDetachedFromWindow(XViewHolderUtils.XViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if(holder.holder instanceof XViewHolderUtils.NoteHolder &&
                ((XViewHolderUtils.NoteHolder)holder.holder).getNote()== XSelection.getSelectedNote())
        {
            XSelection.clearSelections();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0)return VIEWTYPE_FOOTER;
        if(position==getItemCount()-1)return VIEWTYPE_HEADER;
        return VIEWTYPE_NOTE_HOLDER;
    }

    @Override
    public int getItemCount() {
        if(!usesCursor)return noteStreams.size()+2;
        else return cursor.getCursor().getCount()+2;
    }
}
