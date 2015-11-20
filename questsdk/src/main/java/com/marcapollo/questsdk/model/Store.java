package com.marcapollo.questsdk.model;

import com.squareup.moshi.Json;

/**
 * Created by shinechen on 11/20/15.
 */
public class Store {
    @Json(name="store_uuid")
    private String uuid;
    @Json(name="store_name")
    private String name;
    @Json(name="store_intro")
    private String introduction;
    @Json(name="store_address")
    private String address;
    @Json(name="store_tel")
    private String tel;
    @Json(name="store_website")
    private String website;
    @Json(name="store_booking")
    private String booking;
    @Json(name="store_note")
    private String note;
    @Json(name="store_logo")
    private String logo;
    @Json(name="store_listimg")
    private String listImg;
    @Json(name="store_headerimg")
    private String headerImg;
    @Json(name="store_longitude")
    private double longitude;
    @Json(name="store_latitude")
    private double latitude;
    @Json(name="store_wifi")
    private int wifi;
    @Json(name="store_icg")
    private int icg;
    @Json(name="store_chat")
    private int chat;
    @Json(name="store_flyer")
    private int flyer;
}
