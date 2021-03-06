package hr.foi.varazdinevents.places.about;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.OnClick;
import hr.foi.varazdinevents.MainApplication;
import hr.foi.varazdinevents.R;
import hr.foi.varazdinevents.injection.modules.AboutModule;
import hr.foi.varazdinevents.models.User;
import hr.foi.varazdinevents.ui.base.BaseNavigationActivity;

/**
 * Class for "About" activity
 */
public class AboutActivity extends BaseNavigationActivity {

    @Inject
    User user;

    /**
     * Creates "About" activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(R.string.about_app);
        overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
    }

    /**
     * Crates animation on screen transition
     */
    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
    }

    @Override
    protected User getUser() {
        return user;
    }

    /**
     * Returns layout for activity
     */
    @Override
    protected int getLayout() {
        return R.layout.activity_about;
    }

    @Override
    protected void setupActivityComponent() {
        MainApplication.get(this)
                .getUserComponent()
                .plus(new AboutModule(this))
                .inject(this);
    }

    /**
     * Starts the "About" activity
     * @param startingActivity
     */
    public static void start(Context startingActivity) {
        Intent intent = new Intent(startingActivity, AboutActivity.class);
        startingActivity.startActivity(intent);
    }

    /**
     * Registers click on button, automatically calls customer service
     */
    @OnClick(R.id.about_call_button)
    public void onPhoneClicked() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.callHost)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:0998508608"));
                        try{
                            startActivity(intent);
                        }
                        catch (android.content.ActivityNotFoundException ex){
                            Toast.makeText(getApplicationContext(),getResources().getString(R.string.event_create_failed),Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }
}
