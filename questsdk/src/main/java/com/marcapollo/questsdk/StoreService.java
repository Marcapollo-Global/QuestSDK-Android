package com.marcapollo.questsdk;

import com.marcapollo.questsdk.model.Beacon;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by shinechen on 11/20/15.
 */
interface StoreService {
    @GET("/stores/{storeUUID}/beacons/")
    Call<ListResult<Beacon>> listStoreBeacons(@Path("storeUUID") String uuid, @Query("token") String token);
}
