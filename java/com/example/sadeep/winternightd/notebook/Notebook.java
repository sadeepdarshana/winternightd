package com.example.sadeep.winternightd.notebook;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.sadeep.winternightd.activities.BottomBar;
import com.example.sadeep.winternightd.localstorage.NotebookDataHandler;

/**
 * Created by Sadeep on 10/26/2016.
 */
public class Notebook extends RecyclerView {
    private Context context;
    private String notebookGuid;

    private NotebookDataHandler dataHandler;
    private LinearLayoutManager layoutManager;

    public BottomBar bottomBar;

    public Notebook(Context context, String notebookGuid, BottomBar bottomBar) {
        super(context);
        this.context = context;
        this.notebookGuid = notebookGuid;
        this.bottomBar = bottomBar;

        dataHandler = new NotebookDataHandler(notebookGuid);
        setAdapter(new NotebookAdapter(context,dataHandler.getCursor(),this));

        layoutManager = new LinearLayoutManager(context);
        //layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        setLayoutManager(layoutManager);
        layoutManager.scrollToPositionWithOffset(0,500);
    }

    public NotebookDataHandler getNotebookDataHandler() {
        return dataHandler;
    }

    public void refresh() {
        dataHandler = new NotebookDataHandler(notebookGuid);
        setAdapter(new NotebookAdapter(context,dataHandler.getCursor(),this));
    }

    public ScrollListener scrollListener=null;
    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
        if(scrollListener!=null)scrollListener.onScrolled(dx,dy);
    }
    public interface ScrollListener{void onScrolled(int dx,int dy);}
}
