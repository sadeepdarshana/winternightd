package com.example.sadeep.winternightd.localstorage;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.sadeep.winternightd.dumping.RawFieldDataStream;
import com.example.sadeep.winternightd.note.NoteInfo;

/**
 * Created by Sadeep on 6/16/2017.
 */
public class NotebookDataHandler {

    private String notebookUUID;

    public NotebookDataHandler(String notebookUUID){
        this.notebookUUID = notebookUUID;
    }

    public Cursor getCursor(){
        return DataConnection.readableDatabase().rawQuery("SELECT * FROM "+ notebookUUID +" ORDER BY cvtime DESC",null);
    }

    public void addNote(RawFieldDataStream stream){
        NoteInfo info = NoteInfo.newNoteInfoForCurrentTime();
        ContentValues values = NotebookValuesWriter.generateContentValues(stream,info);

        DataConnection.writableDatabase().insertWithOnConflict(notebookUUID,null,values, SQLiteDatabase.CONFLICT_IGNORE);
    }

}
