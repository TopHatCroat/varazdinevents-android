package hr.foi.varazdinevents;

import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import hr.foi.varazdinevents.places.facebook.FacebookActivity;
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

public class ImportFacebookEvent {

    private String idToBeTyped;

    @Rule
    public ActivityTestRule<FacebookActivity> mInputLoginTextActivityTestRule =
            new ActivityTestRule<FacebookActivity>(FacebookActivity.class);

    @Before
    public void initValidString(){
        idToBeTyped = "1111111";
    }
    @Test
    public void inputText() throws Exception{
        onView(withId(R.id.facebook_import_event_id))
                .perform(typeText(idToBeTyped));

        onView(withId(R.id.facebook_import_event_id))
                .check(matches(withText(idToBeTyped)));

//        onView(withId(R.id.fb_import_event_id))
//                .perform(click());
    }
}
