package com.marcapollo.questsdk;

import com.squareup.moshi.Json;

/**
 * Created by shinechen on 11/18/15.
 */
class AuthResponse {
    @Json(name = "app_id")
    private String appId;
    @Json(name = "app_uuid")
    private String appUUID;
    @Json(name = "token")
    private String token;
    @Json(name = "expiration_date")
    private String expirationDate;
    @Json(name = "latest_sdk")
    private SDKInfo latestSDK;
    @Json(name = "urls")
    private ContentURLs urls;

    public String getAppId() {
        return appId;
    }

    public String getAppUUID() {
        return appUUID;
    }

    public String getToken() {
        return token;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public SDKInfo getLatestSDK() {
        return latestSDK;
    }

    public ContentURLs getUrls() {
        return urls;
    }
}
