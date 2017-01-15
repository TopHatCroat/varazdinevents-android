package hr.foi.varazdinevents.ui.elements;

import android.view.View;

/**
 * Created by Antonio Martinović on 15.10.16.
 */

public interface ItemTouchHelperAdapter {
    void onItemClicked(int adapterPosition, View view);

    boolean onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int possition);
}
