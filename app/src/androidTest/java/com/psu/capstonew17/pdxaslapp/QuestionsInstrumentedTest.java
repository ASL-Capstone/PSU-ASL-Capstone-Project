package com.psu.capstonew17.pdxaslapp;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Pair;


import com.psu.capstonew17.backend.api.Card;
import com.psu.capstonew17.backend.api.Question;
import com.psu.capstonew17.backend.api.Video;
import com.psu.capstonew17.backend.api.stubs.CardStub;
import com.psu.capstonew17.backend.api.stubs.QuestionStub;
import com.psu.capstonew17.backend.api.stubs.VideoStub;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Created by Tim on 5/10/2017.
 */

@RunWith(AndroidJUnit4.class)
public class QuestionsInstrumentedTest {
    private Context context = InstrumentationRegistry.getTargetContext();

    @Test
    public void getVideo() {
        Question question = new QuestionStub();

        assertEquals(null, question.getVideo());
    }

    @Test
    public void getType() {
        Question question = new QuestionStub();

        assertEquals(null, question.getType());
    }

    @Test
    public void getOptions() {
        Question question = new QuestionStub();

        assertEquals(null, question.getOptions());
    }

    @Test
    public void answer() {
        Question question = new QuestionStub();

    }
}
