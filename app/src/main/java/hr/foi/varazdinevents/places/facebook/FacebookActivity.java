package hr.foi.varazdinevents.places.facebook;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import hr.foi.varazdinevents.MainApplication;
import hr.foi.varazdinevents.R;
import hr.foi.varazdinevents.api.RestService;
import hr.foi.varazdinevents.injection.modules.FacebookActivityModule;
import hr.foi.varazdinevents.injection.modules.MainActivityModule;
import hr.foi.varazdinevents.models.User;
import hr.foi.varazdinevents.places.events.MainActivity;
import hr.foi.varazdinevents.places.events.MainPresenter;
import hr.foi.varazdinevents.ui.base.BaseNavigationActivity;
import hr.foi.varazdinevents.util.SharedPrefs;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by Valentin Magdić on 23.01.17..
 */

/**
 * Contains methods for facebook login and import events from facebook
 */
public class FacebookActivity extends BaseNavigationActivity{
    @Inject
    User user;
    @Inject
    FacebookPresenter presenter;
    @BindView(R.id.fb_import_event_id)
    Button facebookButton;
    @BindView(R.id.facebook_import_event_id)
    TextView facebookEventId;

    CallbackManager callbackManager;
    private LoginButton fbLoginButton;
    private RestService restService;

    /**
     * Creates "Facebook" activity
     * Binds facebook button from xml, creates callback for facebook login
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        fbLoginButton = (LoginButton) findViewById(R.id.facebook_login_button);

        callbackManager = CallbackManager.Factory.create();
        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                SharedPrefs.write("FACEBOOK_TOKEN", loginResult.getAccessToken().getToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    @Override
    protected User getUser() {
        return null;
    }

    /**
     * Returns layout for "Facebook" activity
     */
    @Override
    protected int getLayout() {
        return R.layout.activity_facebook;
    }

    @Override
    protected void setupActivityComponent() {
        MainApplication.get(this).getUserComponent()
                .plus(new FacebookActivityModule(this))
                .inject(this);
    }

    public static void start(BaseNavigationActivity activity) {
        Intent intent = new Intent(activity, FacebookActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    /**
     * Facebook's login activity result
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * If Import button is clicked, reads the entered event Id, gets user's token from database,
     * gets facebook's token from shared preferences and calls method for event import
     */
    @OnClick(R.id.fb_import_event_id)
    public void OnImportEventButtonClicked(){
        String enteredId = facebookEventId.getText().toString();
        String userToken = user.getToken();
        String facebookToken = SharedPrefs.read("FACEBOOK_TOKEN", "Null");

        presenter.importEvent(enteredId, userToken, facebookToken);

    }

    /**
     * Attaches view from presenter
     */
    @Override
    protected void onStart() {
        super.onStart();
        presenter.attachView(this);
    }

    /**
     * Detaches view from presenter
     */
    @Override
    protected void onStop(){
        super.onStop();
        presenter.detachView();
    }


}
