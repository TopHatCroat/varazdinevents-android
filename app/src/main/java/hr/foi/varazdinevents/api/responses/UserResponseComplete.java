package hr.foi.varazdinevents.api.responses;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Valentin Magdić on 02.01.17..
 */

/**
 * Complete API response for retrieving users
 */
public class UserResponseComplete {
    public List<UserResponse> items = new ArrayList<>();
    public LinksResponse links;
    public MetaResponse meta;
}
