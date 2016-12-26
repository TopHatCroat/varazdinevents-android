package hr.foi.varazdinevents.api.responses;

/**
 * Created by Antonio Martinović on 30.10.16.
 */

import com.google.gson.annotations.SerializedName;

/**
 * API response for event
 */
public class EventResponse {
    public Integer id;
    public String title;
    public String text;
    public Integer date;
    @SerializedName("date_to")
    public Integer dateTo;
    public String host;
    @SerializedName("official_link")
    public String officialLink;
    public String image;
    public String facebook;
    public String offers;
    public String category;
    @SerializedName("date_updated")
    public Integer lastUpdate;
}
