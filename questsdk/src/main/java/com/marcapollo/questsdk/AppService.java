package com.marcapollo.questsdk;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by shinechen on 11/19/15.
 */
interface AppService {
    @GET("/apps/{appUUID}/beacons/")
    Call<ListResult<Beacon>> listAppBeacons(@Path("appUUID") String uuid, @Query("token") String token);
}
