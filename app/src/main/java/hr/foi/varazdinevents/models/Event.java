package hr.foi.varazdinevents.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.provider.SyncStateContract;

import java.util.HashMap;
import java.util.Map;

import hr.foi.varazdinevents.ui.elements.Listable;
import hr.foi.varazdinevents.util.Constants;

/**
 * Created by Antonio Martinović on 30.10.16.
 */
public class Event implements Listable, Parcelable{
    private Integer id;
    private Integer visible;
    private Integer dateAdded;
    private String title;
    private String text;
    private Integer date;
    private String time;
    private Integer dateTo;
    private String timeTo;
    private String host;
    private String officialLink;
    private String image;
    private String facebook;
    private Object offers;
    private Integer fbId;
    private Integer author;

    public Event(){}

    protected Event(Parcel in) {
        title = in.readString();
        text = in.readString();
        time = in.readString();
        timeTo = in.readString();
        host = in.readString();
        officialLink = in.readString();
        image = in.readString();
        facebook = in.readString();
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVisible() {
        return visible;
    }

    public void setVisible(Integer visible) {
        this.visible = visible;
    }

    public Integer getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Integer dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getDate() {
        return date;
    }

    public void setDate(Integer date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getDateTo() {
        return dateTo;
    }

    public void setDateTo(Integer dateTo) {
        this.dateTo = dateTo;
    }

    public String getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(String timeTo) {
        this.timeTo = timeTo;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getOfficialLink() {
        return officialLink;
    }

    public void setOfficialLink(String officialLink) {
        this.officialLink = officialLink;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public Object getOffers() {
        return offers;
    }

    public void setOffers(Object offers) {
        this.offers = offers;
    }

    public Integer getFbId() {
        return fbId;
    }

    public void setFbId(Integer fbId) {
        this.fbId = fbId;
    }

    public Integer getAuthor() {
        return author;
    }

    public void setAuthor(Integer author) {
        this.author = author;
    }

    @Override
    public int getType() {
        return Constants.EVENTS_SIMPLE_CARD;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(text);
        parcel.writeString(time);
        parcel.writeString(timeTo);
        parcel.writeString(host);
        parcel.writeString(officialLink);
        parcel.writeString(image);
        parcel.writeString(facebook);
    }
}
