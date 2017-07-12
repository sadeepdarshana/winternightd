package com.example.sadeep.winternightd.notebook;

/**
 * Created by Sadeep on 7/13/2017.
 */

public class NotebookInfo {
    public String notebookUUID;
    public String title;
    public long createdTime;

    public NotebookInfo(String notebookUUID, String title, long createdTime) {
        this.createdTime = createdTime;
        this.notebookUUID = notebookUUID;
        this.title = title;
    }

}
