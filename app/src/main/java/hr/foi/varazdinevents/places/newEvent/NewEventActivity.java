package hr.foi.varazdinevents.places.newEvent;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.common.base.Strings;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import hr.foi.varazdinevents.MainApplication;
import hr.foi.varazdinevents.R;
import hr.foi.varazdinevents.api.EventManager;
import hr.foi.varazdinevents.injection.modules.NewEventModule;
import hr.foi.varazdinevents.models.User;
import hr.foi.varazdinevents.ui.base.BaseActivity;
import hr.foi.varazdinevents.ui.base.BaseNavigationActivity;
import hr.foi.varazdinevents.util.FileUtils;
import hr.foi.varazdinevents.util.PickerHelper;

/**
 * Created by Antonio Martinović on 03.12.16.
 */
public class NewEventActivity extends BaseNavigationActivity implements TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener {
    private static final String START_DATE_PICKER_TAG = "start_date_picker";
    public static final String START_TIME_PICKER_TAG = "start_time_picker";
    public static final String END_DATE_PICKER_TAG = "end_date_picker";
    public static final String END_TIME_PICKER_TAG = "end_time_picker";
    private static final int SELECT_PICTURE = 100;
    private final int PERMISSION_STORAGE_REQUEST = 1;
    @Inject
    EventManager eventManager;
    @Inject
    NewEventPresenter presenter;
    @Inject
    User user;

    @BindView(R.id.title_new_event)
    EditText title;
    @BindView(R.id.text_new_event)
    EditText text;
    @BindView(R.id.start_date_new_event)
    EditText startDate;
    @BindView(R.id.start_time_new_event)
    EditText startTime;
    @BindView(R.id.end_date_new_event)
    EditText endDate;
    @BindView(R.id.end_time_new_event)
    EditText endTime;
    @BindView(R.id.official_link_new_event)
    EditText officialLink;
    @BindView(R.id.image_new_event)
    ImageView image;
    @BindView(R.id.facebook_new_event)
    EditText facebook;
    @BindView(R.id.offers_new_event)
    EditText offers;
    @BindView(R.id.category_new_event)
    Spinner category;
    @BindView(R.id.create_new_event)
    FloatingActionButton createButton;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private Uri chosenImage;

    /**
     * Creates "New event" activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(R.string.create_new_event);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(adapter);

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

    public void showLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

    /**
     * Monitors if inputed text has changed
     * @param editText
     */
    @OnTextChanged(value = R.id.text_new_event)
    public void onChangeText(CharSequence editText) {
        eventManager.getNewEvent().setText(editText.toString());
    }

    /**
     * Monitors if inputed text has changed
     * @param editText
     */
    @OnTextChanged(value = R.id.title_new_event)
    public void onChangeTitle(CharSequence editText) {
        eventManager.getNewEvent().setTitle(editText.toString());
    }

    /**
     * Shows date picker if clicked
     */
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

    @OnTextChanged(value = R.id.official_link_new_event)
    public void onChangeOfficialLink(CharSequence editText) {
        eventManager.getNewEvent().setOfficialLink(editText.toString());
    }

    @OnClick(value = R.id.image_new_event)
    public void onChangeImage() {
        startImagePicker();
//        eventManager.getNewEvent().setImage(editText.toString());
    }

    /**
     * Starts image picker
     */
    private void startImagePicker() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,
                    "Select Picture"), SELECT_PICTURE);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_STORAGE_REQUEST);
        }
    }

    @OnTextChanged(value = R.id.facebook_new_event)
    public void onChangeFacebook(CharSequence editText) {
        eventManager.getNewEvent().setFacebook(editText.toString());
    }

    @OnTextChanged(value = R.id.offers_new_event)
    public void onChangeOffers(CharSequence editText) {
        eventManager.getNewEvent().setOffers(editText.toString());
    }

    @OnClick(R.id.create_new_event)
    public void onClickCreate() {
        showLoading(true);
//        if(dataValid()) {
            eventManager.getNewEvent().setHost(user.getApiId().toString());
            presenter.uploadImage(eventManager.getNewEvent(), chosenImage);
//        }
    }

    /**
     * Checks if the entered data is valid
     * @return True if data is valid, otherwise false
     */
    private boolean dataValid() {
        List<EditText> editTexts = Arrays.asList(title, text, startDate, startTime, endDate, endTime);

        boolean isValid = true;
        for (TextView et : editTexts) {
            if (Strings.isNullOrEmpty(String.valueOf(et.getText()))) {
                et.requestFocus();
                et.setError("Must not be empty");
                isValid = false;
            }
        }

        return isValid;
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

    /**
     * Sets the chosen date from the date picker
     * @param view
     * @param year
     * @param monthOfYear
     * @param dayOfMonth
     */
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

    /**
     * Sets the chosen time from the time picker
     * @param view
     * @param hourOfDay
     * @param minute
     * @param second
     */
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

    /**
     * Updates time format
     * @param timeView
     * @param calendar
     */
    private void updateTimeField(TextView timeView, Calendar calendar) {
        Calendar cal = new GregorianCalendar();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        dateFormat.setTimeZone(cal.getTimeZone());
        timeView.setText(dateFormat.format(calendar.getTime()));
    }

    /**
     * Updates date format
     * @param dateView
     * @param calendar
     */
    private void updateDateField(TextView dateView, Calendar calendar) {
        Calendar cal = new GregorianCalendar();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        dateFormat.setTimeZone(cal.getTimeZone());
        dateView.setText(dateFormat.format(calendar.getTime()));
    }

    /**
     * Converts image to bitmap, sets image if it's existent
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data != null) {
            chosenImage = data.getData();
            if(chosenImage != null) {
                Bitmap myImg = BitmapFactory.decodeFile(FileUtils.getPath(this, chosenImage));
                Matrix matrix = new Matrix();
                Bitmap rotated = Bitmap.createBitmap(myImg, 0, 0, myImg.getWidth(), myImg.getHeight(),
                        matrix, true);
                image.setImageBitmap(rotated);
            }
        }
    }

    /**
     * Asks for permissions to pick image
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_STORAGE_REQUEST: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startImagePicker();
                } else {
                    showBasicError("Unable to pick image");
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
