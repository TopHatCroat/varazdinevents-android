package hr.foi.varazdinevents.places.eventDetails;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import hr.foi.varazdinevents.MainApplication;
import hr.foi.varazdinevents.R;
import hr.foi.varazdinevents.injection.EventDetailsActivityComponent;
import hr.foi.varazdinevents.injection.modules.EventDetailsActivityModule;
import hr.foi.varazdinevents.models.Event;
import hr.foi.varazdinevents.ui.base.BaseActivity;

/**
 * Created by Antonio Martinović on 08.11.16.
 */

public class EventDetailsActivity extends BaseActivity {
    private static final String ARG_EVENT = "arg_event";

    EventDetailsActivityComponent eventDetailsActivityComponent;

    @Override
    protected int getLayout() {
        return R.layout.activity_event_details;
    }

    public static void startWithRepository(Event event, Context startingActivity) {
        Intent intent = new Intent(startingActivity, EventDetailsActivity.class);
        intent.putExtra(ARG_EVENT, event);
        startingActivity.startActivity(intent);
    }

    public EventDetailsActivityComponent getMainActivityComponent() {
        if (eventDetailsActivityComponent == null) {
            eventDetailsActivityComponent = MainApplication.get(this).getComponent()
                    .plus(new EventDetailsActivityModule(this));
        }
        return eventDetailsActivityComponent;
    }
}
