package com.example.sadeep.winternightd.catalog;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.sadeep.winternightd.dumping.RawFieldDataStream;
import com.example.sadeep.winternightd.localstorage.DataConnection;
import com.example.sadeep.winternightd.note.NoteInfo;

/**
 * Created by Sadeep on 7/13/2017.
 */

public class CatalogDataHandler {


    public Cursor getCursor(){
        Cursor cursor = DataConnection.readableDatabase().rawQuery("SELECT * FROM `catalog` ORDER BY created DESC",null);
        return cursor;
    }

    public void addNotebook(CatalogEntry entry){

        ContentValues values = new ContentValues();
        values.put("created",entry.createdTime);
        values.put("title",entry.title);
        values.put("title",entry.title);

        DataConnection.writableDatabase().insert("catalog",null,values);
    }

    public static void createCatalog(){
        String createSQL = "CREATE TABLE IF NOT EXISTS `catalog` (`created` INTEGER,`title` TEXT,`notebookId` TEXT);";
        DataConnection.execSQL(createSQL);
    }
}
