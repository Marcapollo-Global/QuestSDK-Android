package com.marcapollo.questsdk.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.squareup.moshi.Json;

/**
 * Created by shinechen on 11/19/15.
 */
public class Beacon implements Parcelable{
    @Json(name="beacon_uuid")
    private String uuid ="";
    @Json(name="beacon_major")
    private int major;
    @Json(name="beacon_minor")
    private int minor;
    @Json(name="beacon_tag_name")
    private String tagName = "";

    protected Beacon(Parcel in) {
        uuid = in.readString();
        major = in.readInt();
        minor = in.readInt();
        tagName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uuid);
        dest.writeInt(major);
        dest.writeInt(minor);
        dest.writeString(tagName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Beacon> CREATOR = new Creator<Beacon>() {
        @Override
        public Beacon createFromParcel(Parcel in) {
            return new Beacon(in);
        }

        @Override
        public Beacon[] newArray(int size) {
            return new Beacon[size];
        }
    };

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getMajor() {
        return major;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public int getMinor() {
        return minor;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
