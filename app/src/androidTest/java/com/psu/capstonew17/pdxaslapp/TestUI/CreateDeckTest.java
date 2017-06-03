package com.psu.capstonew17.pdxaslapp.TestUI;

import android.content.Intent;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.Button;

import com.psu.capstonew17.pdxaslapp.CreateCardActivity;
import com.psu.capstonew17.pdxaslapp.CreateDeckActivity;
import com.psu.capstonew17.pdxaslapp.HomeActivity;
import com.psu.capstonew17.pdxaslapp.R;
import com.psu.capstonew17.pdxaslapp.TakeQuizSubMenuActivity;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class CreateDeckTest {

    @Rule
    public ActivityTestRule<CreateDeckActivity> mActivityActivityTestRule
            = new ActivityTestRule<>(
            CreateDeckActivity.class,
            true,
            false);

    @Test
    public void intent(){
        Intent intent = new Intent();
        mActivityActivityTestRule.launchActivity(intent);

        // test clickable radiobutton
        onView(withId(R.id.radioButtonFlashCard)).perform(click());
        onView(withId(R.id.radioButtonFlashCard)).check(matches(isChecked()));

    }





}
