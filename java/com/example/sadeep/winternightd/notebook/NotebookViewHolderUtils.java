package com.example.sadeep.winternightd.notebook;

import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.example.sadeep.winternightd.activities.NotebookActivity;
import com.example.sadeep.winternightd.bottombar.BottomBar;
import com.example.sadeep.winternightd.misc.Globals;
import com.example.sadeep.winternightd.misc.Utils;
import com.example.sadeep.winternightd.note.Note;
import com.example.sadeep.winternightd.temp.d;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.example.sadeep.winternightd.notebook.NoteHolderModes.MODE_EDIT;
import static com.example.sadeep.winternightd.notebook.NoteHolderModes.MODE_VIEW;

/**
 * Created by Sadeep on 6/17/2017.
 */

public final class NotebookViewHolderUtils {
    private NotebookViewHolderUtils(){}//static class

    public static final int VIEWTYPE_HEADER = 0;
    public static final int VIEWTYPE_NOTE_HOLDER = 1;
    public static final int VIEWTYPE_HEIGHT_BALANCER=2;
    public static final int VIEWTYPE_NEWNOTEBAR = 3;

    static class NotebookViewHolder extends RecyclerView.ViewHolder{
        public ViewGroup holder;
        public NotebookViewHolder(ViewGroup holder) {
            super(holder);
            this.holder = holder;
        }
    }


    public static class NoteHolder extends NotebookItem{

        private Notebook notebook;

        int mode = -1;//-1 means not assigned a mode yet

        public boolean noteEditable;



        public NoteHolder(Context context,Notebook notebook) {
            super(context);
            this.notebook = notebook;

            Notebook.LayoutParams params = new RecyclerView.LayoutParams(MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(Globals.dp2px*6,Globals.dp2px*5,Globals.dp2px*6,Globals.dp2px*5);
            setLayoutParams(params);

            notebook.noteHolderController.addNoteHolder(this);
        }


        public void bind(Note note,int mode){
            getNoteSpace().removeAllViews();
            setMode(mode,false);
            note.setEditable(noteEditable);
            if(note.getParent()!=null)((ViewGroup)note.getParent()).removeView(note);
            getNoteSpace().addView(note);
            onBind();
        }

        private void onBind() {
            if(mode==MODE_VIEW) NoteHolderModes.ModeView.onBind(this);
            if(mode==MODE_EDIT) NoteHolderModes.ModeEdit.onBind(this);
        }

        public void setMode(int mode,final boolean animate){
            if(mode==MODE_VIEW) NoteHolderModes.ModeView.setAsNoteHolderMode(this,animate);
            if(mode==MODE_EDIT) NoteHolderModes.ModeEdit.setAsNoteHolderMode(this,animate);
        }

        public int getMode() {
            return mode;
        }


        public Note getNote() {
            return getNoteSpace().getChildCount()!=0 ?   (Note) getNoteSpace().getChildAt(0) : null;
        }

        public Notebook getNotebook() {
            return notebook;
        }
    }

    static class Header extends LinearLayout {

        public Header(Context context) {
            super(context);
            setPadding(0, 0, 0, 0);
            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(MATCH_PARENT,Globals.dp2px * 6);
            params.setMargins(0, 0, 0, 0);
            setLayoutParams(params);
            post(new Runnable() {
                @Override
                public void run() {/*
                    RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(MATCH_PARENT, ((NotebookActivity)getContext()).
                            getSupportActionBar().getHeight()+Globals.dp2px * 6);
                    params.setMargins(0, 0, 0, 0);
                    setLayoutParams(params);*/

                }
            });
        }
    }

    public static class HeightBalancer extends LinearLayout{

        private final Notebook notebook;
        public static HeightBalancer balancer;

        public HeightBalancer(Context context, final Notebook notebook) {
            super(context);
            this.notebook = notebook;

            balancer=this;

            //RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(MATCH_PARENT,34);
            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(MATCH_PARENT,WRAP_CONTENT);
            setLayoutParams(params);
            //setLayoutParams(params);

            new CountDownTimer(1000000000,10){

                @Override
                public void onTick(long millisUntilFinished) {
                    balance();
                }

                @Override
                public void onFinish() {

                }
            }.start();

        }

        public void balance(){
            if(true)return;
            if(HeightBalancer.this.getParent()==null)return;
            if(notebook.layoutManager.getItemCount()==notebook.layoutManager.getChildCount())
            {
                int height=((View)notebook.getParent()).getHeight();
                d.p("fkuuuuuuuuuuuuuu",height);
                for(int i=0;i<notebook.getChildCount();i++){
                    if(notebook.getChildAt(i)==HeightBalancer.this)continue;
                    height-=notebook.getChildAt(i).getHeight();
                    height-=((RecyclerView.LayoutParams)notebook.getChildAt(i).getLayoutParams()).topMargin;
                    height-=((RecyclerView.LayoutParams)notebook.getChildAt(i).getLayoutParams()).bottomMargin;
                    height-=notebook.getPaddingTop();
                    height-=notebook.getPaddingBottom();


                }

                HeightBalancer.this.getLayoutParams().height=height;
                HeightBalancer.this.requestLayout();
            }
            else{
                HeightBalancer.this.getLayoutParams().height=0;
                HeightBalancer.this.requestLayout();
            }
        }
    }

    static class NewNoteBarHolder extends LinearLayout {
        private Notebook notebook;
        public NewNoteBarHolder(Context context, Notebook notebook) {
            super(context);
            this.notebook = notebook;
        }

        public void bind(){
            if(notebook.notebookActivity.newNoteBottomBar.getParent()!=null)
                ((ViewGroup)notebook.notebookActivity.newNoteBottomBar.getParent()).removeView(notebook.notebookActivity.newNoteBottomBar);
            addView(notebook.notebookActivity.newNoteBottomBar);
        }
    }
}


