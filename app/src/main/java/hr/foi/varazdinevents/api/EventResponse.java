package hr.foi.varazdinevents.api;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Antonio Martinović on 30.10.16.
 */
public class EventResponse {
    public Integer id;
    public Integer visible;
    public Integer dateAdded;
    public String title;
    public String text;
    public Integer date;
    public String time;
    public Integer dateTo;
    public String timeTo;
    public String host;
    public String officialLink;
    public String image;
    public String facebook;
    public Object offers;
    public Integer fbId;
    public Integer author;
    public Map<String, Object> additionalProperties = new HashMap<String, Object>();

}
