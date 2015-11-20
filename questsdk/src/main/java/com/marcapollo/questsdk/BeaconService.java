package com.marcapollo.questsdk;

import com.marcapollo.questsdk.model.Flyer;
import com.marcapollo.questsdk.model.Notification;
import com.marcapollo.questsdk.model.Store;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by shinechen on 11/20/15.
 */
public interface BeaconService {
    @GET("/beacons/{uuid}/{major}/{minor}/stores/")
    Call<ListResult<Store>> listBeaconStores(@Path("uuid") String uuid,
                                          @Path("major") int major,
                                          @Path("minor") int minor,
                                          @Query("token") String token);

    @GET("/beacons/{uuid}/{major}/{minor}/notifications/")
    Call<ListResult<Notification>> listBeaconNotifications(@Path("uuid") String uuid,
                                          @Path("major") int major,
                                          @Path("minor") int minor,
                                          @Query("token") String token);

    @GET("/beacons/{uuid}/{major}/{minor}/flyers/")
    Call<ListResult<Flyer>> listBeaconFlyers(@Path("uuid") String uuid,
                                                           @Path("major") int major,
                                                           @Path("minor") int minor,
                                                           @Query("token") String token);
}
