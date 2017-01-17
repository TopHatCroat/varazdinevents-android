package hr.foi.varazdinevents.util;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Utility class containing static methods for font and text manipulation
 * Created by Antonio Martinović on 12.11.16.
 */
public class FontManager {
    public static final String ROOT = "fonts/";
    public static final String FONTAWESOME = ROOT + "fontawesome-webfont.ttf";

    /**
     * Returns a Font Awesome instance
     * @param context calling activity
     * @return Font Awesome
     */
    public static Typeface getFontAwesome(Context context) {
        return Typeface.createFromAsset(context.getAssets(), FONTAWESOME);
    }

    /**
     * Marks a text view or all views in a view group to be Font Awesome
     * which allows the icons to appear on screen
     * @param v target for Font Awesome
     * @param typeface font typeface
     */
    public static void markAsIconContainer(View v, Typeface typeface) {
        if (v instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) v;
            for (int i = 0; i < vg.getChildCount(); i++) {
                View child = vg.getChildAt(i);
                markAsIconContainer(child, typeface);
            }
        } else if (v instanceof TextView) {
            ((TextView) v).setTypeface(typeface);
        }
    }

    /**
     * Parses the HTML encoded string into Android friendly formating
     * @param text HTML raw string
     * @return parsed HTML
     */
    public static String parseHTML(String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY).toString();
        } else {
            return String.valueOf(Html.fromHtml(text));
        }
    }
}
