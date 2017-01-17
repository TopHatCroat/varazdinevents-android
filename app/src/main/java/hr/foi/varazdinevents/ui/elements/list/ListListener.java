package hr.foi.varazdinevents.ui.elements.list;

import android.view.View;

/**
 * Provides methods for returning item clicked in a list adapter
 * Created by Antonio Martinović on 12.01.17.
 */

public interface ListListener<T> {

    /**
     * Should be called when user clicks on an item
     * @param item object of item clicked
     */
    void onItemClick(T item);

    /**
     * Should be called when user clicks on an item and a view should be passed
     * to animate the activity transition
     * @param item object of item clicked
     * @param view animation target
     */
    void onItemClick(T item, View view);
}
