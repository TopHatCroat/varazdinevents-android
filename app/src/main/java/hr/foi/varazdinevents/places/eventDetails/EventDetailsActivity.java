package hr.foi.varazdinevents.places.eventDetails;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
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

    @Inject
    EventDetailsPresenter presenter;

    @BindView(R.id.event_details_image)
    ImageView image;
    @BindView(R.id.event_details_title)
    TextView title;
    @BindView(R.id.event_details_date)
    TextView date;
    @BindView(R.id.event_details_dateTo)
    TextView dateTo;
    @BindView(R.id.event_details_host)
    TextView host;
    @BindView(R.id.event_details_category)
    TextView category;
    @BindView(R.id.event_details_facebook)
    TextView facebook;
    @BindView(R.id.event_details_offers)
    TextView offers;
    @BindView(R.id.event_details_officialLink)
    TextView officialLink;
    @BindView(R.id.event_details_text)
    TextView text;

    @Override
    protected int getLayout() {
        return R.layout.activity_event_details;
    }

    @Override
    protected void setupActivityComponent() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //super.onFinishInflate();
        //presenter.attachView(this);
        showLoading(true);
        Event event = presenter.getEvent();
        //image.setText(event.getImage());
        this.title.setText(event.getTitle());
        this.date.setText(event.getDate().toString());
        this.dateTo.setText(event.getDateTo().toString());
        this.host.setText(event.getHost());
        this.category.setText(event.getCategory());
        this.facebook.setText(event.getFacebook());
        this.offers.setText(event.getOffers());
        this.officialLink.setText(event.getOfficialLink());
        this.text.setText(event.getText());
        showLoading(false);
    }

    public void showLoading(boolean loading) {
//        recyclerView.setVisibility(loading ? View.GONE : View.VISIBLE);
//        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

    public static void startWithEvent(Event event, Context startingActivity) {
        Intent intent = new Intent(startingActivity, EventDetailsActivity.class);
        intent.putExtra(ARG_EVENT, event);
        startingActivity.startActivity(intent);
    }

    public EventDetailsActivityComponent getMainActivityComponent() {
        if (eventDetailsActivityComponent == null) {
            eventDetailsActivityComponent = MainApplication.get(this).getUserComponent()
                    .plus(new EventDetailsActivityModule(this));
        }
        return eventDetailsActivityComponent;
    }

}
