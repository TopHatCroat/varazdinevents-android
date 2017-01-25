package hr.foi.varazdinevents.ui.elements.list;

import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import hr.foi.varazdinevents.ui.elements.ItemTouchHelperAdapter;
import hr.foi.varazdinevents.ui.elements.ItemViewHolderFactory;
import hr.foi.varazdinevents.ui.elements.Searchable;

/**
 * Created by Antonio Martinović on 14.10.16.
 */
public class ItemListAdapter<T extends Listable & Searchable> extends RecyclerView.Adapter<ItemViewHolder>
        implements ItemTouchHelperAdapter {
    protected ListListener<T> listener;
    protected Map<Integer, ItemViewHolderFactory> viewHolderFactoryMap;
    protected List<T> items;

    public ItemListAdapter(ListListener listener, Map<Integer, ItemViewHolderFactory> itemViewHolderFactoryMap) {
        this.listener = listener;
        this.viewHolderFactoryMap = itemViewHolderFactoryMap;
        this.items = new ArrayList<>();
    }

    /**
     * Creates view holder
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final ItemViewHolder itemViewHolder = viewHolderFactoryMap.get(viewType).createViewHolder(parent);
        itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClicked(itemViewHolder.getAdapterPosition(), itemViewHolder.getAnimationTarget());
            }
        });
        return itemViewHolder;
    }

    /**
     * Binds item to the view holder
     * @param holder new or existing view holder
     * @param position item position
     */
    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        holder.bind(items.get(holder.getAdapterPosition()));
    }

    /**
     * @return number of items in the view
     */
    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * Determines the type of view that should be displayed for the item
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();
    }

    /**
     * Sets new items for adapter
     * @param items new items
     */
    @SuppressWarnings("unchecked")
    public void setItems(List<T> items){
        this.items.clear();
        Collections.sort(items);
        this.items.addAll(items);

        notifyDataSetChanged();
    }

    /**
     * Called when user clicks on item, calles listener method on onClicked
     * @param adapterPosition item position
     * @param view animation target for the item
     */
    @Override
    public void onItemClicked(int adapterPosition, View view) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && view != null) {
            listener.onItemClick(items.get(adapterPosition), view);
        } else {
            listener.onItemClick(items.get(adapterPosition));
        }

    }

    /**
     * Called when item gets moved on the list
     * @param fromPosition beginning position
     * @param toPosition ending position
     */
    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        return false;
//        if(fromPosition < toPosition) {
//            for(int i = fromPosition; i < toPosition; i++) {
//                Collections.swap(items, i, i - 1);
//            }
//        } else {
//            for(int i = fromPosition; i > toPosition; i--) {
//                Collections.swap(items, i, i - 1);
//            }
//        }
//
//        notifyItemMoved(fromPosition, toPosition);
//        return true;
    }

    /**
     * Called when item gets removed from the list
     * @param position removed item position
     */
    @Override
    public void onItemDismiss(int position) {
        items.remove(items.get(position));
        notifyItemRemoved(position);
    }

    /**
     * Triggers remove animation
     * @param position removed item position
     */
    public T removeItem(int position){
        final T event = items.remove(position);
        notifyItemRemoved(position);
        return event;
    }

    /**
     * Triggers add animation
     * @param item new item
     * @param position new item position
     */
    public void addItem(T item, int position){
        items.add(position, item);
        notifyItemInserted(position);
    }

    /**
     * Triggers move animation
     * @param fromPosition
     * @param toPosition
     */
    public void moveItem(int fromPosition, int toPosition){
        final T item = items.remove(fromPosition);
        items.add(toPosition, item);
        notifyItemMoved(fromPosition, toPosition);
    }

    /**
     * Animates the whole list of items
     * @param items new items
     */
    public void animateTo(List<T> items) {
        applyAndAnimateRemovals(items);
        applyAndAnimateAdditions(items);
        applyAndAnimateMovedItems(items);
    }

    /**
     * Iterates over the removed items and triggers their animations
     * @param newEvents removed events
     */
    private void applyAndAnimateRemovals(List<T> newEvents) {
        for(int i = items.size() - 1; i >= 0; i--){
            final T event = items.get(i);
            if(!newEvents.contains(event)){
                removeItem(i);
            }
        }
    }

    /**
     * Iterates over the added items and triggers their animations
     * @param newEvents added events
     */
    private void applyAndAnimateAdditions(List<T> newEvents){
        for(int i = 0, count = newEvents.size(); i < count; i++){
            final T event = newEvents.get(i);
            if(!items.contains(event)){
                addItem(event, i);
            }
        }
    }

    /**
     * Iterates over the moved items and triggers their animations
     * @param newEvents moved events
     */
    private void applyAndAnimateMovedItems(List<T> newEvents){
        for(int toPosition = newEvents.size() - 1; toPosition >= 0; toPosition--){
            final T event = newEvents.get(toPosition);
            final int fromPosition = items.indexOf(event);
            if(fromPosition >= 0 && fromPosition != toPosition){
                moveItem(fromPosition, toPosition);
            }
        }
    }
}
