package com.example.sadeep.winternightd.notebook;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.sadeep.winternightd.localstorage.CursorReader;
import com.example.sadeep.winternightd.activities.notebookactivity.NotebookActivity;
import com.example.sadeep.winternightd.activities.notebookactivity.bottombar.BottomBarCombined;
import com.example.sadeep.winternightd.localstorage.NotebookDataHandler;

/**
 * Created by Sadeep on 10/26/2016.
 */
public class Notebook extends RecyclerView {
    private NotebookActivity notebookActivity;
    private String notebookGuid;

    private NotebookDataHandler dataHandler;
    private LinearLayoutManager layoutManager;

    public BottomBarCombined _BottomBar;

    public static boolean scrollEnabled = true;

    public Notebook(NotebookActivity notebookActivity, String notebookGuid, BottomBarCombined _BottomBar) {
        super(notebookActivity);
        this.notebookActivity = notebookActivity;
        this.notebookGuid = notebookGuid;
        this._BottomBar = _BottomBar;

        dataHandler = new NotebookDataHandler(notebookGuid);
        setAdapter(new NotebookAdapter(notebookActivity,new CursorReader(dataHandler.getCursor()),this));

        layoutManager = new LinearLayoutManager(notebookActivity) {
            @Override
            public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
                if (Notebook.scrollEnabled)return super.scrollVerticallyBy(dy,recycler,state);
                return 0;
            }
        };

        layoutManager.setReverseLayout(true);
        setLayoutManager(layoutManager);
        layoutManager.scrollToPositionWithOffset(0,500);
    }

    public NotebookDataHandler getNotebookDataHandler() {
        return dataHandler;
    }

    public void refresh() {
        dataHandler = new NotebookDataHandler(notebookGuid);
        setAdapter(new NotebookAdapter(notebookActivity,new CursorReader(dataHandler.getCursor()),this));
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
        notebookActivity.onNotebookScrolled(dx,dy);
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
        },100);
    }
}
