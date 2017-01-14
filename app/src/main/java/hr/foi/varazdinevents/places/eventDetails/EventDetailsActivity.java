package hr.foi.varazdinevents.places.eventDetails;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.transition.Fade;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import hr.foi.varazdinevents.MainApplication;
import hr.foi.varazdinevents.R;
import hr.foi.varazdinevents.api.EventManager;
import hr.foi.varazdinevents.injection.modules.EventDetailsActivityModule;
import hr.foi.varazdinevents.models.Event;
import hr.foi.varazdinevents.models.User;
import hr.foi.varazdinevents.places.hostProfile.HostProfileActivity;
import hr.foi.varazdinevents.ui.base.BaseNavigationActivity;
import hr.foi.varazdinevents.util.Constants;
import hr.foi.varazdinevents.util.FontManager;
import rx.Observer;

/**
 * Created by Antonio Martinović on 08.11.16.
 */

public class EventDetailsActivity extends BaseNavigationActivity {
    private static final String ARG_EVENT = "arg_event";
    private Event event;
    Double latitude, longitude;
    String locationTitle, locationCategory;

    @Inject
    User user;
    @Inject
    EventDetailsPresenter presenter;
    @Inject
    @Nullable
    Fade animation;
//    @Inject
//    EventManager eventManager;

    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Nullable
    @BindView(R.id.progresBar)
    ProgressBar progressBar;
    @BindView(R.id.details_content_holder)
    LinearLayout contentHolder;
    @BindView(R.id.event_details_image)
    ImageView image;
    @BindView(R.id.event_details_title)
    TextView title;
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

    @BindView(R.id.fab_detailed_favorite)
    FloatingActionButton fab_detailed_favorite;
//    @BindView(R.id.fab_basic_favorite)
//    FloatingActionButton fab_basic_favorite;


    @BindView(R.id.awesome_title)
    TextView awesomeTitle;
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
        toggleFavoriteIcon(this.event.isFavorite);

        collapsingToolbarLayout.setTitle(event.getTitle());
        toolbar.setTitle(event.getTitle());

        if(event.getType() != Constants.EVENTS_NO_IMAGE_CARD) {
            collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

            Picasso.with(this)
                    .load(event.getImage())
                    .resize(380, 380)
                    .centerCrop()
                    .into(image);
        } else {
            appBarLayout.setExpanded(false);
            AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) collapsingToolbarLayout.getLayoutParams();
            params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP | AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
//            image.getLayoutParams().height = 200;
//            collapsingToolbarLayout.setLayoutParams(params);
        }

        Typeface iconFont = FontManager.getFontAwesome(getApplicationContext());
        FontManager.markAsIconContainer(awesomeTitle, iconFont);
        FontManager.markAsIconContainer(awesomeCalendar, iconFont);
        FontManager.markAsIconContainer(awesomeClock, iconFont);
        FontManager.markAsIconContainer(awesomeFaceboot, iconFont);
        FontManager.markAsIconContainer(awesomeHost, iconFont);
        FontManager.markAsIconContainer(awesomeRocket, iconFont);

//        collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(primary));
//        collapsingToolbarLayout.setStatusBarScrimColor(palette.getDarkMutedColor(primaryDark));
//        fab_detailed_favorite.setRippleColor(lightVibrantColor);
//        fab_detailed_favorite.setBackgroundTintList(ColorStateList.valueOf(vibrantColor));

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(animation);
        }

    }

    @Override
    protected User getUser() {
        return user;
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
        presenter.parseEventData(event, this.text);

        Date eventDate = new Date(event.getDate() - 3600000);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        this.date.setText(dateFormat.format(eventDate));

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        this.time.setText(timeFormat.format(eventDate));

        this.title.setText(event.getTitle());
        this.host.setText(event.getHost());
        this.host.setPaintFlags(this.host.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        this.category.setText(event.getCategory());
        this.offers.setText(event.getOffers());

        Observer<Void> mapObserver = new Observer<Void>() {
            @Override
            public void onCompleted() {
                LatLng latlong = new LatLng(presenter.getLatitude(), presenter.getLongitude());
                Marker marker = presenter.getMap().addMarker(new MarkerOptions().position(latlong).title(locationTitle).snippet(locationCategory));
                marker.showInfoWindow();
                presenter.getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(latlong, 17));
                EventDetailsActivity.this.facebook.setMovementMethod(LinkMovementMethod.getInstance());
                showLoading(false);
            }

            @Override
            public void onError(Throwable e) {
                showBasicError("Error displaying something");
            }

            @Override
            public void onNext(Void aVoid) {}
        };

        rx.Observable<Void> eventStream = presenter.parseEventData(event, this.text);
        eventStream.subscribe(mapObserver);
    }

    @Override
    protected void onStop() {
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

    @OnClick(R.id.fab_detailed_favorite)
    public void onFavoriteClicked() {
        this.event.setFavorite(EventManager.toggleFavorite(this.event));
        toggleFavoriteIcon(this.event.isFavorite);
    }

    @OnClick(R.id.event_details_facebook)
    public void onFacebookClicked() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(event.getFacebook()));
        startActivity(intent);
    }

    public void toggleFavoriteIcon(boolean isFavorite) {
        if (isFavorite)
            fab_detailed_favorite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favorite_white_24dp));
        else
            fab_detailed_favorite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favorite_border_white_24dp));
    }

    @OnClick(R.id.event_details_host)
    public void onClick(View view) {
        Intent newIntent = new Intent(EventDetailsActivity.this, HostProfileActivity.class);
        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        EventDetailsActivity.this.startActivity(newIntent);
    }
}
