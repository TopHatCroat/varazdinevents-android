package hr.foi.varazdinevents.ui.elements;

import android.view.ViewGroup;

import hr.foi.varazdinevents.ui.elements.list.ItemViewHolder;

/**
 * Created by Antonio Martinović on 21.10.16.
 */

public interface ItemViewHolderFactory {
    ItemViewHolder createViewHolder(ViewGroup parent);
}
