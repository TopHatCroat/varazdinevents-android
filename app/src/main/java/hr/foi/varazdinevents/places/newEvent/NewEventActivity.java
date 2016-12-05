package hr.foi.varazdinevents.places.newEvent;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import hr.foi.varazdinevents.MainApplication;
import hr.foi.varazdinevents.R;
import hr.foi.varazdinevents.api.EventManager;
import hr.foi.varazdinevents.injection.modules.NewEventModule;
import hr.foi.varazdinevents.ui.base.BaseActivity;
import hr.foi.varazdinevents.util.PickerHelper;
import timber.log.Timber;

/**
 * Created by Antonio Martinović on 03.12.16.
 */
public class NewEventActivity extends BaseActivity implements TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener {
    private static final String START_DATE_PICKER_TAG = "start_date_picker";
    public static final String START_TIME_PICKER_TAG = "start_time_picker";
    public static final String END_DATE_PICKER_TAG = "end_date_picker";
    public static final String END_TIME_PICKER_TAG = "end_time_picker";
    @Inject
    EventManager eventManager;
    @Inject
    NewEventPresenter presenter;

    @BindView(R.id.title_new_event)
    EditText title;
    @BindView(R.id.text_new_event)
    EditText text;
    @BindView(R.id.start_date_new_event)
    TextView startDate;
    @BindView(R.id.start_time_new_event)
    TextView startTime;
    @BindView(R.id.end_date_new_event)
    TextView endDate;
    @BindView(R.id.end_time_new_event)
    TextView endTime;
    @BindView(R.id.host_new_event)
    EditText host;
    @BindView(R.id.official_link_new_event)
    EditText officialLink;
    @BindView(R.id.image_new_event)
    EditText image;
    @BindView(R.id.facebook_new_event)
    EditText facebook;
    @BindView(R.id.offers_new_event)
    EditText offers;
    @BindView(R.id.category_new_event)
    EditText category;
    @BindView(R.id.create_new_event)
    FloatingActionButton createButton;
    @BindView(R.id.progresBar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_new_event;
    }

    @Override
    protected void setupActivityComponent() {
        MainApplication.get(this)
                .getUserComponent()
                .plus(new NewEventModule(this))
                .inject(this);
    }

    @Override
    public void onItemClicked(Object item) {

    }

    public void showLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

    @OnTextChanged(value = R.id.text_new_event)
    public void onChangeText(CharSequence editText) {
        eventManager.getNewEvent().setText(editText.toString());
    }

    @OnTextChanged(value = R.id.title_new_event)
    public void onChangeTitle(CharSequence editText) {
        eventManager.getNewEvent().setTitle(editText.toString());
    }

    @OnClick(R.id.start_date_new_event)
    public void onChangeDate() {
        PickerHelper.createDatePicker(START_DATE_PICKER_TAG, this, this);
    }

    @OnClick(R.id.start_time_new_event)
    public void onChangeLength() {
        PickerHelper.createTimePicker(START_TIME_PICKER_TAG, this, this);
    }

    @OnClick(R.id.end_date_new_event)
    public void onChangeEndDate() {
        PickerHelper.createDatePicker(END_DATE_PICKER_TAG, this, this);
    }

    @OnClick(R.id.end_time_new_event)
    public void onChangeEndTime() {
        PickerHelper.createTimePicker(END_TIME_PICKER_TAG, this, this);
    }

    @OnTextChanged(value = R.id.host_new_event)
    public void onChangeHost(CharSequence editText) {
        eventManager.getNewEvent().setHost(editText.toString());
    }

    @OnTextChanged(value = R.id.official_link_new_event)
    public void onChangeOfficialLink(CharSequence editText) {
        eventManager.getNewEvent().setOfficialLink(editText.toString());
    }

    @OnTextChanged(value = R.id.image_new_event)
    public void onChangeImage(CharSequence editText) {
        eventManager.getNewEvent().setImage(editText.toString());
    }

    @OnTextChanged(value = R.id.facebook_new_event)
    public void onChangeFacebook(CharSequence editText) {
        eventManager.getNewEvent().setFacebook(editText.toString());
    }

    @OnTextChanged(value = R.id.offers_new_event)
    public void onChangeOffers(CharSequence editText) {
        eventManager.getNewEvent().setOffers(editText.toString());
    }

    @OnTextChanged(value = R.id.category_new_event)
    public void onChangeCategory(CharSequence editText) {
        eventManager.getNewEvent().setCategory(editText.toString());
    }

    @OnClick(R.id.create_new_event)
    public void onClickCreate() {
        showLoading(true);
        presenter.itemClicked(eventManager.getNewEvent());
    }

    public static void start(BaseActivity baseActivity) {
        Intent intent = new Intent(baseActivity, NewEventActivity.class);
        baseActivity.startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.attachView(this);
        showLoading(false);
    }

    @Override
    protected void onStop(){
        super.onStop();
        presenter.detachView();
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        switch (view.getTag()) {
            case START_DATE_PICKER_TAG:
                if(eventManager.getNewEvent().getDate() != null) {
                    calendar.setTimeInMillis(eventManager.getNewEvent().getDate());
                }
                calendar.set(year, monthOfYear, dayOfMonth);
                eventManager.getNewEvent().setDate(calendar.getTimeInMillis());
                updateDateField(startDate, calendar);
                break;
            case END_DATE_PICKER_TAG:
                if(eventManager.getNewEvent().getDateTo() != null) {
                    calendar.setTimeInMillis(eventManager.getNewEvent().getDateTo());
                }
                calendar.set(year, monthOfYear, dayOfMonth);
                eventManager.getNewEvent().setDateTo(calendar.getTimeInMillis());
                updateDateField(endDate, calendar);
                break;
        }
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        switch (view.getTag()) {
            case START_TIME_PICKER_TAG:
                if(eventManager.getNewEvent().getDate() != null) {
                    calendar.setTimeInMillis(eventManager.getNewEvent().getDate());
                }
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, second);
                eventManager.getNewEvent().setDate(calendar.getTimeInMillis() );
                updateTimeField(startTime, calendar);
                break;
            case END_TIME_PICKER_TAG:
                if(eventManager.getNewEvent().getDateTo() != null) {
                    calendar.setTimeInMillis(eventManager.getNewEvent().getDateTo());
                }
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, second);
                eventManager.getNewEvent().setDateTo(calendar.getTimeInMillis());
                updateTimeField(endTime, calendar);
                break;
        }
    }

    private void updateTimeField(TextView timeView, Calendar calendar) {
        Calendar cal = new GregorianCalendar();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        dateFormat.setTimeZone(cal.getTimeZone());
        timeView.setText(dateFormat.format(calendar.getTime()));
    }

    private void updateDateField(TextView dateView, Calendar calendar) {
        Calendar cal = new GregorianCalendar();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        dateFormat.setTimeZone(cal.getTimeZone());
        dateView.setText(dateFormat.format(calendar.getTime()));
    }
}