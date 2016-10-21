package hr.foi.varazdinevents.ui.base;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Antonio Martinović on 12.10.16.
 */

public abstract class BaseView extends CoordinatorLayout implements ViewLayer {
    protected Unbinder unbinder;

    public BaseView(Context context) {
        this(context, null);

    }

    public BaseView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public BaseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        unbinder = ButterKnife.bind(this);
    }

    @Override
    public void onDetachedFromWindow() {
        unbinder.unbind();
        super.onDetachedFromWindow();
    }

    @Override
    public void showBasicError(String message) {
        Snackbar.make(this, message, Snackbar.LENGTH_LONG).show();
    }
}
