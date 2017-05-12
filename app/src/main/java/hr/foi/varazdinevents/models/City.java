package hr.foi.varazdinevents.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

/**
 * Created by Antonio Martinović on 11/05/17.
 */

public class City extends SugarRecord implements Parcelable {
    public static final Creator<City> CREATOR = new Creator<City>() {
        @Override
        public City createFromParcel(Parcel in) {
            return new City(in);
        }

        @Override
        public City[] newArray(int size) {
            return new City[size];
        }
    };
    @Unique
    public Integer apiId;
    public String title;
    public Integer zip;
    public Double longitude;
    public Double latitude;

    public City() {
    }

    public City(Integer apiId, String title, Integer zip, Double longitude, Double latitude) {
        this.apiId = apiId;
        this.title = title;
        this.zip = zip;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    protected City(Parcel in) {
        title = in.readString();
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

    public Integer getZip() {
        return zip;
    }

    public void setZip(Integer zip) {
        this.zip = zip;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
    }
}
