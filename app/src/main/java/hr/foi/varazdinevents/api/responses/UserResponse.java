package hr.foi.varazdinevents.api.responses;

/**
 * Created by Antonio Martinović on 13.11.16.
 */

import com.google.gson.annotations.SerializedName;

/***
 * API response for user log in
 */
public class UserResponse {
    public Integer id;
    public String username;
    public String email;
    public String password;
    public String token;
    public String description;
    public String workingTime;
    public String address;
    public String facebook;
    public String web;
    public String phone;
    public String image;
    @SerializedName("date_updated")
    public Integer lastUpdate;
}
