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
    @SerializedName("name")
    public String username;
    public String email;
    public String password;
    public String token;
    @SerializedName("about")
    public String description;
    @SerializedName("work_hours")
    public String workingTime;
    public String address;
    public String facebook;
    @SerializedName("website")
    public String web;
    public String phone;
    public String image;
    @SerializedName("date_updated")
    public Integer lastUpdate;
}
