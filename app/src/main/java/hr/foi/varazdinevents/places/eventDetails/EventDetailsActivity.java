package hr.foi.varazdinevents.places.eventDetails;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

import javax.inject.Inject;

import butterknife.BindView;
import hr.foi.varazdinevents.MainApplication;
import hr.foi.varazdinevents.R;
import hr.foi.varazdinevents.injection.modules.EventDetailsActivityModule;
import hr.foi.varazdinevents.models.Event;
import hr.foi.varazdinevents.ui.base.BaseActivity;

/**
 * Created by Antonio Martinović on 08.11.16.
 */

public class EventDetailsActivity extends BaseActivity {
    private static final String ARG_EVENT = "arg_event";

    private Event event;

    @Inject
    EventDetailsPresenter presenter;

    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.progresBar)
    ProgressBar progressBar;
    @BindView(R.id.details_content_holder)
    LinearLayout contentHolder;
    @BindView(R.id.event_details_image)
    ImageView image;
    @BindView(R.id.event_details_title)
    TextView title;
    @BindView(R.id.event_details_date)
    TextView date;
//    @BindView(R.id.event_details_dateTo)
//    TextView dateTo;
    @BindView(R.id.event_details_host)
    TextView host;
    @BindView(R.id.event_details_category)
    TextView category;
    @BindView(R.id.event_details_facebook)
    TextView facebook;
    @BindView(R.id.event_details_offers)
    TextView offers;
//    @BindView(R.id.event_details_officialLink)
//    TextView officialLink;
    @BindView(R.id.event_details_text)
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.event = getIntent().getParcelableExtra(ARG_EVENT);

        collapsingToolbarLayout.setTitle(event.getTitle());
        toolbar.setTitle(event.getTitle());
        Picasso.with(this).load(event.getImage()).into(image);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_event_details;
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.attachView(this);
        showLoading(true);
        this.title.setText(event.getTitle());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        this.date.setText(dateFormat.format(event.date*1000L));

//        this.date.setText(event.getDate() != null ? event.getDate().toString() : "");
//        this.dateTo.setText(event.getDateTo() != null ? event.getDateTo().toString() : "");
        this.host.setText(event.getHost());
        this.category.setText(event.getCategory());
        this.facebook.setText(event.getFacebook());
        this.offers.setText(event.getOffers());
//        this.officialLink.setText(event.getOfficialLink());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            this.text.setText(Html.fromHtml(event.getText(), Html.FROM_HTML_MODE_LEGACY).toString());
        } else {
            this.text.setText(Html.fromHtml(event.getText()).toString());
        }
        showLoading(false);

    }

    @Override
    protected void onStop(){
        super.onStop();
        presenter.detachView();
    }

    public void showLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        contentHolder.setVisibility(loading ? View.GONE : View.VISIBLE);
    }

    public static void startWithEvent(Event event, Context startingActivity) {
        Intent intent = new Intent(startingActivity, EventDetailsActivity.class);
        intent.putExtra(ARG_EVENT, event);
        startingActivity.startActivity(intent);
    }

    @Override
    public void setupActivityComponent() {
        MainApplication.get(this).getUserComponent()
                .plus(new EventDetailsActivityModule(this))
                .inject(this);
    }

    @Override
    public void onItemClicked(Object item) {

    }
}
