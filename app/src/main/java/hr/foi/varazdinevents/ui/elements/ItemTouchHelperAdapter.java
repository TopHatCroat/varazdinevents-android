package hr.foi.varazdinevents.ui.elements;

/**
 * Created by Antonio Martinović on 15.10.16.
 */

public interface ItemTouchHelperAdapter {
    void onItemClicked(int adapterPosition);

    boolean onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int possition);
}
