package com.marcapollo.questsdk;

import com.squareup.moshi.Json;

/**
 * Created by shinechen on 11/19/15.
 */
public class Beacon {
    @Json(name="beacon_uuid")
    private String uuid;
    @Json(name="beacon_major")
    private int major;
    @Json(name="beacon_minor")
    private int minor;
}
