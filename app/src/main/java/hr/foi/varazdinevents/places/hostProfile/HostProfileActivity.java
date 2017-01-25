package hr.foi.varazdinevents.places.hostProfile;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.test.espresso.core.deps.guava.base.Strings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.method.LinkMovementMethod;
import android.transition.Fade;
import android.transition.Slide;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.orm.SugarContext;
import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import hr.foi.varazdinevents.MainApplication;
import hr.foi.varazdinevents.R;
import hr.foi.varazdinevents.api.UserManager;
import hr.foi.varazdinevents.injection.modules.HostProfileActivityModule;
import hr.foi.varazdinevents.models.Event;
import hr.foi.varazdinevents.models.User;
import hr.foi.varazdinevents.places.eventDetails.EventDetailsActivity;
import hr.foi.varazdinevents.ui.base.BaseNavigationActivity;
import hr.foi.varazdinevents.ui.elements.SimpleItemTouchHelperCallback;
import hr.foi.varazdinevents.ui.elements.list.ItemListAdapter;
import hr.foi.varazdinevents.ui.elements.list.ItemRecyclerView;
import hr.foi.varazdinevents.util.Constants;
import hr.foi.varazdinevents.util.FontManager;

/**
 * Created by Valentin Magdić on 26.12.16..
 */

/**
 * Contains methods related to host's profile.
 * Shows host profile's details, host's location on google maps and list of upcoming events
 */
public class HostProfileActivity extends BaseNavigationActivity implements OnMapReadyCallback {
    private static final String ARG_EVENT = "arg_event";
    private GoogleMap mMap;
    private User host;
    private String hostDescription;
    Double latitude, longitude;
    String locationTitle, locationCategory;
    @Inject
    User user;
    @Inject
    HostProfilePresenter presenter;
    @Inject
    UserManager userManager;

    @Nullable
    @BindView(R.id.item_recycler_view)
    ItemRecyclerView recyclerView;
    @Inject
    GridLayoutManager gridLayoutManager;
    @Inject
    LinearLayoutManager linearLayoutManager;
    @Inject
    ItemListAdapter eventListAdapter;
    ItemTouchHelper itemTouchHelper;

    List<Event> events = new ArrayList<>();


    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.progressBar)
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
    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;

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


    /**
     * Creates "Host Profile" activity.
     * Assigns selected host from database, changes toolbar title to host name title,
     * loads host's image if existent, pre loads icons
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String hostname = getIntent().getStringExtra("hostname");
        this.host = Select.from(User.class)
                .where(Condition.prop("USERNAME").eq(hostname))
                .first();

        collapsingToolbarLayout.setTitle(host.getUsername());
        toolbar.setTitle(host.getUsername());

        if (host.getType() != Constants.EVENTS_NO_IMAGE_CARD) {
            collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

            Picasso.with(this)
                    .load(host.getImage())
                    .resize(380, 380)
                    .centerCrop()
                    .into(image);
        } else {
            appBarLayout.setExpanded(false);
            AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) collapsingToolbarLayout.getLayoutParams();
            params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP | AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
        }

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

    /**
     * Starts "Host Profile" activity.
     * Gets and sets host profile's details
     */
    @Override
    protected void onStart() {
        super.onStart();
        presenter.attachView(this);
        showLoading(true);

//        this.title.setText(user.getUsername());
        if (host.getDescription().equals("")) {
            this.text.setText("N/A");
        }
        else{
            this.text.setText(FontManager.parseHTML(host.getDescription()));
        }

        if (host.getWorkingTime().equals("")) {
            this.workingTime.setText("N/A");
        } else this.workingTime.setText(host.getWorkingTime());

        if (host.getAddress().equals("")) {
            this.address.setText("N/A");
        } else this.address.setText(host.getAddress());

        if (host.getFacebook().equals("")) {
            this.facebook.setText("N/A");
        }

        if (host.getWeb().equals("")) {
            this.web.setText("N/A");
        }

        if (host.getPhone().equals("")) {
            this.phone.setText("N/A");
        } else this.phone.setText(host.getPhone());


        //Google map information
        String location = host.getAddress();
        Geocoder geocoder = new Geocoder(this);
        List<Address> addressList;
        try {
            addressList = geocoder.getFromLocationName(location, 1);
            if (addressList.size() != 0) {
                Address address = addressList.get(0);
                this.latitude = address.getLatitude();
                this.longitude = address.getLongitude();
            } else {
                this.latitude = 46.309079;
                this.longitude = 16.347674;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.locationTitle = host.getUsername();
        this.locationCategory = location;

        if (events.size() == 0) presenter.loadEvents();
        showLoading(false);
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.detachView();
    }

    /**
     * Toggles progress bar's and content holder's visibility on and off if loading
     * @param loading
     */
    public void showLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        contentHolder.setVisibility(loading ? View.GONE : View.VISIBLE);
    }

    public static void startWithEvent(Event event, Context startingActivity) {
        Intent intent = new Intent(startingActivity, EventDetailsActivity.class);
        intent.putExtra(ARG_EVENT, event);
        startingActivity.startActivity(intent);
    }

    /**
     * Adds marker of host's location on google map
     * @param googleMap
     */
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

    public void onItemClicked(Object item) {
        EventDetailsActivity.startWithEvent((Event) item, this);
    }

    /**
     * Shows list of upcoming events which are hosted by the selected host
     * @param events
     */
    public void showEvents(List<Event> events) {
        List<Event> hostsEvents = filterHostEvents(events);

        setEvents(hostsEvents);
        recyclerView.setHasFixedSize(true);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(gridLayoutManager);
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(linearLayoutManager);
        }

        recyclerView.setAdapter(eventListAdapter);
        eventListAdapter.setItems(hostsEvents);
        ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelperCallback(eventListAdapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    /**
     * Filters all events list to match a single host
     * @param events
     * @return Filtered list of host's events
     */
    private List<Event> filterHostEvents(List<Event> events) {
        final List<Event> filteredList = new ArrayList<>();
        for (Event event : events) {
            if (event.host.equals(host.getUsername()))
                filteredList.add(event);
        }
        return filteredList;
    }

    /**
     * Starts new activity if the underlined Facebook text in host's details is clicked.
     */
    @OnClick(R.id.host_profile_facebook)
    public void onFacebookClicked() {
        if(!Strings.isNullOrEmpty(host.getFacebook())) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(host.getFacebook()));
            startActivity(intent);
        }
    }

    /**
     * Starts new activity if the underlined website text in host's details is clicked.
     * Opens host's website in default mobile browser.
     */
    @OnClick(R.id.host_profile_web)
    public void onWebClicked() {
        if(!Strings.isNullOrEmpty(host.getWeb())) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(host.getWeb()));
            startActivity(intent);
        }
    }

    /**
     * Registers click on host's phone number, automatically opens phone number in
     * mobile's default calling application
     */
    @OnClick(R.id.host_profile_phone)
    public void onPhoneClicked() {
        if (!Strings.isNullOrEmpty(host.getPhone())) {
            new AlertDialog.Builder(this)
                    .setMessage(R.string.callHost)
                    .setCancelable(false)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(Uri.parse("tel:" + host.getPhone()));
                            try {
                                startActivity(intent);
                            } catch (android.content.ActivityNotFoundException ex) {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.event_create_failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .setNegativeButton(R.string.no, null)
                    .show();
        }
    }
}
