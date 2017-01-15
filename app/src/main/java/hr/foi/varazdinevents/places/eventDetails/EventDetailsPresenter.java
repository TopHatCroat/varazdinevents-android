package hr.foi.varazdinevents.places.eventDetails;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.ActivityCompat;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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


    public EventDetailsPresenter(User user) {
        this.user = user;
    }


    public void itemFavorited(){

    }

    public Observable<Void> parseEventData(final Event event, final TextView textView) {
        SupportMapFragment mapFragment = (SupportMapFragment) getViewLayer().getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        return Observable.defer(new Func0<Observable<Void>>() {
            @Override
            public Observable<Void> call() {
                return Observable.just(parseEvent(event, textView));
            }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
    }

    private Void parseEvent(Event event, TextView textView) {
        //Google map information
        String location = "Pavlinska 2, 42000 Varaždin";
        Geocoder geocoder = new Geocoder(getViewLayer());
        List<Address> addressList;
        try {
            addressList = geocoder.getFromLocationName(location, 1);
            if (addressList.size()!=0) {
                Address address = addressList.get(0);
                this.latitude = address.getLatitude();
                this.longitude = address.getLongitude();
            }else {
                this.latitude = 46.307819;
                this.longitude = 16.338159;
            }
            resolveMapPosition();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Date eventDate = new Date(event.getDate() - 3600000);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        textView.setText(FontManager.parseHTML(event.getText()));

        this.locationTitle = event.getTitle();
        this.locationCategory = event.getCategory() + " - " + dateFormat.format(eventDate) + " u " + timeFormat.format(eventDate) + " sati";

        return null;
    }

    private void resolveMapPosition() {
        this.counter += 1;
        if(this.counter % 2 == 0) {
            LatLng latlong = new LatLng(getLatitude(), getLongitude());
            Marker marker = getMap().addMarker(new MarkerOptions().position(latlong).title(locationTitle).snippet(locationCategory));
            marker.showInfoWindow();
            getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(latlong, 17));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        if (ActivityCompat.checkSelfPermission(getViewLayer(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                        getViewLayer(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        map.setMyLocationEnabled(true);
        resolveMapPosition();
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
}
