package hr.foi.varazdinevents.places.events;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ProgressBar;

import com.google.common.collect.ImmutableList;

import javax.inject.Inject;

import butterknife.BindView;
import hr.foi.varazdinevents.MainApplication;
import hr.foi.varazdinevents.R;
import hr.foi.varazdinevents.injection.modules.MainActivityModule;
import hr.foi.varazdinevents.models.Event;
import hr.foi.varazdinevents.models.User;
import hr.foi.varazdinevents.places.eventDetails.EventDetailsActivity;
import hr.foi.varazdinevents.ui.base.BaseActivity;
import hr.foi.varazdinevents.ui.elements.ItemListAdapter;
import hr.foi.varazdinevents.ui.elements.ItemRecyclerView;
import hr.foi.varazdinevents.ui.elements.OnStartDragListener;
import hr.foi.varazdinevents.ui.elements.SimpleItemTouchHelperCallback;

public class MainActivity extends BaseActivity implements MainViewLayer, OnStartDragListener {
//    protected MainActivityComponent mainActivityComponent;

    @Inject
    MainPresenter presenter;
    @Inject
    ItemListAdapter itemListAdapter;
    @Inject
    User user;

    @BindView(R.id.item_recycler_view)
    ItemRecyclerView recyclerView;
    @BindView(R.id.progresBar)
    ProgressBar progressBar;

    ItemTouchHelper itemTouchHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.attachView(this);
        showLoading(true);
        presenter.loadEvents();
    }

    @Override
    protected void onStop(){
        super.onStop();
        presenter.detachView();
    }

    public void showLoading(boolean loading) {
        recyclerView.setVisibility(loading ? View.GONE : View.VISIBLE);
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

    public void showEvents(ImmutableList<Event> events) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(itemListAdapter);
        itemListAdapter.setItems(events);
        ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelperCallback(itemListAdapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onItemClicked(Object item) {
        EventDetailsActivity.startWithEvent((Event)item, this);
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
        startingActivity.startActivity(intent);
    }

    public static void start(Context startingActivity){
        Intent intent = new Intent(startingActivity, MainActivity.class);
        startingActivity.startActivity(intent);
    }

}
