package com.marcapollo.questsdk.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.squareup.moshi.Json;

/**
 * Created by shinechen on 11/20/15.
 */
public class Flyer implements Parcelable {
    @Json(name="beacon_flyers_id")
    private String id = "";
    @Json(name="beacon_flyers_order")
    private int order;
    @Json(name="beacon_flyers_type")
    private int type;
    @Json(name="beacon_flyers_content")
    private String content ="";
    @Json(name="beacon_flyers_islogin")
    private int login;
    @Json(name="beacon_flyers_distance")
    private double distance;
    @Json(name="beacon_flyers_activate")
    private int activate;
    @Json(name="beacon_flyers_audio_file")
    private String audioFile = "";
    @Json(name="beacon_flyers_videoimg_file")
    private String videoImageFile = "";
    @Json(name="coupon_id")
    private String couponId = "";
    @Json(name="beacon_flyers_text_title")
    private String textTitle = "";
    @Json(name="beacon_flyers_text_desc")
    private String textDescription = "";

    protected Flyer(Parcel in) {
        id = in.readString();
        order = in.readInt();
        type = in.readInt();
        content = in.readString();
        login = in.readInt();
        distance = in.readDouble();
        activate = in.readInt();
        audioFile = in.readString();
        videoImageFile = in.readString();
        couponId = in.readString();
        textTitle = in.readString();
        textDescription = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeInt(order);
        dest.writeInt(type);
        dest.writeString(content);
        dest.writeInt(login);
        dest.writeDouble(distance);
        dest.writeInt(activate);
        dest.writeString(audioFile);
        dest.writeString(videoImageFile);
        dest.writeString(couponId);
        dest.writeString(textTitle);
        dest.writeString(textDescription);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Flyer> CREATOR = new Creator<Flyer>() {
        @Override
        public Flyer createFromParcel(Parcel in) {
            return new Flyer(in);
        }

        @Override
        public Flyer[] newArray(int size) {
            return new Flyer[size];
        }
    };

    public String getId() {
        return id;
    }

    public int getOrder() {
        return order;
    }

    public int getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public boolean isLogin() {
        return login > 0;
    }

    public double getDistance() {
        return distance;
    }

    public boolean isActivate() {
        return activate > 0;
    }

    public String getAudioFile() {
        return audioFile;
    }

    public String getVideoImageFile() {
        return videoImageFile;
    }

    public String getCouponId() {
        return couponId;
    }

    public String getTextTitle() {
        return textTitle;
    }

    public String getTextDescription() {
        return textDescription;
    }
}
