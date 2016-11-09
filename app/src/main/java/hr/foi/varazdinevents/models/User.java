package hr.foi.varazdinevents.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Antonio Martinović on 09.11.16.
 */
public class User implements Parcelable{
    public long id;
    public String email;
    public String password;

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

    public User(long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    protected User(Parcel in) {
        this.id = in.readLong();
        this.email = in.readString();
        this.password = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.email);
        dest.writeString(this.password);
    }
}
