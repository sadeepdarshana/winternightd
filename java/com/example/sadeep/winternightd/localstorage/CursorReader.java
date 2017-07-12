package com.example.sadeep.winternightd.localstorage;

import android.database.Cursor;

import com.example.sadeep.winternightd.dumping.FieldDataStream;
import com.example.sadeep.winternightd.dumping.RawFieldDataStream;

/**
 * Created by Sadeep on 7/12/2017.
 */

public class CursorReader {
    private Cursor cursor;

    public CursorReader(Cursor cursor) {
        this.cursor = cursor;
    }

    public FieldDataStream getFieldDataStream(int position){
        cursor.moveToPosition(position);
        RawFieldDataStream rawStream = new RawFieldDataStream(cursor.getString(1), cursor.getString(2), cursor.getBlob(3), cursor.getString(4), cursor.getBlob(5));
        return new FieldDataStream(rawStream);
    }
}
