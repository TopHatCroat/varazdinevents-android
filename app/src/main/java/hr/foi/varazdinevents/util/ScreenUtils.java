package hr.foi.varazdinevents.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

/**
 * Helper class holding useful methods for determining screen size and
 * manipulating density-independent pixels
 * Created by Antonio Martinović on 15.01.17.
 */
public class ScreenUtils {
    private static int screenWidth = 0;
    private static int screenHeight = 0;

    /**
     * Calculates the correct pixel size of dp presented value specific
     * to the currently used device
     * @param dp size in dp
     * @return size in px
     */
    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    /**
     * Determines the height of the current screen
     * @param context activity context
     * @return height of the screen in pixels
     */
    public static int getScreenHeight(Context context) {
        if (screenHeight == 0) {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenHeight = size.y;
        }

        return screenHeight;
    }

    /**
     * Determines the width of the current screen
     * @param context activity context
     * @return width of the screen in pixels
     */
    public static int getScreenWidth(Context context) {
        if (screenWidth == 0) {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
        }

        return screenWidth;
    }

}
