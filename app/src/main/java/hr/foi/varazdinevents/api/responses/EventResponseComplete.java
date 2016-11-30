package hr.foi.varazdinevents.api.responses;


import java.util.ArrayList;
import java.util.List;

import hr.foi.varazdinevents.api.responses.EventResponse;
import hr.foi.varazdinevents.api.responses.LinksResponse;
import hr.foi.varazdinevents.api.responses.MetaResponse;

/**
 * Created by Antonio Martinović on 06.11.16.
 */

/**
 * Complete API response for retrieving events
 */
public class EventResponseComplete {
    public List<EventResponse> items = new ArrayList<EventResponse>();
    public LinksResponse links;
    public MetaResponse meta;


}
