package hr.foi.varazdinevents.places.eventDetails;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.test.espresso.core.deps.guava.base.Strings;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.orm.query.Condition;
import com.orm.query.Select;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import hr.foi.varazdinevents.MainApplication;
import hr.foi.varazdinevents.R;
import hr.foi.varazdinevents.injection.modules.EventDetailsActivityModule;
import hr.foi.varazdinevents.models.Event;
import hr.foi.varazdinevents.models.User;
import hr.foi.varazdinevents.places.events.MainActivity;
import hr.foi.varazdinevents.places.hostProfile.HostProfileActivity;
import hr.foi.varazdinevents.ui.base.BaseNavigationActivity;
import hr.foi.varazdinevents.util.Constants;
import hr.foi.varazdinevents.util.FontManager;
import hr.foi.varazdinevents.util.ScreenUtils;
import rx.Observer;

import static hr.foi.varazdinevents.util.Constants.PERMISSION_ACCESS_FINE_LOCATION_REQUEST;

/**
 * Created by Antonio Martinović on 08.11.16.
 */

/**
 * This class is used for displaying details about the selected event
 */
public class EventDetailsActivity extends BaseNavigationActivity implements AppBarLayout.OnOffsetChangedListener {
    private static final String ARG_EVENT = "arg_event";
    @Inject
    User user;
    @Inject
    EventDetailsPresenter presenter;
    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Nullable
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.details_content_holder)
    LinearLayout contentHolder;
    @BindView(R.id.event_details_image)
    ImageView image;
    @BindView(R.id.event_details_title)
    TextView title;
    @BindView(R.id.event_details_date)
    TextView date;
    @BindView(R.id.event_details_time)
    TextView time;
    @BindView(R.id.event_details_host)
    TextView host;
    @BindView(R.id.event_details_category)
    TextView category;
    @BindView(R.id.event_details_facebook)
    TextView facebook;
    @BindView(R.id.event_details_offers)
    TextView offers;
    @BindView(R.id.event_details_text)
    TextView text;
    @BindView(R.id.fab_detailed_favorite)
    FloatingActionButton fabDetailedFavorite;
    @BindView(R.id.awesome_title)
    TextView awesomeTitle;
    @BindView(R.id.awesome_calendar)
    TextView awesomeCalendar;
    @BindView(R.id.awesome_clock)
    TextView awesomeClock;
    @BindView(R.id.awesome_facebook)
    TextView awesomeFaceboot;
    @BindView(R.id.awesome_host)
    TextView awesomeHost;
    @BindView(R.id.awesome_rocket)
    TextView awesomeRocket;
    @BindView(R.id.event_details_map)
    View mapContainer;
    private Event event;
    private boolean shouldInvalidateAnimation = false;

    /**
     * Creates new intent, starts "Event Details" activity
     *
     * @param event
     * @param startingActivity
     */
    public static void startWithEvent(Event event, Context startingActivity) {
        Intent intent = new Intent(startingActivity, EventDetailsActivity.class);
        intent.putExtra(ARG_EVENT, event);
        startingActivity.startActivity(intent);
    }

    /**
     * Shows integrated animations on transition to event details
     *
     * @param event
     * @param startingActivity
     * @param view
     */
    @TargetApi(21)
    public static void startWithEventAnimated(Event event, MainActivity startingActivity, View view) {
        Intent intent = new Intent(startingActivity, EventDetailsActivity.class);
        intent.putExtra(ARG_EVENT, event);
        View decor = startingActivity.getWindow().getDecorView();
        View statusBar = decor.findViewById(android.R.id.statusBarBackground);
        View navBar = decor.findViewById(android.R.id.navigationBarBackground);

        Pair<View, String> p1 = Pair.create(view, "event_image_anim_target");
        Pair<View, String> p2 = Pair.create(statusBar, Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME);
        Pair<View, String> p3 = Pair.create(navBar, Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME);

        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(startingActivity, p2, p3, p1);
        startingActivity.startActivity(intent, options.toBundle());
    }

    /**
     * Creates "Event Details" activity.
     * This method sets the decor, checks if the selected event is favorited,
     * sets the toolbar title, grabs event image for toolbar,
     * creates transition animations and pre loads needed icons.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            postponeEnterTransition();

            final View decor = getWindow().getDecorView();
            decor.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        decor.getViewTreeObserver().removeOnPreDrawListener(this);
                        startPostponedEnterTransition();
                    }
                    return true;
                }
            });
        }

        setExitSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onSharedElementStart(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
                super.onSharedElementStart(sharedElementNames, sharedElements, sharedElementSnapshots);
            }
        });

        this.event = getIntent().getParcelableExtra(ARG_EVENT);
        refreshEvent();
        toggleFavoriteIcon(this.event.isFavorite);

        if (event.getTitle().equals("")) {
            collapsingToolbarLayout.setTitle(getResources().getString(R.string.app_name));
        } else {
            collapsingToolbarLayout.setTitle(event.getTitle());
        }

        appBarLayout.addOnOffsetChangedListener(this);

        if (event.getTitle().equals("")) {
            toolbar.setTitle(getResources().getString(R.string.app_name));
        } else {
            toolbar.setTitle(event.getTitle());
        }

        if (event.getType() != Constants.EVENTS_NO_IMAGE_CARD) {
            collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

            int screenSize = ScreenUtils.getScreenWidth(this);

            Picasso.with(this)
                    .load(event.getImage())
                    .resize(screenSize, screenSize)
                    .centerCrop()
                    .into(image);
        } else {
            appBarLayout.setExpanded(false);
            AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) collapsingToolbarLayout.getLayoutParams();
            params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP | AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
//            image.getLayoutParams().height = 200;
//            collapsingToolbarLayout.setLayoutParams(params);
        }

        Typeface iconFont = FontManager.getFontAwesome(getApplicationContext());
        FontManager.markAsIconContainer(awesomeTitle, iconFont);
        FontManager.markAsIconContainer(awesomeCalendar, iconFont);
        FontManager.markAsIconContainer(awesomeClock, iconFont);
        FontManager.markAsIconContainer(awesomeFaceboot, iconFont);
        FontManager.markAsIconContainer(awesomeHost, iconFont);
        FontManager.markAsIconContainer(awesomeRocket, iconFont);

//        collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(primary));
//        collapsingToolbarLayout.setStatusBarScrimColor(palette.getDarkMutedColor(primaryDark));
//        fabDetailedFavorite.setRippleColor(lightVibrantColor);
//        fabDetailedFavorite.setBackgroundTintList(ColorStateList.valueOf(vibrantColor));

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected User getUser() {
        return user;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_event_details;
    }

    /**
     * Starts "Event Details" activity.
     * Gets and sets event details.
     */
    @Override
    protected void onStart() {
        super.onStart();
        presenter.attachView(this);
        showLoading(true);

        if (event.getDate().equals("")) {
            this.date.setText("N/A");
            this.time.setText("N/A");
        } else {
            Date eventDate = new Date(event.getDate() - 3600000);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

            this.date.setText(dateFormat.format(eventDate));

            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            this.time.setText(timeFormat.format(eventDate));
        }

        if (event.getTitle().equals("")) {
            this.title.setText("N/A");
        } else {
            this.title.setText(event.getTitle());
        }

        if (event.getHost().equals("")) {
            this.host.setText("N/A");
        } else {
            this.host.setText(event.getHost());
        }

        if (event.getCategory().equals("")) {
            this.category.setText("N/A");
        } else {
            this.category.setText(event.getCategory());
        }

        if (event.getOffers().equals("")) {
            this.offers.setText("N/A");
        } else {
            this.offers.setText(event.getOffers());
        }

        Observer<Void> mapObserver = new Observer<Void>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                showBasicError(getString(R.string.error_display));
            }

            @Override
            public void onNext(Void aVoid) {
                presenter.resolveMapPosition();
                text.setText(presenter.getParsedEventDescription());
                showLoading(false);
            }
        };

        rx.Observable<Void> eventStream = presenter.parseEventData(event);
        eventStream.subscribe(mapObserver);
    }

    /**
     * Detaches view from presenter on stop
     */
    @Override
    protected void onStop() {
        super.onStop();
        presenter.detachView();
    }

    /**
     * Check which transition animation to load
     *
     * @param loading
     */
    public void showLoading(boolean loading) {
        if (loading) {
            animateOut();
        } else {
            animateIn();
        }
    }

    /**
     * Shows animations for transitioning into activity
     */
    private void animateIn() {
        progressBar.setVisibility(View.GONE);
        contentHolder.setTranslationY(ScreenUtils.getScreenHeight(this));
        contentHolder.animate()
                .translationY(0)
                .alpha(1.0f)
                .setInterpolator(new DecelerateInterpolator(3.0f))
                .setDuration(700)
                .start();
        fabDetailedFavorite.setTranslationX(ScreenUtils.dpToPx(200));
        fabDetailedFavorite.animate()
                .translationX(0)
                .setStartDelay(300)
                .setInterpolator(new DecelerateInterpolator(3.0f))
                .setDuration(500)
                .start();
    }

    /**
     * Shows animations for transitioning out of the activity
     */
    private void animateOut() {
        progressBar.setVisibility(View.VISIBLE);
        contentHolder.animate()
                .translationY(ScreenUtils.getScreenHeight(this))
                .alpha(1.0f)
                .setInterpolator(new DecelerateInterpolator(3.0f))
                .setDuration(500)
                .start();
        fabDetailedFavorite.setTranslationX(ScreenUtils.dpToPx(200));
        contentHolder.setTranslationY(ScreenUtils.dpToPx(ScreenUtils.getScreenHeight(this) - ScreenUtils.dpToPx(380)));
    }

    @Override
    public void setupActivityComponent() {
        MainApplication.get(this).getUserComponent()
                .plus(new EventDetailsActivityModule(this))
                .inject(this);
    }

    /**
     * This method is started if heart shaped icon in event details is clicked.
     * Calls methods to update clicked event's database attribute and
     * changes icon's layout
     */
    @OnClick(R.id.fab_detailed_favorite)
    public void onFavoriteClicked() {
        presenter.itemFavorited(!event.isFavorite());

    }

    /**
     * Starts new activity if the underlined Facebook text in event's details is clicked.
     * Opens event's facebook page in default mobile browser.
     */
    @OnClick(R.id.event_details_facebook)
    public void onFacebookClicked() {
        if (!Strings.isNullOrEmpty(event.getFacebook())) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(event.getFacebook()));
            startActivity(intent);
        }
    }

    /**
     * Toggles event's heart shaped icon on and off, changes icon layout
     *
     * @param isFavorite
     */
    public void toggleFavoriteIcon(boolean isFavorite) {
        if (isFavorite)
            fabDetailedFavorite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favorite_white_24dp));
        else
            fabDetailedFavorite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favorite_border_white_24dp));
    }

    /**
     * Starts new (Host Profile) activity if the underlined Host text in event's details is clicked.
     *
     * @param view
     */
    @OnClick(R.id.event_details_host)
    public void onClick(View view) {
        Intent newIntent = new Intent(EventDetailsActivity.this, HostProfileActivity.class);
        newIntent.putExtra("hostname", event.getHost());
        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        EventDetailsActivity.this.startActivity(newIntent);
    }

    /**
     * Checks permissions to show location on map
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_FINE_LOCATION_REQUEST: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    presenter.setMap();
                } else {
                    showBasicError("Unable to show map");
                }
                return;
            }
        }
    }

    /**
     * Shows animations after "back" is pressed from Event's Details activity
     */
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setExitTransition(null);
                Animation slideDownAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.activity_slide_down);
                getWindow().getDecorView()
                        .findViewById(android.R.id.statusBarBackground)
                        .setAlpha(0f);
                contentHolder.startAnimation(slideDownAnimation);
            }

            if (shouldInvalidateAnimation) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    image.setVisibility(View.INVISIBLE);
                    getWindow().setSharedElementReturnTransition(null);
                }
            }
            super.onBackPressed();
        }
    }

    /**
     * Changes flag if vertical offset is lower than -5
     *
     * @param appBarLayout
     * @param verticalOffset
     */
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset < -5) {
            shouldInvalidateAnimation = true;
        } else {
            shouldInvalidateAnimation = false;
        }
    }

    public Event getEvent() {
        return event;
    }

    private void setEvent(Event event) {
        this.event = event;
    }

    public void refreshEvent() {
        setEvent(Select.from(Event.class)
                .where(Condition.prop("API_ID").eq(getEvent().getApiId()))
                .first());
    }
}
