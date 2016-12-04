package hr.foi.varazdinevents.places.eventDetails;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.text.Html;
import android.transition.Fade;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

import javax.annotation.Nullable;
import javax.inject.Inject;

import butterknife.BindView;
import hr.foi.varazdinevents.MainApplication;
import hr.foi.varazdinevents.R;
import hr.foi.varazdinevents.injection.modules.EventDetailsActivityModule;
import hr.foi.varazdinevents.models.Event;
import hr.foi.varazdinevents.ui.base.BaseActivity;
import hr.foi.varazdinevents.util.FontManager;

/**
 * Created by Antonio Martinović on 08.11.16.
 */

public class EventDetailsActivity extends BaseActivity {
    private static final String ARG_EVENT = "arg_event";

    private Event event;

    @Inject
    EventDetailsPresenter presenter;
    @Inject
    @Nullable
    Fade animation;

    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Nullable
    @BindView(R.id.progresBar)
    ProgressBar progressBar;
    @BindView(R.id.details_content_holder)
    LinearLayout contentHolder;
    @BindView(R.id.event_details_image)
    ImageView image;
    @BindView(R.id.event_details_date)
    TextView date;
    @BindView(R.id.event_details_time)
    TextView time;
    @BindView(R.id.event_details_host)
    TextView host;
    @BindView(R.id.event_details_category)
    TextView category;
    @BindView(R.id.event_details_facebook)
    TextView facebook;
    @BindView(R.id.event_details_offers)
    TextView offers;
//    @BindView(R.id.event_details_officialLink)
//    TextView officialLink;
    @BindView(R.id.event_details_text)
    TextView text;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.awesome_calendar)
    TextView awesomeCalendar;
    @BindView(R.id.awesome_clock)
    TextView awesomeClock;
    @BindView(R.id.awesome_facebook)
    TextView awesomeFaceboot;
    @BindView(R.id.awesome_host)
    TextView awesomeHost;
    @BindView(R.id.awesome_rocket)
    TextView awesomeRocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.event = getIntent().getParcelableExtra(ARG_EVENT);

        collapsingToolbarLayout.setTitle(event.getTitle());
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        toolbar.setTitle(event.getTitle());
        Picasso.with(this)
                .load(event.getImage())
                .resize(380, 380)
                .centerCrop()
                .into(image);

        Typeface iconFont = FontManager.getFontAwesome(getApplicationContext());
        FontManager.markAsIconContainer(awesomeCalendar, iconFont);
        FontManager.markAsIconContainer(awesomeClock, iconFont);
        FontManager.markAsIconContainer(awesomeFaceboot, iconFont);
        FontManager.markAsIconContainer(awesomeHost, iconFont);
        FontManager.markAsIconContainer(awesomeRocket, iconFont);

//        collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(primary));
//        collapsingToolbarLayout.setStatusBarScrimColor(palette.getDarkMutedColor(primaryDark));
//        fab.setRippleColor(lightVibrantColor);
//        fab.setBackgroundTintList(ColorStateList.valueOf(vibrantColor));

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(animation);
        }

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_event_details;
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.attachView(this);
        showLoading(true);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        this.date.setText(dateFormat.format(event.date*1000L));

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        this.time.setText(timeFormat.format(event.date*1000L));

        this.host.setText(event.getHost());
        this.category.setText(event.getCategory());
        this.facebook.setText(event.getFacebook());
        this.offers.setText(event.getOffers());
//        this.officialLink.setText(event.getOfficialLink());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            this.text.setText(Html.fromHtml(event.getText(), Html.FROM_HTML_MODE_LEGACY).toString());
        } else {
            this.text.setText(Html.fromHtml(event.getText()).toString());
        }
        showLoading(false);

    }

    @Override
    protected void onStop(){
        super.onStop();
        presenter.detachView();
    }

    public void showLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        contentHolder.setVisibility(loading ? View.GONE : View.VISIBLE);
    }

    public static void startWithEvent(Event event, Context startingActivity) {
        Intent intent = new Intent(startingActivity, EventDetailsActivity.class);
        intent.putExtra(ARG_EVENT, event);
        startingActivity.startActivity(intent);
    }

    @Override
    public void setupActivityComponent() {
        MainApplication.get(this).getUserComponent()
                .plus(new EventDetailsActivityModule(this))
                .inject(this);
    }

    @Override
    public void onItemClicked(Object item) {

    }

}
