package hr.foi.varazdinevents.ui;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;

import javax.inject.Inject;

import butterknife.BindView;
import hr.foi.varazdinevents.R;
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
    @BindView(R.id.item_recycler_view)
    ItemRecyclerView recyclerView;

    ItemListAdapter itemListAdapter;


    ItemTouchHelper itemTouchHelper;

    public MainView(Context context) {
        this(context, null);
    }

    public MainView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MainView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ((BaseActivity) context).getActivityComponent().inject(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        presenter.attachView(this);
        presenter.loadEvents();
        showEvents();

    }

    @Override
    public void onDetachedFromWindow() {
        presenter.detachView();
        super.onDetachedFromWindow();
    }

    @Override
    public void showEvents() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ItemListAdapter<String, ItemViewHolder> itemListAdapter = new ItemListAdapter(this);
        recyclerView.setAdapter(itemListAdapter);

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
