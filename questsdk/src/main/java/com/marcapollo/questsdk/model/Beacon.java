package com.marcapollo.questsdk.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.squareup.moshi.Json;

/**
 * Created by shinechen on 11/19/15.
 */
public class Beacon implements Parcelable{
    /**
     * Beacon UUID
     */
    @Json(name="beacon_uuid")
    private String uuid ="";
    @Json(name="beacon_major")
    /**
     * Major number
     */
    private int major;
    @Json(name="beacon_minor")
    /**
     * Minor number
     */
    private int minor;
    /**
     * Beacon tag name
     */
    @Json(name="beacon_tag_name")
    private String tagName = "";

    /**
     * Detected signal strength, will be set for beacons from monitor result.
     */
    private int rssi;
    /**
     * Estimated distance in meters, will be set for beacons from monitor result.
     */
    private double distance;

    public Beacon(String uuid, int major, int minor) {
        this.uuid = uuid;
        this.major = major;
        this.minor = minor;
    }

    protected Beacon(Parcel in) {
        uuid = in.readString();
        major = in.readInt();
        minor = in.readInt();
        tagName = in.readString();
        rssi = in.readInt();
        distance = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uuid);
        dest.writeInt(major);
        dest.writeInt(minor);
        dest.writeString(tagName);
        dest.writeInt(rssi);
        dest.writeDouble(distance);
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

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
