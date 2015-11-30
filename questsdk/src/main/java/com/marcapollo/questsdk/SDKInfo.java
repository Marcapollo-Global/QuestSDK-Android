package com.marcapollo.questsdk;

import com.squareup.moshi.Json;

/**
 * Created by shinechen on 11/30/15.
 */
public class SDKInfo {
    @Json(name = "version")
    private String version;
    @Json(name = "homepage")
    private String homepage;

    public String getVersion() {
        return version;
    }

    public String getHomepage() {
        return homepage;
    }
}
