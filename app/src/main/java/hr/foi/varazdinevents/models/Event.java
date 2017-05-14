package hr.foi.varazdinevents.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.test.espresso.core.deps.guava.base.Strings;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;
import com.orm.dsl.Unique;

import hr.foi.varazdinevents.ui.elements.list.Listable;
import hr.foi.varazdinevents.ui.elements.Searchable;
import hr.foi.varazdinevents.util.Constants;

/**
 * Created by Antonio Martinović on 30.10.16.
 */

public class Event extends SugarRecord implements Listable<Event>, Searchable, Parcelable{
    @Unique
    public Integer apiId;
    public String title;
    public String text;
    public Long date;
    public Long dateTo;
    public String host;
    public Integer hostApiId;
    public String officialLink;
    public String image;
    public String facebook;
    public String offers;
    public String category;
    public Integer dateUpdated;

    @Ignore
    public boolean isHidden = false;

    public boolean isFavorite = false;

    public boolean isNotified = false;
    private Double longitude;
    private Double latitude;
    private String address;
    private Integer festivalId;

    public Event(){}

    protected Event(Parcel in) {
        apiId = in.readInt();
        title = in.readString();
        text = in.readString();
        date = in.readLong(); //getDate() != null ? in.readInt() : 0;
        dateTo = in.readLong(); //getDateTo() != null ? in.readInt() : 0;
        host = in.readString();
        officialLink = in.readString();
        image = in.readString();
        facebook = in.readString();
        offers = in.readString();
        category = in.readString();
        isFavorite = in.readByte() != 0;
        longitude = in.readDouble();
        latitude = in.readDouble();
        address = in.readString();
        festivalId = in.readInt();
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
        parcel.writeInt(apiId);
        parcel.writeString(title);
        parcel.writeString(text);
        parcel.writeLong(date != null ? date : 0);
        parcel.writeLong(dateTo != null ? dateTo : 0);
        parcel.writeString(host);
        parcel.writeString(officialLink);
        parcel.writeString(image);
        parcel.writeString(facebook);
        parcel.writeString(offers);
        parcel.writeString(category);
        parcel.writeByte((byte) (isFavorite ? 1 : 0));     //
        parcel.writeDouble(longitude != null ? longitude : 0.0);
        parcel.writeDouble(latitude != null ? latitude : 0.0);
        parcel.writeString(address);
        parcel.writeInt(festivalId != null ? festivalId : -1);
    }

    @Override
    public int getType() {
        if(Strings.isNullOrEmpty(this.image) || this.image.equals("http://cms.varazdinevents.cf")) {
            return Constants.EVENTS_NO_IMAGE_CARD;
        }
        return Constants.EVENTS_SIMPLE_CARD;
    }

    public Integer getApiId() {
        return apiId;
    }

    public void setApiId(Integer apiId) {
        this.apiId = apiId;
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

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Long getDateTo() {
        return dateTo;
    }

    public void setDateTo(Long dateTo) {
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

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public boolean isNotified() { return isNotified; }

    public void setNotified(boolean notified) { this.isNotified = notified; }

    public Integer getHostApiId() {
        return hostApiId;
    }

    public void setHostApiId(Integer hostApiId) {
        this.hostApiId = hostApiId;
    }

    public Integer getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Integer dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    @Override
    public boolean isMatching(String query) {
        if (query.isEmpty())
            return true;

        StringBuilder descriptor = new StringBuilder();
        descriptor.append(this.getTitle().toLowerCase());
//        descriptor.append(this.getText().toLowerCase());
//        descriptor.append(this.getOffers().toLowerCase());
//        descriptor.append(this.getOffers().toLowerCase());

        for (String part : query.split("\\s")) { //splits query string on every white space
            if(!descriptor.toString().contains(part)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int compareTo(Event event) {
        return event.date > this.date ? -1 : 1;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setFestivalId(Integer festivalId) {
        this.festivalId = festivalId;
    }

    public String getAddress() {
        return address;
    }

    public Integer getFestivalId() {
        return festivalId;
    }
}
