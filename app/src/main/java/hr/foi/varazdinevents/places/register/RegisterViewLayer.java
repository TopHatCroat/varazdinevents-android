package hr.foi.varazdinevents.places.register;

import hr.foi.varazdinevents.ui.base.ViewLayer;

/**
 * Created by Antonio Martinović on 12.11.16.
 */
public interface RegisterViewLayer extends ViewLayer{
    void onSuccess();
    void onFailure(String message);
}
