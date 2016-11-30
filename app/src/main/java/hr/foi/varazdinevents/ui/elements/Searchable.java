package hr.foi.varazdinevents.ui.elements;

/**
 * Created by Antonio Martinović on 29.11.16.
 */
public interface Searchable {

    /**
     * Search the object with text
     * @param query
     * @return true if object contains searched string
     */
    boolean isMatching(String query);

}
