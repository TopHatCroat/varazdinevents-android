package hr.foi.varazdinevents;

import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import hr.foi.varazdinevents.places.login.LoginActivity;
import hr.foi.varazdinevents.places.register.RegisterActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Valentin Magdić on 27.01.17..
 */

public class InputRegistrationText {

    private String usernameToBeTyped;
    private String emailToBeTyped;
    private String passwordToBeTyped1;
    private String passwordToBeTyped2;

    @Rule
    public ActivityTestRule<RegisterActivity> mInputLoginTextActivityTestRule =
            new ActivityTestRule<RegisterActivity>(RegisterActivity.class);

    @Before
    public void initValidString(){
        usernameToBeTyped = "test1";
        emailToBeTyped = "test2";
        passwordToBeTyped1 = "test3";
        passwordToBeTyped2 = "test4";

    }
    @Test
    public void inputText() throws Exception{
        onView(withId(R.id.TFusername_register))
                .perform(typeText(usernameToBeTyped));

        onView(withId(R.id.TFemail_register))
                .perform(typeText(emailToBeTyped));

//        onView(withId(R.id.TFpass1_register))
//                .perform(typeText(passwordToBeTyped1));
//
//        onView(withId(R.id.TFpass2_register))
//                .perform(typeText(passwordToBeTyped2));
//

        onView(withId(R.id.TFusername_register))
                .check(matches(withText(usernameToBeTyped)));

        onView(withId(R.id.TFemail_register))
                .check(matches(withText(emailToBeTyped)));

//        onView(withId(R.id.TFpass1_register))
//                .check(matches(withText(passwordToBeTyped1)));
//
//        onView(withId(R.id.TFpass2_register))
//                .check(matches(withText(passwordToBeTyped2)));

//        onView(withId(R.id.login_button))
//                .perform(click());
    }
}
