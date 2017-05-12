package hr.foi.varazdinevents.api.responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Antonio Martinović on 11/05/17.
 */

public class CityResponseComplete {
    @SerializedName("items")
    public CityResponse[] cities;

    public class CityResponse {
        public int id;
        public String name;
        public int postal_code;
        public double longitude;
        public double latitude;
    }
}
