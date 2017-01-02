package hr.foi.varazdinevents.places.hostProfile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Inject;

import butterknife.BindView;
import hr.foi.varazdinevents.MainApplication;
import hr.foi.varazdinevents.R;
import hr.foi.varazdinevents.api.UserManager;
import hr.foi.varazdinevents.injection.modules.EventDetailsActivityModule;
import hr.foi.varazdinevents.injection.modules.HostProfileActivityModule;
import hr.foi.varazdinevents.models.Event;
import hr.foi.varazdinevents.models.User;
import hr.foi.varazdinevents.places.eventDetails.EventDetailsActivity;
import hr.foi.varazdinevents.places.events.MainPresenter;
import hr.foi.varazdinevents.ui.base.BaseNavigationActivity;
import hr.foi.varazdinevents.ui.base.BasePresenter;
import hr.foi.varazdinevents.util.FontManager;
import rx.Observer;

import static hr.foi.varazdinevents.util.Constants.ARG_EVENT;

/**
 * Created by Valentin Magdić on 26.12.16..
 */

public class HostProfileActivity extends BaseNavigationActivity{
    private static final String ARG_EVENT = "arg_event";
//    private GoogleMap mMap;
    private Event event;
    @Inject
    User user;
    @Inject
    HostProfilePresenter presenter;
    @Inject
    @Nullable
    Fade animation;
    @Inject
    UserManager userManager;

    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.progresBar)
    ProgressBar progressBar;
    @BindView(R.id.host_profile_content_holder)
    LinearLayout contentHolder;
    @BindView(R.id.host_profile_image)
    ImageView image;
//    @BindView(R.id.host_profile_title)
//    TextView title;
    @BindView(R.id.host_profile_time)
    TextView workingTime;
    @BindView(R.id.host_profile_facebook)
    TextView facebook;
    @BindView(R.id.host_profile_address)
    TextView address;
    @BindView(R.id.host_profile_text)
    TextView text;
    @BindView(R.id.host_profile_web)
    TextView web;
    @BindView(R.id.host_profile_phone)
    TextView phone;

    @BindView(R.id.awesome_home)
    TextView awesomeHome;
    @BindView(R.id.awesome_calendar)
    TextView awesomeCalendar;
    @BindView(R.id.awesome_clock)
    TextView awesomeClock;
    @BindView(R.id.awesome_facebook)
    TextView awesomeFaceboot;
    @BindView(R.id.awesome_host)
    TextView awesomeHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rx.Observable<List<User>> userStream = userManager.getUsers();
        userStream.subscribe();
//        collapsingToolbarLayout.setTitle(event.getTitle());
//        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
//        toolbar.setTitle(event.getTitle());
//        Picasso.with(this)
//                .load(event.getImage())
//                .resize(380, 380)
//                .centerCrop()
//                .into(image);

        Typeface iconFont = FontManager.getFontAwesome(getApplicationContext());
        FontManager.markAsIconContainer(awesomeClock, iconFont);
        FontManager.markAsIconContainer(awesomeHome, iconFont);
        FontManager.markAsIconContainer(awesomeCalendar, iconFont);
        FontManager.markAsIconContainer(awesomeFaceboot, iconFont);
        FontManager.markAsIconContainer(awesomeHost, iconFont);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.attachView(this);
        showLoading(true);

//        this.title.setText(user.getUsername());
//        this.text.setText(user.getDescription());
//        this.workingTime.setText(user.getWorkingTime());
//        this.address.setText(user.getAddress());
//        this.facebook.setText(user.getFacebook());
//        this.web.setText(user.getWeb());
//        this.phone.setText(user.getPhone());
//        this.image.setImageAlpha(user.getImage());

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

//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//        LatLng latlong = new LatLng(latitude, longitude);
//        Marker marker = mMap.addMarker(new MarkerOptions().position(latlong).title(locationTitle).snippet(locationCategory));
//        marker.showInfoWindow();
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlong,17));
//        mMap.setMyLocationEnabled(true);
//    }
//    @Override
//    public void onItemClicked(Object item) {
//
//    }

    @Override
    protected User getUser() {
        return user;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_host_profile;
    }

    @Override
    protected void setupActivityComponent() {
        MainApplication.get(this).getUserComponent()
                .plus(new HostProfileActivityModule(this))
                .inject(this);
    }

    @Override
    public void onItemClicked(Object item) {

    }
}
