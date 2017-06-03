package com.psu.capstonew17.pdxaslapp.TestUI;

import android.content.Intent;
import android.support.test.espresso.ViewInteraction;
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
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

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
    public void intent() {
        Intent intent = new Intent();
        mActivityActivityTestRule.launchActivity(intent);

        onView(withId(R.id.createDeckNameField)).perform(replaceText("deck x"), closeSoftKeyboard());
        onView(withId(R.id.createDeckNameField)).check(matches(withText("deck x")));

        onView(withId(R.id.bttn_create_done)).check(matches(ViewMatchers.isDisplayed()));
        onView(withId(R.id.list_items)).check(matches(ViewMatchers.isDisplayed()));

    }


}
