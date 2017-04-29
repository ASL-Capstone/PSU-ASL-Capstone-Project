package com.psu.capstonew17.backend.api.stubs;

import com.psu.capstonew17.backend.api.Card;
import com.psu.capstonew17.backend.api.Deck;
import com.psu.capstonew17.backend.api.Statistics;
import com.psu.capstonew17.backend.api.StatisticsManager;

/**
 * Created by Tim on 4/28/2017.
 */

public class StatisticsManagerStub implements StatisticsManager {
    @Override
    public Statistics forCard(Card c) {
        return null;
    }

    @Override
    public Statistics forDeck(Deck d) {
        return null;
    }

    @Override
    public Statistics forTimeSpan(long start, long length) {
        return null;
    }
}
