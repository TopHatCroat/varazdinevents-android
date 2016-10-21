package hr.foi.varazdinevents.ui.elements.navigation;

import hr.foi.varazdinevents.ui.elements.OnStartDragListener;
import hr.foi.varazdinevents.ui.elements.ItemListAdapter;

/**
 * Created by Antonio Martinović on 16.10.16.
 */

public class NavigationItemListAdapter extends ItemListAdapter {
    public NavigationItemListAdapter(OnStartDragListener startDragListener) {
        super(startDragListener);
    }
}
