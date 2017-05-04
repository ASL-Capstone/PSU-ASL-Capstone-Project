//MIT License Copyright 2017 PSU ASL Capstone Team

package com.psu.capstonew17.pdxaslapp.FrontEndTestStubs;

import android.util.Pair;

import com.psu.capstonew17.backend.api.Question;
import com.psu.capstonew17.backend.api.Video;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Doug Whitley on 5/1/2017.
 */

public class testMultiChoiceQuestion implements Question {
    @Override
    public Video getVideo() {
        return null;
    }

    @Override
    public List<String> getOptions() {
        ArrayList<String> ret = new ArrayList<>();
        for(int i = 0; i < 3; ++i){
            ret.add("False");
        }
        ret.add("True");
        return ret;
    }

    @Override
    public Pair<Boolean, String> answer(String answer) {
        if (answer.compareTo("True") == 0){
            return new Pair<Boolean, String>(Boolean.TRUE,"True");
        }
            return new Pair<Boolean, String>(Boolean.FALSE,"True");
    }

    @Override
    public Type getType() {
        return Type.MULTIPLE_CHOICE;
    }
}
