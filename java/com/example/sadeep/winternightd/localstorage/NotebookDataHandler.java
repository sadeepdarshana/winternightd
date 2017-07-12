package com.example.sadeep.winternightd.localstorage;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.sadeep.winternightd.dumping.FieldDataStream;
import com.example.sadeep.winternightd.dumping.RawFieldDataStream;

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


    public ArrayList<FieldDataStream> fetchNotes(){
        Cursor cursor = DataConnection.readableDatabase().rawQuery("SELECT * FROM "+notebookGuid,null);

        ArrayList<FieldDataStream> list = new ArrayList<>();
        while (cursor.moveToNext()){
            RawFieldDataStream rawStream = new RawFieldDataStream(cursor.getString(1),cursor.getString(2),cursor.getBlob(3),cursor.getString(4),cursor.getBlob(5));
            list.add(new FieldDataStream(rawStream));
        }

        return list;
    }
    public Cursor getCursor(){
        Cursor cursor = DataConnection.readableDatabase().rawQuery("SELECT * FROM "+notebookGuid+" ORDER BY time DESC",null);
        return cursor;
    }

    public void addNote(RawFieldDataStream stream){
        ContentValues values = new ContentValues();

        values.put("noteId", UUID.randomUUID().toString());
        values.put("time", System.currentTimeMillis());

        values.put("strings0",stream.strings[0]);
        values.put("strings1",stream.strings[1]);
        values.put("ints0",stream.ints0);
        values.put("ints1",stream.ints1);
        values.put("fieldTypes",stream.fieldTypes);

        DataConnection.writableDatabase().insert("qwerty",null,values);
    }

    public static void createNotebook(String notebookUuid){
        String createSQL = "CREATE TABLE `" + notebookUuid +"` ( `noteId` TEXT, `strings0` TEXT, `strings1` TEXT, `ints0` BLOB, `ints1` TEXT, `fieldTypes` BLOB, `cvtime` INTEGER, `created` INTEGER, `cvId` TEXT, PRIMARY KEY(`noteId`) )";
        DataConnection.execSQL(createSQL);
    }
}
