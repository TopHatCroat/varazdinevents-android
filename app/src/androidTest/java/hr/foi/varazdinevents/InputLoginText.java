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

public class InputLoginText {

    private String usernameToBeTyped;
    private String passwordToBeTyped;

    @Rule
    public ActivityTestRule<LoginActivity> mInputLoginTextActivityTestRule =
            new ActivityTestRule<LoginActivity>(LoginActivity.class);

    @Before
    public void initValidString(){
        usernameToBeTyped = "test1";
        passwordToBeTyped = "test2";
    }
    @Test
    public void inputText() throws Exception{
        onView(withId(R.id.TFusername))
                .perform(typeText(usernameToBeTyped));

        onView(withId(R.id.TFpassword))
                .perform(typeText(passwordToBeTyped));

        onView(withId(R.id.TFusername))
                .check(matches(withText(usernameToBeTyped)));

        onView(withId(R.id.TFpassword))
                .check(matches(withText(passwordToBeTyped)));

//        onView(withId(R.id.login_button))
//                .perform(click());
    }
}
