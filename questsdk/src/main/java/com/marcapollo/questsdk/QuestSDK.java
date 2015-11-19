package com.marcapollo.questsdk;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import java.util.UUID;

import retrofit.Call;
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

    private Context mContext;
    private String mAppKey;
    private String mUserUUID = "";
    // Application UUID, will be available after auth
    private String mAppUUID;
    // Session token, will be available after auth
    private String mToken;

    public static String getVersion() {
        return BuildConfig.VERSION_NAME + "." + BuildConfig.VERSION_CODE;
    }

    public static void init(Context context, String appKey) {
        sInstance = new QuestSDK(context);
        sInstance.setAppKey(appKey);
    }

    public static QuestSDK getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException("QuestSDK is not init");
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

    public void auth(final AuthCallback callback) {

        Log.d(TAG, "auth");

        if (TextUtils.isEmpty(mAppKey)) {
            Log.e(TAG, "App key is not set");
            if (callback != null) {
                callback.onFailure(new Error("App key is not set"));
            }
            return;
        }

        try {
            AuthService authService = createService(AuthService.class);
            authService.auth(new AuthRequest(mAppKey, mUserUUID, OS)).enqueue(new Callback<AuthResponse>() {
                @Override
                public void onResponse(Response<AuthResponse> response) {
                    Log.d(TAG, "onResponse");
                    Log.d(TAG, response.raw().toString());

                    AuthResponse authResponse = response.body();
                    if (authResponse == null) {
                        if (callback != null) {
                            callback.onFailure(new Error("Null response"));
                        }
                        return;
                    }

                    mAppUUID = authResponse.getAppUUID();
                    mToken = authResponse.getToken();

                    if (TextUtils.isEmpty(mAppUUID) || TextUtils.isEmpty(mToken)) {
                        if (callback != null) {
                            callback.onFailure(new Error("Unexpected response"));
                        }
                        return;
                    }

                    if (callback != null) {
                        callback.onComplete();
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    t.printStackTrace();
                    if (callback != null) {
                        callback.onFailure(t);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            if (callback != null) {
                callback.onFailure(e);
            }
        }
    }

    public void listApplicationBeacons(QueryCallback<ListResult<Beacon>> callback) {
        Log.d(TAG, "listApplicationBeacons");

        new QueryRequestFacade<ListResult<Beacon>>(mToken, callback) {
            @Override
            protected Call<ListResult<Beacon>> performRequest() {
                AppService appService = createService(AppService.class);
                return appService.listAppBeacons(mAppUUID, mToken);
            }
        }.start();
    }
}
