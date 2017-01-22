package hr.foi.varazdinevents.ui.elements.list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.Unbinder;

/**
 * Every view holder should extend this class
 * Created by Antonio Martinović on 13.10.16.
 */
public abstract class ItemViewHolder extends RecyclerView.ViewHolder {
    protected Unbinder unbinder;

    public ItemViewHolder(View itemView) {
        super(itemView);
    }

    /**
     * Called when view holder is populated with new data
     * Should fill out the holder data and do other actions required
     * to display the item properly
     * @param item data object
     */
    public abstract void bind(Object item);

    /**
     * Called when the view holder gets destroyed
     */
    public abstract void unbind();

    /**
     * Determines with view should participate in the shared element transition
     * @return view to be animated
     */
    public abstract View getAnimationTarget();

}

