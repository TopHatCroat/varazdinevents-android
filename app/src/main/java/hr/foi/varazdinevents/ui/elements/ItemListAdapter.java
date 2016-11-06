package hr.foi.varazdinevents.ui.elements;

import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import hr.foi.varazdinevents.R;
import hr.foi.varazdinevents.models.Event;
import hr.foi.varazdinevents.ui.base.PresenterLayer;
import hr.foi.varazdinevents.ui.base.ViewLayer;

/**
 * Created by Antonio Martinović on 14.10.16.
 */
public class ItemListAdapter extends RecyclerView.Adapter<ItemViewHolder> implements ItemTouchHelperAdapter {
    protected PresenterLayer viewLayer;
    protected Map<Integer, ItemViewHolderFactory> viewHolderFactoryMap;
    protected List<Object> items;

    public ItemListAdapter(PresenterLayer viewLayer, List<Object> items, Map<Integer, ItemViewHolderFactory> itemViewHolderFactoryMap) {
        this.viewLayer = viewLayer;
        this.viewHolderFactoryMap = itemViewHolderFactoryMap;
        this.items = items;
    }

    public ItemListAdapter(PresenterLayer viewLayer, Map<Integer, ItemViewHolderFactory> itemViewHolderFactoryMap) {
        this.viewLayer = viewLayer;
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
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return ((Listable) items.get(position)).getType();
    }

    public void setItems(ImmutableList<Event> items){
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public void onItemClicked(int adapterPosition) {
        viewLayer.itemClicked(items.get(adapterPosition));
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if(fromPosition < toPosition) {
            for(int i = fromPosition; i < toPosition; i++) {
                Collections.swap(items, i, i - 1);
            }
        } else {
            for(int i = fromPosition; i > toPosition; i--) {
                Collections.swap(items, i, i - 1);
            }
        }

        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

}
