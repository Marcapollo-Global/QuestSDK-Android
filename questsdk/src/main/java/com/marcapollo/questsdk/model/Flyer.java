package com.marcapollo.questsdk.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.marcapollo.questsdk.QuestSDK;
import com.squareup.moshi.Json;

/**
 * Created by shinechen on 11/20/15.
 */
public class Flyer implements Parcelable {

    public static final int FlyerTypeImage = 1;
    public static final int FlyerTypeVideo = 2;
    public static final int FlyerTypeWeb = 3;

    public static final int FlyerDistanceImmediate = 1;
    public static final int FlyerDistanceNear = 2;
    public static final int FlyerDistanceFar = 4;

    @Json(name="beacon_flyers_id")
    private String id = "";
    @Json(name="beacon_id")
    private String beaconId;
    @Json(name="beacon_flyers_order")
    private int order;

    @Json(name="beacon_flyers_type")
    /**
     * Flyer type.
     * @see FlyerTypeImage
     * @see FlyerTypeVideo
     * @see FlyerTypeWeb
     */
    private int type;

    @Json(name="beacon_flyers_content")
    private String content ="";

    @Json(name="beacon_flyers_islogin")
    /**
     * Whether login is required to see this flyer
     */
    private int login;

    @Json(name="beacon_flyers_distance")
    /**
     * What distance the flyer should notify or present.
     * Could be any or sum of FlyerDistanceImmediate, FlyerDistanceNear, FlyerDistanceFar
     */
    private int distance;

    @Json(name="beacon_flyers_activate")
    private int activate;

    @Json(name="beacon_flyers_audio_file")
    /**
     * File of audio content
     */
    private String audioFile = "";

    @Json(name="beacon_flyers_videoimg_file")
    /**
     * File of video content thumbnail
     */
    private String videoImageFile = "";

    @Json(name="coupon_id")
    private String couponId = "";
    @Json(name="beacon_flyers_text_title")
    private String textTitle = "";
    @Json(name="beacon_flyers_text_desc")
    private String textDescription = "";

    private String resolvedContentUrl;
    private String resolvedAudioUrl;
    private String resolvedVideoImgUrl;

    protected Flyer(Parcel in) {
        id = in.readString();
        beaconId = in.readString();
        order = in.readInt();
        type = in.readInt();
        content = in.readString();
        login = in.readInt();
        distance = in.readInt();
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
        dest.writeString(beaconId);
        dest.writeInt(order);
        dest.writeInt(type);
        dest.writeString(content);
        dest.writeInt(login);
        dest.writeInt(distance);
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

    /**
     * Returns resolved URL of content
     * @return Resolved URL of content
     */
    public String getContent() {

        if (!TextUtils.isEmpty(resolvedContentUrl)) {
            return resolvedContentUrl;
        }

        switch (getType()) {
            case FlyerTypeImage: {
                resolvedContentUrl = QuestSDK.getInstance().getBeaconFlyerImgURL();
                resolvedContentUrl = resolvedContentUrl.replace(":beacon_id", getBeaconId());
                resolvedContentUrl = resolvedContentUrl.replace(":beacon_flyers_distance", getDistance() + "");
                resolvedContentUrl = resolvedContentUrl.replace(":beacon_flyers_order", getOrder() + "");
                resolvedContentUrl = resolvedContentUrl.replace(":beacon_flyers_content", content);
                break;
            }
            case FlyerTypeVideo:
                resolvedContentUrl = content;
                break;
            default:
            case FlyerTypeWeb:
                resolvedContentUrl = content;
                break;
        }

        return resolvedContentUrl;
    }

    public boolean isLogin() {
        return login > 0;
    }

    public int getDistance() {
        return distance;
    }

    public boolean isActivate() {
        return activate > 0;
    }

    /**
     * Returns resolved URL of audio content
     * @return Resolved URL of audio content
     */
    public String getAudioFile() {
        if (TextUtils.isEmpty(audioFile)) {
            return "";
        }
        if (!TextUtils.isEmpty(resolvedAudioUrl)) {
            return resolvedAudioUrl;
        }
        resolvedAudioUrl = QuestSDK.getInstance().getBeaconFlyerAudioURL();
        resolvedAudioUrl = resolvedAudioUrl.replace(":beacon_id", getBeaconId());
        resolvedAudioUrl = resolvedAudioUrl.replace(":beacon_flyers_distance", getDistance() + "");
        resolvedAudioUrl = resolvedAudioUrl.replace(":beacon_flyers_order", getOrder() + "");
        resolvedAudioUrl = resolvedAudioUrl.replace(":beacon_flyers_audio_file", audioFile);

        return resolvedAudioUrl;
    }

    /**
     * Returns resolved URL of video content thumbnail
     * @return Resolved URL of video content thumbnail
     */
    public String getVideoImageFile() {
        if (TextUtils.isEmpty(videoImageFile)) {
            return "";
        }
        if (!TextUtils.isEmpty(resolvedVideoImgUrl)) {
            return resolvedVideoImgUrl;
        }
        resolvedVideoImgUrl = QuestSDK.getInstance().getBeaconFlyerAudioURL();
        resolvedVideoImgUrl = resolvedVideoImgUrl.replace(":beacon_id", getBeaconId());
        resolvedVideoImgUrl = resolvedVideoImgUrl.replace(":beacon_flyers_distance", getDistance() + "");
        resolvedVideoImgUrl = resolvedVideoImgUrl.replace(":beacon_flyers_order", getOrder() + "");
        resolvedVideoImgUrl = resolvedVideoImgUrl.replace(":beacon_flyers_videoimg_file", videoImageFile);

        return resolvedVideoImgUrl;
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

    public String getBeaconId() {
        return beaconId;
    }
}
