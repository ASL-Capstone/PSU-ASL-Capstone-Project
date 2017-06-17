package com.psu.capstonew17.pdxaslapp.TestUI;


import android.content.Intent;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.psu.capstonew17.pdxaslapp.CreateCardActivity;
import com.psu.capstonew17.pdxaslapp.HomeActivity;
import com.psu.capstonew17.pdxaslapp.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class CreateCardTest {

    @Rule
    public ActivityTestRule<CreateCardActivity> mActivityActivityTestRule
            = new ActivityTestRule<>(
            CreateCardActivity.class,
            true,
            false);

    @Test
    public void createCardTest() {
        Intent intent = new Intent();
        mActivityActivityTestRule.launchActivity(intent);

    }

}
