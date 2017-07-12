package com.example.sadeep.winternightd.catalog;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.sadeep.winternightd.localstorage.DataConnection;
import com.example.sadeep.winternightd.notebook.NotebookInfo;

/**
 * Created by Sadeep on 7/13/2017.
 */

public class CatalogDataHandler {


    public Cursor getCursor(){
        Cursor cursor = DataConnection.readableDatabase().rawQuery("SELECT * FROM `catalog` ORDER BY created DESC",null);
        return cursor;
    }

    public void addNotebook(NotebookInfo info){

        ContentValues values = new ContentValues();
        values.put("created",info.createdTime);
        values.put("title",info.title);
        values.put("notebookId",info.notebookUUID);

        DataConnection.writableDatabase().insert("catalog",null,values);
    }

    public static void createCatalog(){
        String createSQL = "CREATE TABLE IF NOT EXISTS `catalog` (`created` INTEGER,`title` TEXT,`notebookId` TEXT);";
        DataConnection.execSQL(createSQL);
    }
}
