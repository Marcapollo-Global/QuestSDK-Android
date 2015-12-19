package com.marcapollo.questsdk.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.squareup.moshi.Json;

/**
 * Created by shinechen on 11/20/15.
 */
public class Store implements Parcelable {
    @Json(name="store_uuid")
    private String uuid = "";
    @Json(name="store_name")
    private String name = "";
    @Json(name="store_intro")
    private String introduction = "";
    @Json(name="store_address")
    private String address = "";
    @Json(name="store_tel")
    private String tel = "";
    @Json(name="store_website")
    private String website = "";
    @Json(name="store_booking")
    private String booking = "";
    @Json(name="store_note")
    private String note = "";
    @Json(name="store_logo")
    private String logo = "";
    @Json(name="store_listimg")
    private String listImg = "";
    @Json(name="store_headerimg")
    private String headerImg = "";
    @Json(name="store_longitude")
    private Double longitude;
    @Json(name="store_latitude")
    private Double latitude;
    @Json(name="store_wifi")
    private int wifi;
    @Json(name="store_icg")
    private int icg;
    @Json(name="store_chat")
    private int chat;
    @Json(name="store_flyer")
    private int flyer;

    protected Store(Parcel in) {
        uuid = in.readString();
        name = in.readString();
        introduction = in.readString();
        address = in.readString();
        tel = in.readString();
        website = in.readString();
        booking = in.readString();
        note = in.readString();
        logo = in.readString();
        listImg = in.readString();
        headerImg = in.readString();
        longitude = in.readDouble();
        latitude = in.readDouble();
        wifi = in.readInt();
        icg = in.readInt();
        chat = in.readInt();
        flyer = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uuid);
        dest.writeString(name);
        dest.writeString(introduction);
        dest.writeString(address);
        dest.writeString(tel);
        dest.writeString(website);
        dest.writeString(booking);
        dest.writeString(note);
        dest.writeString(logo);
        dest.writeString(listImg);
        dest.writeString(headerImg);
        dest.writeDouble(getLongitude());
        dest.writeDouble(getLatitude());
        dest.writeInt(wifi);
        dest.writeInt(icg);
        dest.writeInt(chat);
        dest.writeInt(flyer);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Store> CREATOR = new Creator<Store>() {
        @Override
        public Store createFromParcel(Parcel in) {
            return new Store(in);
        }

        @Override
        public Store[] newArray(int size) {
            return new Store[size];
        }
    };

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getIntroduction() {
        return introduction;
    }

    public String getAddress() {
        return address;
    }

    public String getTel() {
        return tel;
    }

    public String getWebsite() {
        return website;
    }

    public String getBooking() {
        return booking;
    }

    public String getNote() {
        return note;
    }

    public String getLogo() {
        return logo;
    }

    public String getListImg() {
        return listImg;
    }

    public String getHeaderImg() {
        return headerImg;
    }

    public double getLongitude() {
        if (longitude == null) {
            longitude = new Double(0);
        }
        return longitude;
    }

    public double getLatitude() {
        if (latitude == null) {
            latitude = new Double(0);
        }
        return latitude;
    }

    public boolean hasWifi() {
        return wifi > 0;
    }

    public boolean hasIcg() {
        return icg > 0;
    }

    public boolean hasChat() {
        return chat > 0;
    }

    public boolean hasFlyer() {
        return flyer > 0;
    }
}
