package com.example.backendtesting;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.example.backendtesting.AslDbContract.*;

public class MainActivity extends AppCompatActivity {

    private AslDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new AslDbHelper(getApplicationContext());
        Log.println(Log.INFO, "db path", dbHelper.getReadableDatabase().getPath());

        // Add some fake data
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CardEntry.COLUMN_SHA, "somesupercoolsha");
        values.put(CardEntry.COLUMN_PATH, "pathtovideo");
        values.put(CardEntry.COLUMN_ANSWER, "someanswer");
        db.insert(CardEntry.TABLE_NAME, null, values);
    }

}
