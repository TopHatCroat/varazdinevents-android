package hr.foi.varazdinevents.places.eventDetails;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import hr.foi.varazdinevents.R;
import hr.foi.varazdinevents.models.Event;
import hr.foi.varazdinevents.models.User;
import hr.foi.varazdinevents.ui.base.BasePresenter;
import hr.foi.varazdinevents.util.FontManager;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

import static hr.foi.varazdinevents.util.Constants.PERMISSION_ACCESS_FINE_LOCATION_REQUEST;

/**
 * Created by Antonio Martinović on 08.11.16.
 */
public class EventDetailsPresenter extends BasePresenter<EventDetailsActivity> implements OnMapReadyCallback {

    private User user;
    private double latitude;
    private double longitude;
    private String locationTitle;
    private String locationCategory;
    private GoogleMap map;
    private int counter = 0;
    private String parsedEventDescription;
    private User hostData;


    public EventDetailsPresenter(User user) {
        this.user = user;
    }


    public void itemFavorited() {

    }

    public Observable<Void> parseEventData(final Event event) {
        this.counter = 0;
        SupportMapFragment mapFragment = (SupportMapFragment) getViewLayer().getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        return Observable.defer(new Func0<Observable<Void>>() {
            @Override
            public Observable<Void> call() {
                return Observable.just(parseEvent(event));
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Void parseEvent(Event event) {
        //Google map information
        this.hostData = Select.from(User.class)
                .where(Condition.prop("USERNAME").eq(event.getHost()))
                .first();
        String location = hostData.getAddress();
        Geocoder geocoder = new Geocoder(getViewLayer());
        List<Address> addressList;
        try {
            addressList = geocoder.getFromLocationName(location, 1);
            if (addressList.size() != 0) {
                Address address = addressList.get(0);
                this.latitude = address.getLatitude();
                this.longitude = address.getLongitude();
            } else {
                this.latitude = 46.307819;
                this.longitude = 16.338159;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Date eventDate = new Date(event.getDate() - 3600000);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        this.parsedEventDescription = FontManager.parseHTML(event.getText());

        this.locationTitle = event.getTitle();
        this.locationCategory = event.getCategory() + " - " + dateFormat.format(eventDate) + " u " + timeFormat.format(eventDate) + " sati";

        return null;
    }

    public void resolveMapPosition() {
        this.counter += 1;
        if (this.counter >= 2) {
            LatLng latlong = new LatLng(getLatitude(), getLongitude());
            Marker marker = getMap().addMarker(new MarkerOptions().position(latlong).title(locationTitle).snippet(locationCategory));
            marker.showInfoWindow();
            getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(latlong, 17));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        setMap();
    }

    public GoogleMap getMap() {
        return map;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getParsedEventDescription() {
        return parsedEventDescription;
    }

    public void setMap() {
        if (ActivityCompat.checkSelfPermission(getViewLayer(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getViewLayer(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getViewLayer(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_ACCESS_FINE_LOCATION_REQUEST);
            return;
        } else {
            getMap().setMyLocationEnabled(true);
            resolveMapPosition();
            getViewLayer().mapContainer.setVisibility(View.VISIBLE);
        }
    }
}
