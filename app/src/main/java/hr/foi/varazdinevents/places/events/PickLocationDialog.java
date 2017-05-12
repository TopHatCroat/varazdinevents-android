package hr.foi.varazdinevents.places.events;

import android.app.Dialog;
import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hr.foi.varazdinevents.R;

/**
 * Created by Antonio Martinović on 10/05/17.
 */
public class PickLocationDialog extends Dialog {
    @BindView(R.id.message_cancelable_ok_dialog)
    TextView message;

    @BindView(R.id.enable_pick_location_dialog)
    TextView pickLocation;

    @BindView(R.id.manual_pick_location_dialog)
    TextView pickManual;

    @BindView(R.id.progress_pick_location_dialog)
    ProgressBar progressBar;

    @BindView(R.id.content_pick_location_dialog)
    LinearLayout contentHolder;

    private PickLocationListener listener;

    public PickLocationDialog(@NonNull Context context) {
        super(context);
    }

    protected PickLocationDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pick_location_dialog);
        ButterKnife.bind(this);
        setCancelable(false);

        contentHolder.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.enable_pick_location_dialog)
    public void onConfirm() {
        listener.requestPermission();
        setLoading(true);
    }

    @OnClick(R.id.manual_pick_location_dialog)
    public void onCancel() {
        if (listener != null)
            listener.setManual();
        this.dismiss();
    }

    public void setListener(PickLocationListener listener) {
        this.listener = listener;
    }

    public void setLoading(boolean loading) {
        if (loading) {
            contentHolder.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            contentHolder.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }
}
