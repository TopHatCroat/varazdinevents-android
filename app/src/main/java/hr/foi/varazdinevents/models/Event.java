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
    public Integer id;
    public String title;
    public String text;
    public Integer date;
    public Integer dateTo;
    public String host;
    public String officialLink;
    public String image;
    public String facebook;
    public String offers;
    public String category;

    public Event(){}

    protected Event(Parcel in) {
        title = in.readString();
        text = in.readString();
        date = in.readInt(); //getDate() != null ? in.readInt() : 0;
        dateTo = in.readInt(); //getDateTo() != null ? in.readInt() : 0;
        host = in.readString();
        officialLink = in.readString();
        image = in.readString();
        facebook = in.readString();
        offers = in.readString();
        category = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(text);
        //parcel.writeInt(date);
        //parcel.writeInt(dateTo);
        parcel.writeInt(date != null ? date : 0);
        parcel.writeInt(dateTo != null ? dateTo : 0);
        parcel.writeString(host);
        parcel.writeString(officialLink);
        parcel.writeString(image);
        parcel.writeString(facebook);
        parcel.writeString(offers);
        parcel.writeString(category);
    }

    @Override
    public int getType() {
        return Constants.EVENTS_SIMPLE_CARD;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getDateTo() {
        return dateTo;
    }

    public void setDateTo(Integer dateTo) {
        this.dateTo = dateTo;
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

    public String getOffers() {
        return offers;
    }

    public void setOffers(String offers) {
        this.offers = offers;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
