package com.psu.capstonew17.backend.api.stubs;

import java.util.List;
import android.util.Pair;

import com.psu.capstonew17.backend.api.Question;
import com.psu.capstonew17.backend.api.Video;

/**
 * Created by Tim on 4/27/2017.
 */

public class QuestionStub implements Question {
    private Video video;
    private List<String> answers;
    private Pair<Boolean, String> correctAnswer;
    enum Type {
        MULTIPLE_CHOICE,
        TEXT_ENTRY
    }

    public Video getVideo() {
        return this.video;
    }

    @Override
    public Question.Type getType() {
        return null;
    }

    public List<String> getOptions() {
        return answers;
    }

    public Pair<Boolean, String> answer(String answer) {
        return correctAnswer;
    }
}
