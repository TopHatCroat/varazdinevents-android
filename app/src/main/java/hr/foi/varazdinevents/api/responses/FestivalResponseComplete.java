package hr.foi.varazdinevents.api.responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Antonio Martinović on 14/05/17.
 */

public class FestivalResponseComplete {
    @SerializedName("items")
    public FestivalResponse[] festivals;

    public class FestivalResponse {
        public int id;
        public String name;
        public double longitude;
        public double latitude;
    }
}
