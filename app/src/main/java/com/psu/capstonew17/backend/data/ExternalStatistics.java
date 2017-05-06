package com.psu.capstonew17.backend.data;

import com.psu.capstonew17.backend.api.Card;
import com.psu.capstonew17.backend.api.Statistics;

import java.util.List;


class ExternalStatistics implements Statistics{
    private List<Card> correctCards;
    private List<Card> incorrectCards;
    private Long avgTime;

    public ExternalStatistics(List<Card> correct, List<Card> incorrect, Long avgTime){
        this.correctCards = correct;
        this.incorrectCards = incorrect;
        this.avgTime = avgTime;
    }

    @Override
    public List<Card> getCorrectCards() {
        return null;
    }

    @Override
    public List<Card> getIncorrectCards() {
        return null;
    }

    @Override
    public int getCorrect() {
        return correctCards.size();
    }

    @Override
    public int getIncorrect() {
        return incorrectCards.size();
    }

    @Override
    public Long getAverageAnswerTime() {
        return avgTime;
    }
}
