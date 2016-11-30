package hr.foi.varazdinevents.api.responses;

/**
 * Created by Antonio Martinović on 09.11.16.
 */

/**
 * Link response holding link of the AII call
 */
public class LinksResponse {
    public Self self;

    /**
     * The link of the response
     */
    public class Self {
        public String href;
    }

}
