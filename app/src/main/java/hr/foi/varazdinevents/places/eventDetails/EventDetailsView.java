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

    @BindView(R.id.event_details_image)
    TextView image;
    @BindView(R.id.event_details_title)
    TextView title;
    @BindView(R.id.event_details_date)
    TextView date;
    @BindView(R.id.event_details_dateTo)
    TextView dateTo;
    @BindView(R.id.event_details_host)
    TextView host;
    @BindView(R.id.event_details_category)
    TextView category;
    @BindView(R.id.event_details_facebook)
    TextView facebook;
    @BindView(R.id.event_details_offers)
    TextView offers;
    @BindView(R.id.event_details_officialLink)
    TextView officialLink;
    @BindView(R.id.event_details_text)
    TextView text;

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
        //image.setText(event.getImage());
        this.title.setText(event.getTitle());
        this.date.setText(event.getDate().toString());
        this.dateTo.setText(event.getDateTo().toString());
        this.host.setText(event.getHost());
        this.category.setText(event.getCategory());
        this.facebook.setText(event.getFacebook());
        this.offers.setText(event.getOffers());
        this.officialLink.setText(event.getOfficialLink());
        this.text.setText(event.getText());
        showLoading(false);
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
