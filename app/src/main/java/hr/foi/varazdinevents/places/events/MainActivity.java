package hr.foi.varazdinevents.places.events;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.GravityCompat;
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
import android.widget.Button;
import android.widget.ProgressBar;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import butterknife.BindView;
import hr.foi.varazdinevents.MainApplication;
import hr.foi.varazdinevents.R;
import hr.foi.varazdinevents.api.UserManager;
import hr.foi.varazdinevents.injection.modules.MainActivityModule;
import hr.foi.varazdinevents.models.Event;
import hr.foi.varazdinevents.models.User;
import hr.foi.varazdinevents.places.eventDetails.EventDetailsActivity;
import hr.foi.varazdinevents.ui.base.BaseNavigationActivity;
import hr.foi.varazdinevents.ui.elements.list.ItemAnimator;
import hr.foi.varazdinevents.ui.elements.list.ItemListAdapter;
import hr.foi.varazdinevents.ui.elements.OnStartDragListener;
import hr.foi.varazdinevents.ui.elements.SimpleItemTouchHelperCallback;

import static hr.foi.varazdinevents.util.Constants.LIST_STATE_KEY;

/**
 * Class for Main activity which shows a list of upcoming events
 */
public class MainActivity extends BaseNavigationActivity implements MainViewLayer, OnStartDragListener,
        SearchView.OnQueryTextListener {

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
    private Parcelable listState;
    CallbackManager callbackManager;

    /**
     * Creates "Main" activity, loads events into presenter
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
        if (this.events.isEmpty()){
            presenter.loadEvents();
        } else {
            animateIn();
        }
        presenter.loadUsers();
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
            } catch (Exception e) {}
        }
    }

    /**
     * Detaches View from presenter
     */
    @Override
    protected void onStop(){
        super.onStop();
        presenter.detachView();
    }

    /**
     * Shows loading animation
     * @param loading
     */
    public void showLoading(boolean loading) {
        if(loading) {
            swipeRefreshLayout.setRefreshing(true);
        } else {
            swipeRefreshLayout.setRefreshing(false);
            animateIn();
        }
    }

    /**
     * Shows a list of upcoming events on main screen
     * @param events new events to show
     */
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
//        recyclerView.setItemAnimator(new ItemAnimator());
    }

    /**
     * Registers on screen drag
     * @param viewHolder
     */
    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }

    /**
     * Shows input box if "Search" is clicked, sets listener for query change
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
     * @param menuItem
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        List<Event> filteredModelList;
        switch (menuItem.getItemId()) {
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
            default: filteredModelList = null;
        }
        eventListAdapter.animateTo(filteredModelList);
        recyclerView.scrollToPosition(0);
        return true;
    }

    /**
     * Calls method for event filtering based on inputted query
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

    /**
     * Starts "Main" activity
     * @param startingActivity
     */
    public static void start(Context startingActivity) {
        Intent intent = new Intent(startingActivity, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startingActivity.startActivity(intent);
    }

    @Override
    protected User getUser() {
        return user;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public void setUser(User user) { this.user = user;}

    /**
     * Filters list of all events based on inputted query
     * @param query
     * @return Filtered list based on query (key word)
     */
    private List<Event> filter(String query) {
        query = query.toLowerCase();

        final List<Event> filteredList = new ArrayList<>();
        for(Event event : this.events){
            if(event.isMatching(query))
                filteredList.add(event);
        }
        return filteredList;
    }

    /**
     * Shows all events without filters
     * @return All events
     */
    private List<Event> showAll(){
        final List<Event> filteredList = new ArrayList<>();
        for(Event event : this.events){
            filteredList.add(event);
        }
        return filteredList;
    }

    /**
     * Filters all events which are favorited
     * @return List of favorite events
     */
    private List<Event> filterFavorite(){
        final List<Event> filteredList = new ArrayList<>();
        for(Event event : this.events){
            if(event.isFavorite)
                filteredList.add(event);
        }
        return filteredList;
    }

    /**
     * Filters all events based on chosen category
     * @param stringCategory
     * @return List of events with specific category
     */
    private List<Event> filterCategory(String stringCategory){
        final List<Event> filteredList = new ArrayList<>();
        for(Event event : this.events){
            if(event.category.equals(stringCategory))
                filteredList.add(event);
        }
        return filteredList;
    }

    /**
     * Saves instance state
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
}