//MIT License Copyright 2017 PSU ASL Capstone Team

package com.psu.capstonew17.backend.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.psu.capstonew17.backend.api.*;
import com.psu.capstonew17.backend.db.AslDbHelper;
import com.psu.capstonew17.backend.db.AslDbContract.*;

import java.util.Arrays;


public class ExternalCardManager implements CardManager{
    static ExternalCardManager INSTANCE = new ExternalCardManager();

    private AslDbHelper dbHelper;

    public static CardManager getInstance(Context context){
        INSTANCE.dbHelper = AslDbHelper.getInstance(context);
        ExternalVideoManager.getInstance(context);
        return INSTANCE;
    }

    AslDbHelper getDbHelper(){
        return dbHelper;
    }

    public Card getCard(int id){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = dbHelper.buildSelectQuery(
                CardEntry.TABLE_NAME,
                Arrays.asList(CardEntry.COLUMN_ID + "=" + id)
        );
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            int videoId = cursor.getInt(cursor.getColumnIndex(CardEntry.COLUMN_VIDEO));
            String answer = cursor.getString(cursor.getColumnIndex(CardEntry.COLUMN_ANSWER));
            cursor.close();
            return new ExternalCard(id, ExternalVideoManager.INSTANCE.getVideo(videoId), answer);
        }
        cursor.close();
        return null;
    }

    @Override
    public Card buildCard(Video video, String answer) throws ObjectAlreadyExistsException {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String query = dbHelper.buildSelectQuery(
                CardEntry.TABLE_NAME,
                Arrays.asList(CardEntry.COLUMN_VIDEO + "=" + video.getVideoId())
        );
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            cursor.close();
            throw new ObjectAlreadyExistsException("A card for this video already exists.");
        }
        cursor.close();
        ContentValues values = new ContentValues();
        values.put(CardEntry.COLUMN_VIDEO, video.getVideoId());
        values.put(CardEntry.COLUMN_ANSWER, answer);
        int id = (int) db.insert(CardEntry.TABLE_NAME, null, values);
        return new ExternalCard(id, video, answer);
    }

}
