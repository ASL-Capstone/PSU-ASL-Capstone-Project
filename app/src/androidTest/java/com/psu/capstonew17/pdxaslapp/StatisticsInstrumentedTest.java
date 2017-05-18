package com.psu.capstonew17.pdxaslapp;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.psu.capstonew17.backend.api.Card;
import com.psu.capstonew17.backend.api.CardManager;
import com.psu.capstonew17.backend.api.DeckManager;
import com.psu.capstonew17.backend.api.Statistics;
import com.psu.capstonew17.backend.api.StatisticsManager;
import com.psu.capstonew17.backend.api.Video;
import com.psu.capstonew17.backend.api.VideoManager;
import com.psu.capstonew17.backend.api.stubs.StatisticsStub;
import com.psu.capstonew17.backend.data.ExternalCardManager;
import com.psu.capstonew17.backend.data.ExternalStatisticsManager;
import com.psu.capstonew17.backend.data.ExternalVideoManager;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Created by Tim on 5/11/2017.
 */

@RunWith(AndroidJUnit4.class)
public class StatisticsInstrumentedTest {
    private Context context = InstrumentationRegistry.getTargetContext();

    @Test
    public void getCorrectCards() {
        Statistics stats = new StatisticsStub();
        assertEquals(null, stats.getCorrectCards());
    }

    @Test
    public void getIncorrectCard() {
        Statistics stats = new StatisticsStub();
        assertEquals(null, stats.getIncorrectCards());
    }

    @Test
    public void getCorrect() {
        Statistics stats = new StatisticsStub();
        assertEquals(null, stats.getCorrect());
    }

    @Test
    public void getIncorrect() {
        Statistics stats = new StatisticsStub();
        assertEquals(null, stats.getIncorrect());
    }

    @Test
    public void getAverageAnswerTime() {
        Statistics stats = new StatisticsStub();
        assertEquals(null, stats.getAverageAnswerTime());
    }

    @Test
    public void forTimeSpan() {
        StatisticsManager statsManager = ExternalStatisticsManager.getInstance(context);
        Statistics stats = new StatisticsStub();

        stats = statsManager.forTimeSpan(0, 1);
        assertNotEquals(null, stats);
    }

    /*
    @Test
    public void forCard() {
        CardManager cardManager = ExternalCardManager.getInstance(context);
        VideoManager videoManager = ExternalVideoManager.getInstance(context);
        Video video = videoManager.
        Card card = cardManager.buildCard();
    }*/
}
