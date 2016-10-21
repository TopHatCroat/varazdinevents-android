package hr.foi.varazdinevents.ui.elements;

/**
 * Created by Antonio Martinović on 15.10.16.
 */

public interface ItemTouchHelperAdapter {

    boolean onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int possition);
}
