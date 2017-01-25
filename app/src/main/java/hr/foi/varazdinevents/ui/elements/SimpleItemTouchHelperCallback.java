package hr.foi.varazdinevents.ui.elements;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Manages touch events on Recycler View
 * Created by Antonio Martinović on 15.10.16.
 */
public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {
    private final ItemTouchHelperAdapter adapter;

    public SimpleItemTouchHelperCallback(ItemTouchHelperAdapter adapter) {
        this.adapter = adapter;
    }

    /**
     * Used to determine what touch events are supported
     * @param recyclerView view that receives events
     * @param viewHolder view holder that recieves events
     * @return combined flags of allowed touch event
     */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
//        return makeMovementFlags(dragFlags, swipeFlags);
        return 0;
    }

    /**
     * Gets called on move
     * @param recyclerView
     * @param viewHolder
     * @param target
     * @return
     */
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        adapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    /**
     * Gets called on swipe
     * @param viewHolder
     * @param direction
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        adapter.onItemDismiss(viewHolder.getAdapterPosition());
    }

    /**
     * Should be overridden to change default value
     * @return
     */
    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    /**
     * Should be overridden to change default value
     * @return
     */
    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }
}
