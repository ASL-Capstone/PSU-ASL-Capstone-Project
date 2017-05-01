//MIT License Copyright 2017 PSU ASL Capstone Team

package com.psu.capstonew17.backend.db;


import android.provider.BaseColumns;

public class AslDbContract {
    private AslDbContract(){}

    public static class CardEntry implements BaseColumns{
        public static final String TABLE_NAME = "card";
        public static final String COLUMN_ID = "card_id";
        public static final String COLUMN_VIDEO = "video_id";
        public static final String COLUMN_ANSWER = "answer";
    }

    public static class VideoEntry implements BaseColumns{
        public static final String TABLE_NAME = "video";
        public static final String COLUMN_ID = "video_id";
        public static final String COLUMN_PATH = "video_path";
        public static final String COLUMN_SHA = "video_sha";
    }

    public static class AnswerEntry implements BaseColumns{
        public static final String TABLE_NAME = "answers";
        public static final String COLUMN_ID = "answer_id";
        public static final String COLUMN_ASKED_AT = "asked_at";
        public static final String COLUMN_ANSWERED_AT = "answered_at";
        public static final String COLUMN_CARD = "card_id";
        public static final String COLUMN_DECK = "deck_id";
        public static final String COLUMN_TYPE = "question_type";
        public static final String COLUMN_CORRECT = "correct";
    }

    public static class DeckEntry implements BaseColumns{
        public static final String TABLE_NAME = "deck";
        public static final String COLUMN_ID = "deck_id";
        public static final String COLUMN_NAME = "deck_name";
    }

    public static class RelationEntry implements BaseColumns{
        public static final String TABLE_NAME = "deck_card_relation";
        public static final String COLUMN_DECK = "deck_id";
        public static final String COLUMN_CARD = "card_id";
    }
}
