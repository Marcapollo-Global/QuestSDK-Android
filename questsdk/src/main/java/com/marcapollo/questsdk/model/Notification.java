package com.marcapollo.questsdk.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.squareup.moshi.Json;

/**
 * Created by shinechen on 11/20/15.
 */
public class Notification implements Parcelable {
    @Json(name="notification_beacon_id")
    private String id = "";
    @Json(name="notification_beacon_msg")
    private String message = "";

    protected Notification(Parcel in) {
        id = in.readString();
        message = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(message);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Notification> CREATOR = new Creator<Notification>() {
        @Override
        public Notification createFromParcel(Parcel in) {
            return new Notification(in);
        }

        @Override
        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }
}
