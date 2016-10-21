package hr.foi.varazdinevents.ui.elements;

import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import hr.foi.varazdinevents.R;

/**
 * Created by Antonio Martinović on 14.10.16.
 */
public class ItemListAdapter<T, H extends ItemViewHolder> extends RecyclerView.Adapter<H> implements ItemTouchHelperAdapter {
    private static final String[] STRINGS = new String[]{
            "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten"
    };

    private final List<T> items = new ArrayList<>();
    private final OnStartDragListener startDragListener;
    public ItemListAdapter(OnStartDragListener startDragListener) {
        this.items.addAll((Collection<? extends T>) Arrays.asList(STRINGS));
        this.startDragListener = startDragListener;
    }

    @Override
    public H onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getItemLayout(), parent, false);
        return (H) new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final H holder, int position) {
        holder.bind();
        holder.textView.setText((String) items.get(position));
        //TODO: make generic, call this method to set the data

        holder.handle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) ==
                        MotionEvent.ACTION_DOWN) {
                    startDragListener.onStartDrag(holder);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
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

    public int getItemLayout() {
        return R.layout.item_main;
    }
}
