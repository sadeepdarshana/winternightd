package com.example.sadeep.winternightd.notebook;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.sadeep.winternightd.notebookactivity.BottomBar;
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

    public static boolean scrollEnabled = true;

    public Notebook(Context context, String notebookGuid, BottomBar bottomBar) {
        super(context);
        this.context = context;
        this.notebookGuid = notebookGuid;
        this.bottomBar = bottomBar;

        dataHandler = new NotebookDataHandler(notebookGuid);
        setAdapter(new NotebookAdapter(context,dataHandler.getCursor(),this));

        layoutManager = new LinearLayoutManager(context) {
            @Override
            public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
                if (Notebook.scrollEnabled)return super.scrollVerticallyBy(dy,recycler,state);
                return 0;
            }
        };

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
        }, 500);
    }

    public interface ScrollListener{void onScrolled(int dx,int dy);}
}
