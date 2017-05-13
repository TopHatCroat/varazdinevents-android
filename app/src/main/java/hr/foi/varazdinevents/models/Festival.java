package hr.foi.varazdinevents.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

/**
 * Created by Antonio Martinović on 14/05/17.
 */

public class Festival extends SugarRecord implements Parcelable {
    public static final Creator<Festival> CREATOR = new Creator<Festival>() {
        @Override
        public Festival createFromParcel(Parcel in) {
            return new Festival(in);
        }

        @Override
        public Festival[] newArray(int size) {
            return new Festival[size];
        }
    };
    @Unique
    public Integer apiId;
    public String title;
    public Double longitude;
    public Double latitude;

    public Festival(Parcel in) {
        apiId = in.readInt();
        title = in.readString();
        longitude = in.readDouble();
        latitude = in.readDouble();
    }

    public Festival() {

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
        parcel.writeInt(apiId);
        parcel.writeString(title);
        parcel.writeDouble(longitude);
        parcel.writeDouble(latitude);
    }
}
