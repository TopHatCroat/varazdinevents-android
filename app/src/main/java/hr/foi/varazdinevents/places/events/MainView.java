package hr.foi.varazdinevents.places.events;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;

import com.google.common.collect.ImmutableList;

import javax.inject.Inject;

import butterknife.BindView;
import dagger.multibindings.IntKey;
import hr.foi.varazdinevents.MainApplication;
import hr.foi.varazdinevents.R;
import hr.foi.varazdinevents.models.Event;
import hr.foi.varazdinevents.ui.base.BaseActivity;
import hr.foi.varazdinevents.ui.base.BaseView;
import hr.foi.varazdinevents.ui.elements.ItemListAdapter;
import hr.foi.varazdinevents.ui.elements.ItemRecyclerView;
import hr.foi.varazdinevents.ui.elements.ItemViewHolder;
import hr.foi.varazdinevents.ui.elements.OnStartDragListener;
import hr.foi.varazdinevents.ui.elements.SimpleItemTouchHelperCallback;

/**
 * Created by Antonio Martinović on 12.10.16.
 */

public class MainView extends BaseView implements MainViewLayer, OnStartDragListener {
    @Inject
    MainPresenter presenter;
    @Inject
    ItemListAdapter itemListAdapter;
    @BindView(R.id.item_recycler_view)
    ItemRecyclerView recyclerView;
    @BindView(R.id.progresBar)
    ProgressBar progressBar;


    ItemTouchHelper itemTouchHelper;

    public MainView(Context context) {
        this(context, null);
    }

    public MainView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MainView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ((MainActivity) context).getActivityComponent().inject(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        presenter.attachView(this);
        showLoading(true);
        presenter.loadEvents();
    }

    @Override
    public void onDetachedFromWindow() {
        presenter.detachView();
        super.onDetachedFromWindow();
    }

    public void showLoading(boolean loading) {
        recyclerView.setVisibility(loading ? View.GONE : View.VISIBLE);
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

    public void showEvents(ImmutableList<Event> events) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

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
        //TODO: do something
    }
}
