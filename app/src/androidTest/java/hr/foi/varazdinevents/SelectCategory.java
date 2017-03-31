package hr.foi.varazdinevents;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import hr.foi.varazdinevents.places.events.MainActivity;
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
