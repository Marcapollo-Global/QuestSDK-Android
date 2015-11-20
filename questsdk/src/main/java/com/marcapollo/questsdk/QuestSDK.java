package com.marcapollo.questsdk;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.marcapollo.questsdk.model.Beacon;
import com.marcapollo.questsdk.model.Flyer;
import com.marcapollo.questsdk.model.Notification;
import com.marcapollo.questsdk.model.Store;

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

                    Log.d(TAG, "app-uuid = " + mAppUUID);
                    Log.d(TAG, "token = " + mToken);

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

    public void listApplicationStores(QueryCallback<ListResult<Store>> callback) {
        Log.d(TAG, "listApplicationStores");

        new QueryRequestFacade<ListResult<Store>>(mToken, callback) {

            @Override
            protected Call<ListResult<Store>> performRequest() {
                AppService appService = createService(AppService.class);
                return appService.listAppStores(mAppUUID, mToken);
            }
        }.start();
    }

    public void listStoreBeacons(final String storeUUID, QueryCallback<ListResult<Beacon>> callback) {
        Log.d(TAG, "listStoreBeacons");

        new QueryRequestFacade<ListResult<Beacon>>(mToken, callback) {

            @Override
            protected Call<ListResult<Beacon>> performRequest() {
                StoreService storeService = createService(StoreService.class);
                return storeService.listStoreBeacons(storeUUID, mToken);
            }
        }.start();
    }

    public void listBeaconNotifications(final Beacon beacon, QueryCallback<ListResult<Notification>> callback) {
        Log.d(TAG, "listBeaconNotifications");

        new QueryRequestFacade<ListResult<Notification>>(mToken, callback) {

            @Override
            protected Call<ListResult<Notification>> performRequest() {
                BeaconService beaconService = createService(BeaconService.class);
                return beaconService.listBeaconNotifications(beacon.getUuid(),
                        beacon.getMajor(),
                        beacon.getMinor(),
                        mToken);
            }
        }.start();
    }

    public void listBeaconFlyers(final Beacon beacon, QueryCallback<ListResult<Flyer>> callback) {
        Log.d(TAG, "listBeaconFlyers");

        new QueryRequestFacade<ListResult<Flyer>>(mToken, callback) {

            @Override
            protected Call<ListResult<Flyer>> performRequest() {
                BeaconService beaconService = createService(BeaconService.class);
                return beaconService.listBeaconFlyers(beacon.getUuid(),
                        beacon.getMajor(),
                        beacon.getMinor(),
                        mToken);
            }
        }.start();
    }

    public void listBeaconStores(final Beacon beacon, QueryCallback<ListResult<Store>> callback) {
        Log.d(TAG, "listBeaconStores");

        new QueryRequestFacade<ListResult<Store>>(mToken, callback) {

            @Override
            protected Call<ListResult<Store>> performRequest() {
                BeaconService beaconService = createService(BeaconService.class);
                return beaconService.listBeaconStores(beacon.getUuid(),
                        beacon.getMajor(),
                        beacon.getMinor(),
                        mToken);
            }
        }.start();
    }
}
