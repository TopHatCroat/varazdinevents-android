package hr.foi.varazdinevents.places.eventDetails;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import hr.foi.varazdinevents.R;
import hr.foi.varazdinevents.models.Event;
import hr.foi.varazdinevents.ui.base.BaseView;

/**
 * Created by Antonio Martinović on 08.11.16.
 */

public class EventDetailsView extends BaseView {
    @Inject
    EventDetailsPresenter presenter;

    @BindView(R.id.event_details_picture)
    TextView picture;
    @BindView(R.id.event_details_title)
    TextView title;
    @BindView(R.id.event_details_date)
    TextView date;

    public EventDetailsView(Context context) {
        this(context, null);
    }

    public EventDetailsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EventDetailsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ((EventDetailsActivity) context).getMainActivityComponent().inject(this);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        presenter.attachView(this);
        showLoading(true);
        Event event = presenter.getEvent();
        picture.setText("PLACEHOLDER");
        title.setText("PLACEHOLDER");
        picture.setText("PLACEHOLDER");
    }

    @Override
    public void onDetachedFromWindow() {
        presenter.detachView();
        super.onDetachedFromWindow();
    }


    @Override
    public void onItemClicked(Object item) {

    }

    public void showLoading(boolean loading) {
//        recyclerView.setVisibility(loading ? View.GONE : View.VISIBLE);
//        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
    }
}
