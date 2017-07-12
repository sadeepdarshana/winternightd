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
import com.example.sadeep.winternightd.misc.Globals;
import com.example.sadeep.winternightd.note.Note;
import com.example.sadeep.winternightd.note.NoteFactory;
import com.example.sadeep.winternightd.note.NoteInfo;
import com.example.sadeep.winternightd.selection.XSelection;

import java.util.ArrayList;

/**
 * Created by Sadeep on 6/17/2017.
 */

class NotebookAdapter extends RecyclerView.Adapter {
    private ArrayList<FieldDataStream> noteStreams;
    private Cursor cursor;

    private Context context;
    private Notebook notebook;

    private boolean usesCursor;

    public NotebookAdapter(Context context, ArrayList<FieldDataStream> noteStreams, Notebook notebook) {
        this.noteStreams = noteStreams;
        this.context = context;
        this.notebook = notebook;

        usesCursor = false;
    }

    public NotebookAdapter(Context context, Cursor cursor, Notebook notebook) {
        this.cursor = cursor;
        this.context = context;
        this.notebook = notebook;

        usesCursor = true;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new XViewHolderUtils(XViewHolderUtils.generateHoldingParent(context,viewType));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(getItemViewType(position)== XViewHolderUtils.VIEWTYPE_NOTE_HOLDER) //general note
        {

            CardView card = ((CardView)(((FrameLayout)holder.holdingParent).getChildAt(0)));
            card.removeAllViews();

            try {
                Note note;
                if (!usesCursor)
                    note = NoteFactory.fromFieldDataStream(context, noteStreams.get(position), false, notebook,new NoteInfo());
                else {
                    cursor.moveToPosition(position - 1);
                    RawFieldDataStream rawStream = new RawFieldDataStream(cursor.getString(1), cursor.getString(2), cursor.getBlob(3), cursor.getString(4), cursor.getBlob(5));
                    FieldDataStream stream = new FieldDataStream(rawStream);
                    note = NoteFactory.fromFieldDataStream(context, stream, false, notebook,new NoteInfo());
                }
                card.addView(note);
            } catch (Exception e) {
                TextView err = new TextView(context);
                err.setTextColor(Color.RED);
                err.setText("Error processing note");
                err.setTextSize(TypedValue.COMPLEX_UNIT_FRACTION, Globals.defaultFontSize * 1);
                ((ViewGroup)holder.holdingParent.getChildAt(0)).addView(err);
            }
        }
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if(holder.holdingParent!=null && holder.holdingParent.getChildCount()!=0 && holder.holdingParent.getChildAt(0)== XSelection.getSelectedNote())XSelection.clearSelections();
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0)return XViewHolderUtils.VIEWTYPE_FOOTER;
        if(position==getItemCount()-1)return XViewHolderUtils.VIEWTYPE_HEADER;
        return XViewHolderUtils.VIEWTYPE_NOTE_HOLDER;
    }

    @Override
    public int getItemCount() {
        if(!usesCursor)return noteStreams.size()+2;
        else return cursor.getCount()+2;
    }
}
