package com.psu.capstonew17.pdxaslapp.TestUI;


import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.psu.capstonew17.pdxaslapp.CreateCardActivity;
import com.psu.capstonew17.pdxaslapp.CreateEditDeleteDeckActivity;
import com.psu.capstonew17.pdxaslapp.DeleteCardActivity;
import com.psu.capstonew17.pdxaslapp.HomeActivity;
import com.psu.capstonew17.pdxaslapp.ManageCardsSubMenuActivity;
import com.psu.capstonew17.pdxaslapp.ManageDecksSubMenuActivity;
import com.psu.capstonew17.pdxaslapp.R;
import com.psu.capstonew17.pdxaslapp.ReceiveDeckActivity;
import com.psu.capstonew17.pdxaslapp.ShareDeckActivity;
import com.psu.capstonew17.pdxaslapp.TakeQuizSubMenuActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class NavigationFlowTest {

    @Rule
    public ActivityTestRule<HomeActivity> mActivityTestRule = new ActivityTestRule<>(HomeActivity.class);

    @Test
    public void navigationTest(){
        // make sure to have all requirement permission (memory,...)

        // test go to choose quiz
        onView(withId(R.id.button_take_quiz)).check(matches(ViewMatchers.isDisplayed()));
        Intents.init();
        onView(withId(R.id.button_take_quiz)).perform(click());
        intended(hasComponent(TakeQuizSubMenuActivity.class.getName()));
        Intents.release();
        pressBack();

        // go to manage deck
        onView(withId(R.id.button_manage_decks)).check(matches(ViewMatchers.isDisplayed()));
        Intents.init();
        onView(withId(R.id.button_manage_decks)).perform(click());
        intended(hasComponent(ManageDecksSubMenuActivity.class.getName()));
        Intents.release();

        // on manage deck activity
        onView(withId(R.id.button_ced_decks)).check(matches(ViewMatchers.isDisplayed()));
        Intents.init();
        onView(withId(R.id.button_ced_decks)).perform(click());
        intended(hasComponent(CreateEditDeleteDeckActivity.class.getName()));
        Intents.release();
        pressBack(); // to home screen

        Intents.init();
        onView(withId(R.id.button_manage_decks)).perform(click());
        intended(hasComponent(ManageDecksSubMenuActivity.class.getName()));
        Intents.release();

        // on manage deck, go to receive deck
        onView(withId(R.id.button_receive_deck)).check(matches(ViewMatchers.isDisplayed()));
        Intents.init();
        onView(withId(R.id.button_receive_deck)).perform(click());
        intended(hasComponent(ReceiveDeckActivity.class.getName()));
        Intents.release();
        pressBack(); // home screen now

        // on home screen now, go to manage deck
        Intents.init();
        onView(withId(R.id.button_manage_decks)).perform(click());
        intended(hasComponent(ManageDecksSubMenuActivity.class.getName()));
        Intents.release();

        // go to share deck
        onView(withId(R.id.button_share_deck)).check(matches(ViewMatchers.isDisplayed()));
        Intents.init();
        onView(withId(R.id.button_share_deck)).perform(click());
        intended(hasComponent(ShareDeckActivity.class.getName()));
        Intents.release();
        pressBack();

        // in home activity
        onView(withId(R.id.button_manage_cards)).check(matches(ViewMatchers.isDisplayed()));
        Intents.init();
        onView(withId(R.id.button_manage_cards)).perform(click());
        intended(hasComponent(ManageCardsSubMenuActivity.class.getName()));
        Intents.release();

        // on manage card, go to create card
        onView(withId(R.id.button_create_card)).check(matches(ViewMatchers.isDisplayed()));
        Intents.init();
        onView(withId(R.id.button_create_card)).perform(click());
        intended(hasComponent(CreateCardActivity.class.getName()));
        Intents.release();
        pressBack();        // manage card screen

        // on home, go to manage card
        Intents.init();
        onView(withId(R.id.button_manage_cards)).perform(click());
        intended(hasComponent(ManageCardsSubMenuActivity.class.getName()));
        Intents.release();

        // check and go to delete card
        onView(withId(R.id.button_delete_card)).check(matches(ViewMatchers.isDisplayed()));
        Intents.init();
        onView(withId(R.id.button_delete_card)).perform(click());
        intended(hasComponent(DeleteCardActivity.class.getName()));
        Intents.release();
        pressBack();


    }

}
