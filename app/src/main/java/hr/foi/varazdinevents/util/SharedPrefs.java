package hr.foi.varazdinevents.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Helper class for writing into and reading out of the Shared Preferences
 * Created by Antonio Martinović on 24.12.16.
 */
public class SharedPrefs {
    private static SharedPreferences sharedPreferences;

    public SharedPrefs(Context context) {
        sharedPreferences = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
    }

    /**
     * Reads a string value
     * @param key preference key
     * @param defValue default expected value
     * @return shared preference value, or {@code defValue} if not found
     */
    public static String read(String key, String defValue) {
        return sharedPreferences.getString(key, defValue);
    }

    /**
     * Writes a string value into shared preferences
     * @param key preference key for identification
     * @param value value to write
     */
    public static void write(String key, String value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString(key, value);
        prefsEditor.commit();
    }

    /**
     * Reads a boolean value
     * @param key preference key
     * @param defValue default expected value
     * @return shared preference value, or {@code defValue} if not found
     */
    public static boolean read(String key, boolean defValue) {
        return sharedPreferences.getBoolean(key, defValue);
    }

    /**
     * Writes a boolean value into shared preferences
     * @param key preference key for identification
     * @param value value to write
     */
    public static void write(String key, boolean value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putBoolean(key, value);
        prefsEditor.commit();
    }

    /**
     * Reads an integer value
     * @param key preference key
     * @param defValue default expected value
     * @return shared preference value, or {@code defValue} if not found
     */
    public static Integer read(String key, int defValue) {
        return sharedPreferences.getInt(key, defValue);
    }

    /**
     * Writes an integer value into shared preferences
     * @param key preference key for identification
     * @param value value to write
     */
    public static void write(String key, Integer value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putInt(key, value).commit();
    }
}