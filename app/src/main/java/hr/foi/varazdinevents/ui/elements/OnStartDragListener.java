package hr.foi.varazdinevents.ui.elements;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Antonio Martinović on 15.10.16.
 */
public interface OnStartDragListener {
    /**
     * Gets called when user starts dragging the item
     * @param viewHolder that got grabbed
     */
    void onStartDrag(RecyclerView.ViewHolder viewHolder);
}
