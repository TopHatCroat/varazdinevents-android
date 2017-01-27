package hr.foi.varazdinevents;

import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import hr.foi.varazdinevents.places.events.MainActivity;
import hr.foi.varazdinevents.places.login.LoginActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Valentin Magdić on 27.01.17..
 */

public class SelectCategory {

    @Rule
    public ActivityTestRule<MainActivity> mSelectCategoryActivityTestRule =
            new ActivityTestRule<MainActivity>(MainActivity.class);

    @Test
    public void selectCategory() throws Exception{
//        onView(withId(R.id.item_handle))
//                .perform(click());
//
//        onView(withId(R.id.action_party))
//                .perform(click());
    }
}
