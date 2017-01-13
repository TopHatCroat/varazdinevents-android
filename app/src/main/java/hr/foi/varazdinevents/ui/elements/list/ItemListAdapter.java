package hr.foi.varazdinevents.ui.elements.list;

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
    protected ListListener listener;
    protected Map<Integer, ItemViewHolderFactory> viewHolderFactoryMap;
    protected List<T> items;

    public ItemListAdapter(ListListener listener, Map<Integer, ItemViewHolderFactory> itemViewHolderFactoryMap) {
        this.listener = listener;
        this.viewHolderFactoryMap = itemViewHolderFactoryMap;
        this.items = new ArrayList<>();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final ItemViewHolder itemViewHolder = viewHolderFactoryMap.get(viewType).createViewHolder(parent);
        itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClicked(itemViewHolder.getAdapterPosition());
            }
        });
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        holder.bind(items.get(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();
    }

    @SuppressWarnings("unchecked")
    public void setItems(List<T> items){
        this.items.clear();
        Collections.sort(items);
        this.items.addAll(items);

        notifyDataSetChanged();
    }

    @Override
    public void onItemClicked(int adapterPosition) {
        listener.onItemClick(items.get(adapterPosition));
    }

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

    @Override
    public void onItemDismiss(int position) {
        items.remove(items.get(position));
        notifyItemRemoved(position);
    }

    //next 3 are used manipulate models for animation
    public T removeItem(int position){
        final T event = items.remove(position);
        notifyItemRemoved(position);
        return event;
    }

    public void addItem(T item, int position){
        items.add(position, item);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition){
        final T item = items.remove(fromPosition);
        items.add(toPosition, item);
        notifyItemMoved(fromPosition, toPosition);
    }


    public void animateTo(List<T> items) {
        applyAndAnimateRemovals(items);
        applyAndAnimateAdditions(items);
        applyAndAnimateMovedItems(items);
    }

    private void applyAndAnimateRemovals(List<T> newEvents) {
        for(int i = items.size() - 1; i >= 0; i--){
            final T event = items.get(i);
            if(!newEvents.contains(event)){
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<T> newEvents){
        for(int i = 0, count = newEvents.size(); i < count; i++){
            final T event = newEvents.get(i);
            if(!items.contains(event)){
                addItem(event, i);
            }
        }
    }

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
