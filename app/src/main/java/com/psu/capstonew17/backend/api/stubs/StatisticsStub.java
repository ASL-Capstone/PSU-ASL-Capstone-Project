package com.psu.capstonew17.backend.api.stubs;
import com.psu.capstonew17.backend.api.Card;
import com.psu.capstonew17.backend.api.Statistics;

import java.util.List;
import javax.xml.datatype.Duration;
/**
 * Created by Tim on 4/27/2017.
 */

public class StatisticsStub implements Statistics {
    private List<Card> correct;
    private List<Card> incorrect;
    private int averageAnswerTime;

    public List<Card> getCorrectCards() {
        return correct;
    }

    public List<Card> getIncorrectCards() {
        return incorrect;
    }

    public int getCorrect() {
        int correctCount = 0;
        return correctCount;
    }

    public int getIncorrect() {
        int incorrectCount = 0;
        return incorrectCount;
    }

    @Override
    public Long getAverageAnswerTime() {
        return null;
    }
}
