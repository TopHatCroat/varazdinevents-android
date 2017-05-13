package hr.foi.varazdinevents.places.events;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.facebook.CallbackManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import hr.foi.varazdinevents.MainApplication;
import hr.foi.varazdinevents.R;
import hr.foi.varazdinevents.api.UserManager;
import hr.foi.varazdinevents.injection.modules.MainActivityModule;
import hr.foi.varazdinevents.models.City;
import hr.foi.varazdinevents.models.Event;
import hr.foi.varazdinevents.models.User;
import hr.foi.varazdinevents.ui.base.BaseNavigationActivity;
import hr.foi.varazdinevents.ui.elements.OnStartDragListener;
import hr.foi.varazdinevents.ui.elements.SimpleItemTouchHelperCallback;
import hr.foi.varazdinevents.ui.elements.list.ItemListAdapter;
import hr.foi.varazdinevents.util.Helpers;
import hr.foi.varazdinevents.util.SharedPrefs;

import static hr.foi.varazdinevents.util.Constants.LIST_STATE_KEY;
import static hr.foi.varazdinevents.util.Constants.PERMISSION_ACCESS_FINE_LOCATION_REQUEST;

/**
 * Class for Main activity which shows a list of upcoming events
 */
public class MainActivity extends BaseNavigationActivity implements MainViewLayer, OnStartDragListener,
        SearchView.OnQueryTextListener, PickLocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    @Inject
    MainPresenter presenter;
    @Inject
    ItemListAdapter eventListAdapter;
    @Inject
    LinearLayoutManager linearLayoutManager;
    @Inject
    GridLayoutManager gridLayoutManager;
    @Inject
    User user;
    @Inject
    UserManager userManager;
    @BindView(R.id.item_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeRefreshLayout;

    ItemTouchHelper itemTouchHelper;
    List<Event> events = new ArrayList<>();
    List<User> users = new ArrayList<>();
    boolean favoriteListChecked = false;
    CallbackManager callbackManager;
    PickLocationDialog pickLocationDialog;
    private Parcelable listState;
    private List<City> cities;
    private LocationManager locationManager;
    private GoogleApiClient googleApiClient;

    /**
     * Starts "Main" activity
     *
     * @param startingActivity
     */
    public static void start(Context startingActivity) {
        Intent intent = new Intent(startingActivity, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startingActivity.startActivity(intent);
    }

    /**
     * Creates "Main" activity, loads events into presenter
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadEvents();
            }
        });

        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        new SharedPrefs(this);

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    /**
     * Starts "Main" activity, attaches presenter view, loads events and users
     */
    @Override
    protected void onStart() {
        super.onStart();
        if (listState != null) {
            recyclerView.getLayoutManager().onRestoreInstanceState(listState);
        }
        presenter.attachView(this);
        presenter.loadCities();

        int city = SharedPrefs.read("city", -1);
        if (city == -1) {
            pickLocationDialog = new PickLocationDialog(this, false, null);
            pickLocationDialog.setListener(this);
            pickLocationDialog.show();
        } else {
            initLoad();
        }
        presenter.loadUsers();
    }

    private void initLoad() {
        if (this.events.isEmpty()) {
            presenter.loadEvents();
        } else {
            animateIn();
        }
    }

    /**
     * If list state is not empty, tries to get layout manager from recycler View
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (listState != null) {
            try {
                recyclerView.getLayoutManager().onRestoreInstanceState(listState);
            } catch (Exception e) {
            }
        }
    }

    /**
     * Detaches View from presenter
     */
    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
        presenter.detachView();
    }

    /**
     * Shows loading animation
     *
     * @param loading
     */
    public void showLoading(boolean loading) {
        if (loading) {
            swipeRefreshLayout.setRefreshing(true);
        } else {
            swipeRefreshLayout.setRefreshing(false);
            animateIn();
        }
    }

    /**
     * Shows a list of upcoming events on main screen
     *
     * @param events new events to show
     */
    public void showEvents(List<Event> events) {
        setEvents(events);
        recyclerView.setHasFixedSize(true);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(gridLayoutManager);
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(linearLayoutManager);
        }

        recyclerView.setAdapter(eventListAdapter);
        eventListAdapter.setItems(events);
        ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelperCallback(eventListAdapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
//        recyclerView.setItemAnimator(new ItemAnimator());
    }

    /**
     * Registers on screen drag
     *
     * @param viewHolder
     */
    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }

    /**
     * Shows input box if "Search" is clicked, sets listener for query change
     *
     * @param menu
     * @return True
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

        return true;
    }

    /**
     * Checks which category from the menu was clicked and calls method for category filtering
     *
     * @param menuItem
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        List<Event> filteredModelList = null;
        switch (menuItem.getItemId()) {
            case R.id.action_location:
                setManual();
                break;
            case R.id.action_all:
                filteredModelList = showAll();
                break;
            case R.id.action_favorite:
                showEvents(presenter.refreshEvents());
                filteredModelList = filterFavorite();
                break;
            case R.id.action_theatreAndMovie:
                filteredModelList = filterCategory("Kazalište i film");
                break;
            case R.id.action_backgroundMusic:
                filteredModelList = filterCategory("Slušaona");
                break;
            case R.id.action_volunteering:
                filteredModelList = filterCategory("Volontiranje");
                break;
            case R.id.action_concert:
                filteredModelList = filterCategory("Svirka");
                break;
            case R.id.action_course:
                filteredModelList = filterCategory("Tečaj");
                break;
            case R.id.action_lecture:
                filteredModelList = filterCategory("Predavanje");
                break;
            case R.id.action_party:
                filteredModelList = filterCategory("Party");
                break;
            case R.id.action_other:
                filteredModelList = filterCategory("Ostalo");
                break;
            default:
                filteredModelList = null;
        }
        if (filteredModelList != null) {
            eventListAdapter.animateTo(filteredModelList);
            recyclerView.scrollToPosition(0);
        }
        return true;
    }

    /**
     * Calls method for event filtering based on inputted query
     *
     * @param query
     * @return True
     */
    @Override
    public boolean onQueryTextChange(String query) {
        final List<Event> filteredModelList = filter(query);
        eventListAdapter.animateTo(filteredModelList);
        recyclerView.scrollToPosition(0);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void setupActivityComponent() {
        MainApplication.get(this).getUserComponent()
                .plus(new MainActivityModule(this))
                .inject(this);

    }

    @Override
    protected User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    /**
     * Filters list of all events based on inputted query
     *
     * @param query
     * @return Filtered list based on query (key word)
     */
    private List<Event> filter(String query) {
        query = query.toLowerCase();

        final List<Event> filteredList = new ArrayList<>();
        for (Event event : this.events) {
            if (event.isMatching(query))
                filteredList.add(event);
        }
        return filteredList;
    }

    /**
     * Shows all events without filters
     *
     * @return All events
     */
    private List<Event> showAll() {
        final List<Event> filteredList = new ArrayList<>();
        for (Event event : this.events) {
            filteredList.add(event);
        }
        return filteredList;
    }

    /**
     * Filters all events which are favorited
     *
     * @return List of favorite events
     */
    private List<Event> filterFavorite() {
        final List<Event> filteredList = new ArrayList<>();
        for (Event event : this.events) {
            if (event.isFavorite)
                filteredList.add(event);
        }
        return filteredList;
    }

    /**
     * Filters all events based on chosen category
     *
     * @param stringCategory
     * @return List of events with specific category
     */
    private List<Event> filterCategory(String stringCategory) {
        final List<Event> filteredList = new ArrayList<>();
        for (Event event : this.events) {
            if (event.category.equals(stringCategory))
                filteredList.add(event);
        }
        return filteredList;
    }

    /**
     * Saves instance state
     *
     * @param state
     */
    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        if (recyclerView.getLayoutManager() != null) {
            listState = recyclerView.getLayoutManager().onSaveInstanceState();
        }
        state.putParcelable(LIST_STATE_KEY, listState);
    }

    /**
     * Restores instance state
     *
     * @param state
     */
    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);

        listState = state.getParcelable(LIST_STATE_KEY);
    }

    public void animateOut() {
//        recyclerView.animate().alpha(0f).setDuration(500);
    }

    public void animateIn() {
//        recyclerView.setAlpha(1f);
    }

    @Override
    public void setLocation(Location location) {
        Double minDist = Double.MAX_VALUE;
        int pos = -1;

        for (int i = 0; i < cities.size(); i++) {
            double distance = Helpers.distance(location.getLatitude(), cities.get(i).getLatitude(),
                    location.getLongitude(), cities.get(i).getLongitude());

            if (distance < minDist) {
                minDist = distance;
                pos = i;
            }
        }

        SharedPrefs.write("city", cities.get(pos).getApiId());
        initLoad();
    }

    @Override
    public void setManual() {
        String[] stockArr = new String[cities.size()];
        for (int i = 0; i < cities.size(); i++) {
            stockArr[i] = cities.get(i).getTitle();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.pick_location)
                .setItems(stockArr, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPrefs.write("city", cities.get(which).getApiId());
                        dialog.dismiss();
                        presenter.loadEvents();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void requestPermission() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_ACCESS_FINE_LOCATION_REQUEST);
        } else {
            googleApiClient.connect();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_FINE_LOCATION_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    requestPermission();
                } else {
                    showBasicError("No party for you");
                }
                return;
            }
        }
    }

    public void showLocationPicker(List<City> cities) {
        this.cities = cities;
        if (pickLocationDialog != null)
            pickLocationDialog.setLoading(false);
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        pickLocationDialog.dismiss();
        setLocation(lastLocation);
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}