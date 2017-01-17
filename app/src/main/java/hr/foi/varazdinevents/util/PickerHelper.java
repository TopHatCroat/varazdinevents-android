package hr.foi.varazdinevents.util;

import android.app.Activity;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

import hr.foi.varazdinevents.places.events.MainActivity;

/**
 * Helper class with methods for creating date/time pickers
 * Created by Antonio Martinović on 04.12.16.
 */
public class PickerHelper {
    /**
     * Creates a calendar picker with default date set to today
     * @param tag picker tag, necessary to ID the picker if more are present in the activity
     * @param activity target activity for picker
     * @param listener listener function to be called on finish
     */
    public static void createDatePicker(String tag, Activity activity,
                                        DatePickerDialog.OnDateSetListener listener) {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                listener,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setVersion(DatePickerDialog.Version.VERSION_2);
        dpd.show(activity.getFragmentManager(), tag);
    }

    /**
     * Creates a time picker with default time set to now
     * @param tag picker tag, necessary to ID the picker if more are present in the activity
     * @param activity target activity for picker
     * @param listener listener function to be called on finish
     */
    public static void createTimePicker(String tag, Activity activity,
                                        TimePickerDialog.OnTimeSetListener listener) {
        Calendar now = Calendar.getInstance();
        TimePickerDialog dpd = TimePickerDialog.newInstance(
                listener,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true
        );
        dpd.setVersion(TimePickerDialog.Version.VERSION_2);
        dpd.show(activity.getFragmentManager(), tag);
    }
}
