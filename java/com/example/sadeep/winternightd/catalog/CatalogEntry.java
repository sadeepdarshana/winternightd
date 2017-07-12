package com.example.sadeep.winternightd.catalog;

/**
 * Created by Sadeep on 7/13/2017.
 */

public class CatalogEntry {
    public String notebookUUID;
    public String title;
    public long createdTime;

    public CatalogEntry(String notebookUUID, String title,long createdTime) {
        this.createdTime = createdTime;
        this.notebookUUID = notebookUUID;
        this.title = title;
    }

}
