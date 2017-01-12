package hr.foi.varazdinevents.places.hostProfile;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.transition.Fade;
import android.transition.Slide;
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
import java.util.ArrayList;
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
import hr.foi.varazdinevents.ui.elements.SimpleItemTouchHelperCallback;
import hr.foi.varazdinevents.ui.elements.list.ItemListAdapter;
import hr.foi.varazdinevents.ui.elements.list.ItemRecyclerView;
import hr.foi.varazdinevents.util.FontManager;
import rx.Observer;

import static hr.foi.varazdinevents.util.Constants.ARG_EVENT;

/**
 * Created by Valentin Magdić on 26.12.16..
 */

public class HostProfileActivity extends BaseNavigationActivity implements OnMapReadyCallback {
    private static final String ARG_EVENT = "arg_event";
    private GoogleMap mMap;
    private Event event;
    Double latitude, longitude;
    String locationTitle, locationCategory;
    @Inject
    User user;
    @Inject
    HostProfilePresenter presenter;
    @Inject
    @Nullable
    Fade animation;
    @Inject
    UserManager userManager;

    @BindView(R.id.item_recycler_view)
    ItemRecyclerView recyclerView;
    @Inject
    GridLayoutManager gridLayoutManager;
    @Inject
    LinearLayoutManager linearLayoutManager;
    @Inject
    ItemListAdapter eventListAdapter;
    ItemTouchHelper itemTouchHelper;
    @Inject
    @Nullable
    Slide enterAnimation;
    @Inject
    @Nullable
    Fade returnAnimation;
    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeRefreshLayout;

    List<Event> events = new ArrayList<>();


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

    @BindView(R.id.awesome_web)
    TextView awesomeWeb;
    @BindView(R.id.awesome_phone)
    TextView awesomePhone;
    @BindView(R.id.awesome_clock)
    TextView awesomeClock;
    @BindView(R.id.awesome_facebook)
    TextView awesomeFacebook;
    @BindView(R.id.awesome_address)
    TextView awesomeAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setExitTransition(enterAnimation);
            getWindow().setReturnTransition(returnAnimation);
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadEvents();
            }
        });

        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

//        rx.Observable<List<User>> userStream = userManager.getUsers();
//        userStream.subscribe();
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
        FontManager.markAsIconContainer(awesomeWeb, iconFont);
        FontManager.markAsIconContainer(awesomePhone, iconFont);
        FontManager.markAsIconContainer(awesomeFacebook, iconFont);
        FontManager.markAsIconContainer(awesomeAddress, iconFont);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


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
        String location = "Julija Merlića 9, 42000 Varaždin";
        this.workingTime.setText("0-24");
        this.address.setText(location);
        this.phone.setText("099 12345678");
        this.facebook.setMovementMethod(LinkMovementMethod.getInstance());
        this.web.setMovementMethod(LinkMovementMethod.getInstance());

        //Google map information
        Geocoder geocoder = new Geocoder(this);
        List<Address> addressList;
        try {
            addressList = geocoder.getFromLocationName(location, 1);
            if (addressList.size()!=0) {
                Address address = addressList.get(0);
                this.latitude = address.getLatitude();
                this.longitude = address.getLongitude();
            }else {
                this.latitude = 46.309079;
                this.longitude = 16.347674;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.locationTitle = "VarazdinEvents";
        this.locationCategory = location;

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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng latlong = new LatLng(latitude, longitude);
        Marker marker = mMap.addMarker(new MarkerOptions().position(latlong).title(locationTitle).snippet(locationCategory));
        marker.showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlong, 17));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
    }
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

    public void showEvents(List<Event> events) {
        setEvents(events);
        recyclerView.setHasFixedSize(true);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(gridLayoutManager);
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(linearLayoutManager);
        }

        recyclerView.setAdapter(eventListAdapter);
        eventListAdapter.setItems(events);
        ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelperCallback(eventListAdapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
