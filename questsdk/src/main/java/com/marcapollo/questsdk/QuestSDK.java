package com.marcapollo.questsdk;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.marcapollo.questsdk.auth.AuthRequest;
import com.marcapollo.questsdk.auth.AuthResponse;
import com.marcapollo.questsdk.auth.AuthService;

import java.io.IOException;
import java.util.UUID;

import retrofit.Callback;
import retrofit.MoshiConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by shinechen on 11/18/15.
 */
public class QuestSDK {

    private static final String TAG = "QuestSDK";

    private static final String SHARED_PREF_NAME = "QuestSDK";
    private static final String SHARED_PREF_KEY_USER_UUID = "user_uuid";

    private static final String BASE_URL = "http://10.10.10.134:3000/v1/";

    private static final String OS = "android";

    private static QuestSDK sInstance;

    private String mAppKey;
    private String mUserUUID = "";

    public static String getVersion() {
        return BuildConfig.VERSION_NAME + "." + BuildConfig.VERSION_CODE;
    }

    public static QuestSDK getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new QuestSDK(context);
        }
        return sInstance;
    }

    private QuestSDK(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("QuestSDK", Context.MODE_PRIVATE);

        mUserUUID = sharedPreferences.getString(SHARED_PREF_KEY_USER_UUID, "");
        if (TextUtils.isEmpty(mUserUUID)) {
            mUserUUID = UUID.randomUUID().toString();
            sharedPreferences.edit().putString(SHARED_PREF_KEY_USER_UUID, mUserUUID).commit();
        }

        Log.d(TAG, "user uuid = " + mUserUUID);
    }

    public void setAppKey(String appKey) {
        mAppKey = appKey;
    }

    private static <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build();

        return retrofit.create(serviceClass);
    }

    public void auth() {

        Log.d(TAG, "auth");

        if (TextUtils.isEmpty(mAppKey)) {
            Log.e(TAG, "App key is not set");
            return;
        }

        AuthService authService = createService(AuthService.class);
        authService.auth(new AuthRequest(mAppKey, mUserUUID, OS)).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Response<AuthResponse> response) {
                Log.d(TAG, "onResponse");
                Log.d(TAG, response.raw().toString());
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
