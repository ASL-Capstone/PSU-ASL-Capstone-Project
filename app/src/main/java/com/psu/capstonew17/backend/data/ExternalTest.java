package com.psu.capstonew17.backend.data;

import com.psu.capstonew17.backend.api.*;

import java.util.List;


public class ExternalTest implements Test {
    private List<Question> questions;
    private int current;

    public ExternalTest(List<Question> questions){
        this.questions = questions;
        this.current = 0;
    }

    @Override
    public Statistics getStats() {
        return null;
    }

    @Override
    public boolean hasNext() {
        return current < questions.size() && questions.get(current) != null;
    }

    @Override
    public Question next() {
        current += 1;
        return questions.get(current);
    }
}
