package com.example.sadeep.winternightd.localstorage;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.sadeep.winternightd.dumping.FieldDataStream;
import com.example.sadeep.winternightd.dumping.RawFieldDataStream;
import com.example.sadeep.winternightd.note.NoteInfo;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Sadeep on 6/16/2017.
 */
public class NotebookDataHandler {

    String notebookGuid;

    public NotebookDataHandler(String notebookUuid){
        this.notebookGuid = notebookUuid;
    }

    public Cursor getCursor(){
        Cursor cursor = DataConnection.readableDatabase().rawQuery("SELECT * FROM "+notebookGuid+" ORDER BY cvtime DESC",null);
        return cursor;
    }

    public void addNote(RawFieldDataStream stream){

        NoteInfo info = NoteInfo.newNoteInfoForCurrentTime();
        ContentValues values = ValuesWriter.generateContentValues(stream,info);

        DataConnection.writableDatabase().insert("qwerty",null,values);
    }

    public static void createNotebook(String notebookUuid){
        String createSQL = "CREATE TABLE IF NOT EXISTS `" + notebookUuid +"` ( `noteId` TEXT, `strings0` TEXT, `strings1` TEXT, `ints0` BLOB, `ints1` TEXT, `fieldTypes` BLOB, `cvtime` INTEGER, `created` INTEGER, `cvId` TEXT, PRIMARY KEY(`noteId`) )";
        DataConnection.execSQL(createSQL);
    }
}
