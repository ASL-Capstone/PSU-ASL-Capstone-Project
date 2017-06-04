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


//        ViewInteraction appCompatButton = onView(
//                allOf(ViewMatchers.withId(R.id.button_manage_cards), withText("Manage Cards"), isDisplayed()));
//        appCompatButton.perform(click());
//
//        ViewInteraction appCompatButton2 = onView(
//                allOf(withId(R.id.button_create_card), withText("Create Card"), isDisplayed()));
//        appCompatButton2.perform(click());
//
//        ViewInteraction appCompatEditText = onView(
//                allOf(withId(R.id.edit_text_video_answer),
//                        withParent(allOf(withId(R.id.activity_create_card),
//                                withParent(withId(android.R.id.content)))),
//                        isDisplayed()));
//        appCompatEditText.perform(click());
//
//        ViewInteraction appCompatEditText2 = onView(
//                allOf(withId(R.id.edit_text_video_answer),
//                        withParent(allOf(withId(R.id.activity_create_card),
//                                withParent(withId(android.R.id.content)))),
//                        isDisplayed()));
//        appCompatEditText2.perform(replaceText("card"), closeSoftKeyboard());
//
//        ViewInteraction appCompatEditText3 = onView(
//                allOf(withId(R.id.edit_text_video_answer), withText("card"),
//                        withParent(allOf(withId(R.id.activity_create_card),
//                                withParent(withId(android.R.id.content)))),
//                        isDisplayed()));
//        appCompatEditText3.perform(click());
//
//        ViewInteraction appCompatEditText4 = onView(
//                allOf(withId(R.id.edit_text_video_answer), withText("card"),
//                        withParent(allOf(withId(R.id.activity_create_card),
//                                withParent(withId(android.R.id.content)))),
//                        isDisplayed()));
//        appCompatEditText4.perform(replaceText("ard"), closeSoftKeyboard());
//
//        ViewInteraction appCompatEditText5 = onView(
//                allOf(withId(R.id.edit_text_video_answer), withText("ard"),
//                        withParent(allOf(withId(R.id.activity_create_card),
//                                withParent(withId(android.R.id.content)))),
//                        isDisplayed()));
//        appCompatEditText5.perform(click());
//
//        ViewInteraction appCompatEditText6 = onView(
//                allOf(withId(R.id.edit_text_video_answer), withText("ard"),
//                        withParent(allOf(withId(R.id.activity_create_card),
//                                withParent(withId(android.R.id.content)))),
//                        isDisplayed()));
//        appCompatEditText6.perform(replaceText("card x"), closeSoftKeyboard());
//
//        pressBack();
//
//        ViewInteraction appCompatButton3 = onView(
//                allOf(withId(R.id.buttonFromGallery), withText("Gallery"), isDisplayed()));
//        appCompatButton3.perform(click());
//
//        // Added a sleep statement to match the app's execution delay.
//        // The recommended way to handle such scenarios is to use Espresso idling resources:
//        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        ViewInteraction switch_ = onView(
//                allOf(withId(R.id.start_stop_switch), withText("Edit Start/Stop time"), isDisplayed()));
//        switch_.perform(click());
//
//        ViewInteraction appCompatButton4 = onView(
//                allOf(withId(R.id.submitButtonEditCard), withText("Submit"), isDisplayed()));
//        appCompatButton4.perform(click());
//
//        ViewInteraction appCompatButton5 = onView(
//                allOf(withId(R.id.button_submit), withText("Submit"),
//                        withParent(allOf(withId(R.id.activity_create_card),
//                                withParent(withId(android.R.id.content)))),
//                        isDisplayed()));
//        appCompatButton5.perform(click());

    }

}
