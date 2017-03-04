package com.example.backendtesting;


import android.provider.BaseColumns;

public class AslDbContract {
    private AslDbContract(){}

    public static class CardEntry implements BaseColumns{
        public static final String TABLE_NAME = "card";
        public static final String COLUMN_SHA = "video_sha";
        public static final String COLUMN_PATH = "video_path";
        public static final String COLUMN_ANSWER = "sign_answer";
    }

    public static class DeckEntry implements BaseColumns{
        public static final String TABLE_NAME = "deck";
        public static final String COLUMN_NAME = "deck_name";
    }

    public static class RelationEntry implements BaseColumns{
        public static final String TABLE_NAME = "deck_rel";
        public static final String COLUMN_DECK_NAME = "deck_name";
        public static final String COLUMN_VIDEO_SHA = "video_sha";
    }

}
