package com.marcapollo.questsdk;

/**
 * Created by shinechen on 11/18/15.
 */
class AuthRequest {

    private String app_key;
    private String user_uuid;
    private String os;

    public AuthRequest(String appKey, String userUUID, String os) {
        this.app_key = appKey;
        this.user_uuid = userUUID;
        this.os = os;
    }
}
