package com.marcapollo.questsdk;

import com.squareup.moshi.Json;

/**
 * Created by shinechen on 11/30/15.
 */
class ContentURLs {
    @Json(name = "beacon_flyer_img")
    private String beaconFlyerImg;
    @Json(name = "beacon_flyer_audio")
    private String beaconFlyerAudio;
    @Json(name = "beacon_flyer_video_thumbnail")
    private String beaconFlyerVideoImg;

    public String getBeaconFlyerImg() {
        return beaconFlyerImg;
    }

    public String getBeaconFlyerAudio() {
        return beaconFlyerAudio;
    }

    public String getBeaconFlyerVideoImg() {
        return beaconFlyerVideoImg;
    }
}
