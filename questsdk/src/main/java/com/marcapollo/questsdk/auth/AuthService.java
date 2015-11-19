package com.marcapollo.questsdk.auth;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by shinechen on 11/18/15.
 */
public interface AuthService {
    @POST("auth")
    Call<AuthResponse> auth(@Body AuthRequest request);
}
