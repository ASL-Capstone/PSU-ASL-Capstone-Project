package com.psu.capstonew17.backend.api;

/**
 * Created by Tim on 4/22/2017.
 */

public class CardManagerStub implements CardManager {

    public Card buildCard(Video video, String answer) throws ObjectAlreadyExistsException{
        Card temp = new CardStub();
        temp.setAnswer(answer);
        temp.setVideo(video);

        return temp;
    }
}
