package hr.foi.varazdinevents;

import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import hr.foi.varazdinevents.places.events.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.action.ViewActions.typeTextIntoFocusedView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Valentin Magdić on 27.01.17..
 */

public class SearchText {

    private String stringToBeTyped;

    @Rule
    public ActivityTestRule<MainActivity> mSearchTextActivityTestRule =
            new ActivityTestRule<MainActivity>(MainActivity.class);

    @Before
    public void initValidString(){
        stringToBeTyped = "test";
    }

    @Test
    public void searchText() throws Exception{
        onView(withId(R.id.action_search))
                .perform(click());

        onView(withId(R.id.action_search))
                .perform(typeText(stringToBeTyped));

        onView(withId(R.id.action_search))
                .check(matches(withText(stringToBeTyped)));
    }
}
