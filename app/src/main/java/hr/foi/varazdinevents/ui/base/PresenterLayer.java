package hr.foi.varazdinevents.ui.base;

/**
 * Created by Antonio Martinović on 12.10.16.
 */

public interface PresenterLayer<V extends ViewLayer> {

    /**
     * Attaches a view to the listener
     * @param viewLayer view extending @ViewLayer
     */
    void attachView(V viewLayer);

    /**
     * Detaches the view from the listener
     */
    void detachView();

}
