package com.psu.capstonew17.backend.data;


import android.util.Pair;
import com.psu.capstonew17.backend.api.*;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ExternalQuestion implements Question {
    private Card card;
    private Type type;
    private List<String> options;
    private static int OPTION_SIZE = 4;

    public ExternalQuestion(Card card, Type type){
        this.card = card;
        this.type = type;
        this.options = new ArrayList<String>();
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
        return new Pair<Boolean, String>(correct, this.card.getAnswer());
    }
}
