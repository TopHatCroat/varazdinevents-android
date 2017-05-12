package hr.foi.varazdinevents.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.test.espresso.core.deps.guava.base.Strings;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

import hr.foi.varazdinevents.util.Constants;

/**
 * Created by Antonio Martinović on 09.11.16.
 */
public class User extends SugarRecord implements Parcelable{
    @Unique
    public Integer apiId;
    public String username;
    public String email;
    public String token;
    public String description;
    public String workingTime;
    public String address;
    public String facebook;
    public String web;
    public String phone;
    public String image;

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public User() {}

    public User(String username, String password) {
        this(username, null, password);
    }

    public User(String username, String email, String password) {
        this(0, username, email, password, null, null, null, null, null, null, null);
    }

    public User(Integer apiId, String username, String email, String token, String description, String workingTime, String address, String facebook, String web, String phone, String image) {
        this.apiId = apiId;
        this.username = username;
        this.email = email;
        this.token = token;
        this.description = description;
        this.workingTime = workingTime;
        this.address = address;
        this.facebook = facebook;
        this.web = web;
        this.phone = phone;
        this.image = image;
    }

    protected User(Parcel in) {
        this.apiId = in.readInt();
        this.username = in.readString();
        this.email = in.readString();
        this.token = in.readString();
        this.description = in.readString();
        this.workingTime = in.readString();
        this.address = in.readString();
        this.facebook = in.readString();
        this.web = in.readString();
        this.phone = in.readString();
        this.image = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.apiId);
        dest.writeString(this.username);
        dest.writeString(this.email);
        dest.writeString(this.token);
        dest.writeString(this.description);
        dest.writeString(this.workingTime);
        dest.writeString(this.address);
        dest.writeString(this.facebook);
        dest.writeString(this.web);
        dest.writeString(this.phone);
        dest.writeString(this.image);
    }

//    @Override
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) { this.token = token; }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWorkingTime() {
        return workingTime;
    }

    public void setWorkingTime(String workingTime) {
        this.workingTime = workingTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
