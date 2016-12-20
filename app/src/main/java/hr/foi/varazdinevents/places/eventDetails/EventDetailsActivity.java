package hr.foi.varazdinevents.places.eventDetails;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import hr.foi.varazdinevents.ui.base.BaseActivity;
import hr.foi.varazdinevents.util.FontManager;

/**
 * Created by Antonio Martinović on 08.11.16.
 */

public class EventDetailsActivity extends BaseActivity implements OnMapReadyCallback {
    private static final String ARG_EVENT = "arg_event";
    private GoogleMap mMap;
    private Event event;
    Double latitude;
    Double longitude;
    String location_title;
    @Inject
    EventDetailsPresenter presenter;
    @Inject
    @Nullable
    Fade animation;
//    @Inject
//    EventManager eventManager;

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
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        toolbar.setTitle(event.getTitle());
        Picasso.with(this)
                .load(event.getImage())
                .resize(380, 380)
                .centerCrop()
                .into(image);

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

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        this.date.setText("Datum: " + dateFormat.format(event.date*1000L));

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        this.time.setText("Vrijeme: " + timeFormat.format(event.date*1000L));

        this.title.setText("Naziv: " + event.getTitle());
        this.host.setText("Organizator: " + event.getHost());
        this.category.setText("Kategorija: " + event.getCategory());
        this.facebook.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "Facebook: <a href='" + event.getFacebook() + "'><b>Poveznica na event</b></a>";
        this.offers.setText(event.getOffers());

        //Google map information
            String location = "731 Market St, San Francisco, CA 94103";
            Geocoder geocoder = new Geocoder(this);
            List<Address> addressList;
            try {
                addressList = geocoder.getFromLocationName(location, 1);
                if (addressList.size()!=0) {
                    Address address = addressList.get(0);
                    this.latitude = address.getLatitude();
                    this.longitude = address.getLongitude();
                }
                else {
                    this.latitude = 46.294399;
                    this.longitude = 16.344964;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        this.location_title = event.getTitle();

//        this.officialLink.setText(event.getOfficialLink());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            this.text.setText(Html.fromHtml(event.getText(), Html.FROM_HTML_MODE_LEGACY).toString());
            this.facebook.setText(Html.fromHtml(text,Html.FROM_HTML_MODE_LEGACY));
        } else {
            this.text.setText(Html.fromHtml(event.getText()).toString());
            this.facebook.setText(Html.fromHtml(text));
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

    @OnClick(R.id.fab_detailed_favorite)
    public void onFavoriteClicked(){
        this.event.setFavorite(EventManager.toggleFavorite(this.event));
        toggleFavoriteIcon(this.event.isFavorite);
    }

    public void toggleFavoriteIcon(boolean isFavorite){
        if(isFavorite) fab_detailed_favorite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favorite_red_500_24dp));
        else fab_detailed_favorite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favorite_border_red_400_24dp));
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng latlong = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(latlong).title(location_title));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlong,17));
        mMap.setMyLocationEnabled(true);
    }
}
