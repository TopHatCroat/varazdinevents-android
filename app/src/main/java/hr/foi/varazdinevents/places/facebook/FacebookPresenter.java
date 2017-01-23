package hr.foi.varazdinevents.places.facebook;

import android.view.View;

import org.json.JSONObject;


import hr.foi.varazdinevents.R;
import hr.foi.varazdinevents.api.UserManager;
import hr.foi.varazdinevents.ui.base.BasePresenter;
import rx.Observer;

/**
 * Created by Valentin Magdić on 23.01.17..
 */

public class FacebookPresenter extends BasePresenter<FacebookActivity> {
    private UserManager userManager;

    public FacebookPresenter(UserManager userManager) {
        this.userManager = userManager;
    }

    public void importEvent(String enteredId, String userToken, String facebookToken){
        checkViewAttached();

        Observer<JSONObject> eventObserver = new Observer<JSONObject>() {
            @Override
            public void onNext(JSONObject events) {
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getViewLayer().showBasicError(getViewLayer().getString(R.string.network_not_accessible));
                }
            }
        };

        rx.Observable<JSONObject> eventStream = userManager.importFacebookEvent(enteredId, userToken, facebookToken);
        eventStream.subscribe(eventObserver);
    }

}
