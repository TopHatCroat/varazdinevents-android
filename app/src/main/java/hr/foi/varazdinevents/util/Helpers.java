package hr.foi.varazdinevents.util;

/**
 * Created by Antonio Martinović on 11/05/17.
 */

public class Helpers {
    /**
     * Calculate distance between two points in latitude and longitude taking
     * into account height difference. If you are not interested in height
     * difference pass 0.0. Uses Haversine method as its base.
     *
     * lat1, lon1 Start point lat2, lon2 End point el1 Start altitude in meters
     * el2 End altitude in meters
     * @returns Distance in Meters
     */
    public static double distance(double lat1, double lat2, double lon1, double lon2) {
        return Math.sqrt(Math.pow((lat1-lat2), 2) + Math.pow((lon1-lon2), 2));
    }
}
