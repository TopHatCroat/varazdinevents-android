package hr.foi.varazdinevents.ui.elements.list;

import android.view.View;

/**
 * Created by Antonio Martinović on 12.01.17.
 */

public interface ListListener<T> {
    void onItemClick(T item);
    void onItemClick(T item, View view);
}
