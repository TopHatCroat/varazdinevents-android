package hr.foi.varazdinevents.api;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Antonio Martinović on 06.11.16.
 */

public class EventResponseComplete {
    public List<EventResponse> items = new ArrayList<EventResponse>();
    public LinksResonse links;
    public MetaResponse meta;


}
