package hr.foi.varazdinevents.places.events;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.messaging.RemoteMessage;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Inject;

import butterknife.BindView;
import hr.foi.varazdinevents.MainApplication;
import hr.foi.varazdinevents.R;
import hr.foi.varazdinevents.injection.modules.MainActivityModule;
import hr.foi.varazdinevents.models.Event;
import hr.foi.varazdinevents.models.User;
import hr.foi.varazdinevents.places.eventDetails.EventDetailsActivity;
import hr.foi.varazdinevents.ui.base.BaseActivity;
import hr.foi.varazdinevents.ui.base.BaseNavigationActivity;
import hr.foi.varazdinevents.ui.elements.list.ItemAnimator;
import hr.foi.varazdinevents.ui.elements.list.ItemListAdapter;
import hr.foi.varazdinevents.ui.elements.list.ItemRecyclerView;
import hr.foi.varazdinevents.ui.elements.OnStartDragListener;
import hr.foi.varazdinevents.ui.elements.SimpleItemTouchHelperCallback;

import static hr.foi.varazdinevents.util.Constants.LIST_STATE_KEY;

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
    @BindView(R.id.item_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.progresBar)
    ProgressBar progressBar;
    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeRefreshLayout;

    ItemTouchHelper itemTouchHelper;
    List<Event> events = new ArrayList<>();

    boolean favoriteListChecked = false;
    private Parcelable listState;

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (listState != null) {
            recyclerView.getLayoutManager().onRestoreInstanceState(listState);
        }
    }

    @Override
    protected void onStop(){
        super.onStop();
        presenter.detachView();
    }

    public void showLoading(boolean loading) {
        if(loading) {
            swipeRefreshLayout.setRefreshing(true);
        } else {
            swipeRefreshLayout.setRefreshing(false);
            animateIn();
        }
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
        recyclerView.setItemAnimator(new ItemAnimator());
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        List<Event> filteredModelList;
        switch (menuItem.getItemId()) {
            case R.id.action_all:
                filteredModelList = showAll();
                break;
            case R.id.action_favorite:
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

    public static void start(Context startingActivity) {
        Intent intent = new Intent(startingActivity, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startingActivity.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();

        /*super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        */
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

    private List<Event> filter(String query) {
        query = query.toLowerCase();

        final List<Event> filteredList = new ArrayList<>();
        for(Event event : this.events){
            if(event.isMatching(query))
                filteredList.add(event);
        }
        return filteredList;
    }

    private List<Event> showAll(){
        final List<Event> filteredList = new ArrayList<>();
        for(Event event : this.events){
            filteredList.add(event);
        }
        return filteredList;
    }

    private List<Event> filterFavorite(){
        final List<Event> filteredList = new ArrayList<>();
        for(Event event : this.events){
            if(event.isFavorite)
                filteredList.add(event);
        }
        return filteredList;
    }

    private List<Event> filterCategory(String stringCategory){
        final List<Event> filteredList = new ArrayList<>();
        for(Event event : this.events){
            if(event.category.equals(stringCategory))
                filteredList.add(event);
        }
        return filteredList;
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        if (recyclerView.getLayoutManager() != null) {
            listState = recyclerView.getLayoutManager().onSaveInstanceState();
        }
        state.putParcelable(LIST_STATE_KEY, listState);
    }

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