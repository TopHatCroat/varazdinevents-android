package hr.foi.varazdinevents.ui.elements.list;

/**
 * Should be implemented by view holders for {@link ItemListAdapter}
 * Created by Antonio Martinović on 22.10.16.
 */
public interface Listable<T> extends Comparable<T>{
    /**
     * Used to determine the type of view that should be inflated for the view
     * @return ID of view type
     */
    int getType();
}
