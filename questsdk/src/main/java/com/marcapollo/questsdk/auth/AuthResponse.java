package com.marcapollo.questsdk.auth;

import com.squareup.moshi.Json;

/**
 * Created by shinechen on 11/18/15.
 */
public class AuthResponse {
    @Json(name = "app_id")
    private String appId;
    @Json(name = "app_uuid")
    private String appUUID;
    @Json(name = "token")
    private String token;
    @Json(name = "expiration_date")
    private String expirationDate;
}
