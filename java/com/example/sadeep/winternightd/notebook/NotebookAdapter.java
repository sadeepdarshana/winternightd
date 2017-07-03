package com.example.sadeep.winternightd.notebook;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.widget.CardView;
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

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Sadeep on 6/17/2017.
 */

class NotebookAdapter extends RecyclerView.Adapter <CardViewHolder> {
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
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==1) {
            CardView card = new CardView(context);

            card.setCardBackgroundColor(Color.WHITE);
            card.setCardElevation(Globals.dp2px * 2f);
            card.setRadius(Globals.dp2px * 2);
            card.setMinimumHeight(Globals.dp2px * 100);
            //card.setUseCompatPadding(true);
            card.setContentPadding(Globals.dp2px * 7, Globals.dp2px * 7, Globals.dp2px * 7, Globals.dp2px * 7);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
            params.setMargins(Globals.dp2px * 6, Globals.dp2px * 6, Globals.dp2px * 6, Globals.dp2px * 6);
            card.setLayoutParams(params);

            return new CardViewHolder(card);
        }else if(viewType==0){//header (position=last-1)
            LinearLayout footer = new LinearLayout(context);
            footer.setPadding(0,0,0,0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
            params.setMargins(0,0,0,0);
            footer.setLayoutParams(params);
            return new CardViewHolder(footer);
        }
        else if(viewType==2){ //footer (position=0)
            LinearLayout header = new LinearLayout(context);
            header.setPadding(0,0,0,0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, Globals.dp2px*6);
            params.setMargins(0,0,0,0);
            header.setLayoutParams(params);
            return new CardViewHolder(header);

        }

        return null;

    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {

        if(position==0){//footer that changes height to be always equal to bottombar's height
            if(holder.ll.getChildCount()==0) {
                final View v = new View(context);
                v.setPadding(0,0,0,0);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, 5);
                params.setMargins(0,0,0,0);
                v.setLayoutParams(params);
                holder.ll.addView(v);

                notebook.bottomBar.getBottombar().addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(View xv, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

                        {
                            v.getLayoutParams().height = bottom;
                            v.requestLayout();
                        }
                    }
                });
            }

            return;
        }
        else if(position!=getItemCount()-1) //general note
        {
            holder.card.removeAllViews();

            //holder.card.setCardBackgroundColor(Color.HSVToColor(new float[]{new Random().nextFloat()*360,.001f,1}));
            try {
                Note note;
                if (!usesCursor)
                    note = NoteFactory.fromFieldDataStream(context, noteStreams.get(position), false, notebook);
                else {
                    cursor.moveToPosition(position - 1);
                    RawFieldDataStream rawStream = new RawFieldDataStream(cursor.getString(1), cursor.getString(2), cursor.getBlob(3), cursor.getString(4), cursor.getBlob(5));
                    FieldDataStream stream = new FieldDataStream(rawStream);
                    note = NoteFactory.fromFieldDataStream(context, stream, false, notebook);
                }
                holder.card.addView(note);
            } catch (Exception e) {
                TextView err = new TextView(context);
                err.setTextColor(Color.RED);
                err.setText("Error processing note");
                err.setTextSize(TypedValue.COMPLEX_UNIT_FRACTION, Globals.defaultFontSize * 1);
                holder.card.addView(err);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0)return 0;
        if(position==getItemCount()-1)return 2;
        return 1;
    }

    @Override
    public int getItemCount() {
        if(!usesCursor)return noteStreams.size()+2;
        else return cursor.getCount()+2;
    }
}
