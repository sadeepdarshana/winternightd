package com.example.sadeep.winternightd.notebook;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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

class NotebookAdapter extends RecyclerView.Adapter <XViewHolder> {
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
    public XViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new XViewHolder(XViewHolder.generateHoldingParent(context,viewType));

    }

    @Override
    public void onBindViewHolder(XViewHolder holder, int position) {

        if(getItemViewType(position)==XViewHolder.VIEWTYPE_LINEARLAYOUT_FOOTER){//footer that changes height to be always equal to bottombar_combined's height
            if(holder.holdingParent.getChildCount()==0) {
                final View v = new View(context);
                v.setPadding(0,0,0,0);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, 2*Globals.dp2px);
                params.setMargins(0,0,0,0);
                v.setLayoutParams(params);
                holder.holdingParent.addView(v);

                notebook._BottomBar.getBottomBar().addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(View xv, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                        {
                            v.getLayoutParams().height = bottom;
                            v.requestLayout();
                        }
                    }
                });
            }

        }
        else if(getItemViewType(position)==XViewHolder.VIEWTYPE_CARDVIEW) //general note
        {
            ((ViewGroup)holder.holdingParent.getChildAt(0)).removeAllViews();

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
                ((ViewGroup)holder.holdingParent.getChildAt(0)).addView(note);
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
    public void onViewDetachedFromWindow(XViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if(holder.holdingParent!=null && holder.holdingParent.getChildCount()!=0 && holder.holdingParent.getChildAt(0)== XSelection.getSelectedNote())XSelection.clearSelections();
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0)return XViewHolder.VIEWTYPE_LINEARLAYOUT_FOOTER;
        if(position==getItemCount()-1)return XViewHolder.VIEWTYPE_LINEARLAYOUT_HEADER;
        return XViewHolder.VIEWTYPE_CARDVIEW;
    }

    @Override
    public int getItemCount() {
        if(!usesCursor)return noteStreams.size()+2;
        else return cursor.getCount()+2;
    }
}
