package hr.foi.varazdinevents.ui.elements.list;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Antonio Martinović on 13.10.16.
 */

import butterknife.Unbinder;

public abstract class ItemViewHolder extends RecyclerView.ViewHolder {
    protected Unbinder unbinder;

    public ItemViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void bind(Object item);

    public abstract void unbind();

}

