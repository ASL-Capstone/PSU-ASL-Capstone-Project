package com.psu.capstonew17.backend.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.psu.capstonew17.backend.api.*;
import com.psu.capstonew17.backend.db.AslDbHelper;
import com.psu.capstonew17.backend.db.AslDbContract.*;


public class ExternalCardManager implements CardManager{
    public static ExternalCardManager INSTANCE = new ExternalCardManager();

    private AslDbHelper dbHelper;

    public static CardManager getInstance(Context context){
        INSTANCE.dbHelper = new AslDbHelper(context);
        return INSTANCE;
    }

    public AslDbHelper getDbHelper(){
        return dbHelper;
    }

    public Card getCard(int id){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + CardEntry.TABLE_NAME +
                " WHERE " + CardEntry.COLUMN_ID + "=" + id;
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            int videoId = cursor.getInt(cursor.getColumnIndex(CardEntry.COLUMN_VIDEO));
            String answer = cursor.getString(cursor.getColumnIndex(CardEntry.COLUMN_ANSWER));
            return new ExternalCard(id, ExternalVideoManager.INSTANCE.getVideo(videoId), answer);
        }
        return null;
    }

    @Override
    public Card buildCard(Video video, String answer) throws ObjectAlreadyExistsException {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CardEntry.COLUMN_VIDEO, ((ExternalVideo) video).getVideoId());
        values.put(CardEntry.COLUMN_ANSWER, answer);
        try {
            int id = (int) db.insertOrThrow(CardEntry.TABLE_NAME, null, values);
            return new ExternalCard(id, video, answer);
        } catch (SQLException e) {
            throw new ObjectAlreadyExistsException(e);
        }
    }

}
