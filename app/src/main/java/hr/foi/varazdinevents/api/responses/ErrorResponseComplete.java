package hr.foi.varazdinevents.api.responses;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Antonio Martinović on 03.12.16.
 */

/**
 * API new event creation error response
 */
public class ErrorResponseComplete {
    public List<ErrorResponse> errors = new ArrayList<>();
}
