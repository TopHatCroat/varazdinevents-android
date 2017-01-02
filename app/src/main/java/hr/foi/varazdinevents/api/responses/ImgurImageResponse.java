package hr.foi.varazdinevents.api.responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Antonio Martinović on 02.01.17.
 */
public class ImgurImageResponse {
    public int ups;
    public int downs;
    public String id;
    public String title;
    public String description;
    @SerializedName("account_url")
    public String accountUrl;
    public String link;
    @SerializedName("reddit_comments")
    public String redditComments;
    public String vote;
    @SerializedName("deletehash")
    public String deleteHash;
    public String topic;
    @SerializedName("datetime")
    public long dateTime;
    public boolean favorite;
    public boolean nsfw;
}
