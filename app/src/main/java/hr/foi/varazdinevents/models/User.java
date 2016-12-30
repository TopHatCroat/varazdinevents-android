package hr.foi.varazdinevents.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;

/**
 * Created by Antonio Martinović on 09.11.16.
 */
public class User extends SugarRecord implements Parcelable{
    private Integer apiId;
    private String username;
    private String email;
    private String password;
    private String token;

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public User() {
    }

    public User(String username, String password) {
        this(username, null, password);
    }

    public User(String username, String email, String password) {
        this(0, username, email, password, null);
    }

    public User(Integer apiId, String username, String email, String password, String token) {
        this.apiId = apiId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.token = token;
    }

    protected User(Parcel in) {
        this.apiId = in.readInt();
        this.username = in.readString();
        this.email = in.readString();
        this.password = in.readString();
        this.token = in.readString();
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
        dest.writeString(this.password);
        dest.writeString(this.token);
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) { this.token = token; }
}
