package hr.foi.varazdinevents.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import hr.foi.varazdinevents.R;

/**
 * Base activity that every class should extend, it handles basic UI components
 * like the toolbar and base layout, as well as calls methods for setting the content view,
 * Butter Knife binding and constructing DI graph
 * Created by Antonio Martinović on 12.10.16.
 */

public abstract class BaseActivity extends AppCompatActivity implements ViewLayer {
    protected Unbinder unbinder;

    @Nullable
    @BindView(R.id.toolbar)
    protected Toolbar toolbar;


    @BindView(R.id.coordinator_layout)
    protected CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setupActivityComponent();
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        unbinder = ButterKnife.bind(this);

        if(toolbar != null)
            setSupportActionBar(toolbar);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    /**
     * Specify wich layout should be inflated for the activity
     * @return layout resource ID
     */
    protected abstract int getLayout();

    /**
     * Activity should put define DI graph genrating
     * code by overriding this method
     */
    protected abstract void setupActivityComponent();

    /**
     * Displays a short error message to the user
     * by using a Snackbar
     * @param message error message to display
     */
    @Override
    public void showBasicError(String message) {
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    @Nullable
    public Toolbar getToolbar() {
        return toolbar;
    }
}
