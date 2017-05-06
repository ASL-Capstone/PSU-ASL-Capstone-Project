package com.psu.capstonew17.backend.data;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Pair;
import com.psu.capstonew17.backend.api.*;
import com.psu.capstonew17.backend.db.AslDbContract.*;
import com.psu.capstonew17.backend.db.AslDbHelper;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Date;

class ExternalQuestion implements Question {
    private Card card;
    private int deckId;
    private Type type;
    private int questionId;
    private List<String> options;
    private static int OPTION_SIZE = 4;

    private AslDbHelper dbHelper;

    public ExternalQuestion(Card card, Type type, int deckId){
        this.card = card;
        this.type = type;
        this.deckId = deckId;
        this.options = new ArrayList<String>();
    }

    public void setQuestionId(int id){
        this.questionId = id;
    }

    public int getCardId(){
        return ((ExternalCard) this.card).getId();
    }

    public int getDeckId(){
        return this.deckId;
    }

    @Override
    public Video getVideo() {
        return this.card.getVideo();
    }

    @Override
    public Type getType() {
        return this.type;
    }

    @Override
    public List<String> getOptions() {
        if(type.equals(Type.MULTIPLE_CHOICE)){
            if(!this.options.isEmpty()){
                return this.options;
            }
            List<Deck> users = this.card.getUsers();
            List<String> answers = new ArrayList<String>();
            Random random = new Random();
            for(Deck deck : users){
                List<Card> cards = deck.getCards();
                for(Card card : cards){
                    answers.add(card.getAnswer());
                }
            }
            if(answers.size() < OPTION_SIZE){
                this.options.addAll(answers);
                return this.options;
            }
            this.options.add(this.card.getAnswer());
            while(this.options.size() < OPTION_SIZE){
                String option = answers.get(random.nextInt(answers.size()));
                if(!this.options.contains(option)){
                    this.options.add(option);
                }
            }
            Collections.shuffle(this.options);
            return this.options;
        }
        return null;
    }

    @Override
    public Pair<Boolean, String> answer(String answer) {
        Boolean correct = false;
        if(answer.equals(this.card.getAnswer())){
            correct = true;
        }
        dbHelper = ExternalTestManager.INSTANCE.getDbHelper();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AnswerEntry.COLUMN_ANSWERED_AT, new Date().getTime());
        values.put(AnswerEntry.COLUMN_CORRECT, correct);
        db.update(
                AnswerEntry.TABLE_NAME, values,
                AnswerEntry.COLUMN_ID + "=" + this.questionId, null
        );
        return new Pair<Boolean, String>(correct, this.card.getAnswer());
    }
}
