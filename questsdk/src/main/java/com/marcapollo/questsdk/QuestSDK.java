package com.marcapollo.questsdk;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.marcapollo.questsdk.model.Beacon;
import com.marcapollo.questsdk.model.Flyer;
import com.marcapollo.questsdk.model.Notification;
import com.marcapollo.questsdk.model.Store;

import org.altbeacon.beacon.BeaconManager;

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

    private static final String BASE_URL = "https://labsdk.quest-platform.com/v1/";

    private static final String OS = "android";

    private static QuestSDK sInstance;

    private String mAppKey;
    private String mUserUUID = "";
    // Application UUID, will be available after auth
    private String mAppUUID;
    // Session token, will be available after auth
    private String mToken;
    // Beacon flyer image content URL
    private String mBeaconFlyerImgURL;
    // Beacon flyer audio content URL
    private String mBeaconFlyerAudioURL;
    // Beacon flyer video content thumbnail URL
    private String mBeaconFlyerVideoImgURL;

    // Beacon monitor
    private BeaconMonitor mBeaconMonitor;

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
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        mUserUUID = sharedPreferences.getString(SHARED_PREF_KEY_USER_UUID, "");
        if (TextUtils.isEmpty(mUserUUID)) {
            mUserUUID = UUID.randomUUID().toString();
            sharedPreferences.edit().putString(SHARED_PREF_KEY_USER_UUID, mUserUUID).commit();
        }

        Log.d(TAG, "user uuid = " + mUserUUID);

        mBeaconMonitor = new BeaconMonitor(context);
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

    /**
     * Authorization and get session access token.
     * @param callback
     */
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

                    if (authResponse.getLatestSDK() != null &&
                            !authResponse.getLatestSDK().getVersion().contentEquals(getVersion())) {
                        Log.w(TAG, "!!!Attention!!! The latest QuestSDK version is " + authResponse.getLatestSDK().getVersion());
                        Log.w(TAG, "!!!Attention!!! Your current QuestSDK version is " + getVersion());
                        Log.w(TAG, "!!!Attention!!! For more details, please visit" + authResponse.getLatestSDK().getHomepage());
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

                    if (authResponse.getUrls() != null) {
                        mBeaconFlyerImgURL = authResponse.getUrls().getBeaconFlyerImg();
                        mBeaconFlyerAudioURL = authResponse.getUrls().getBeaconFlyerAudio();
                        mBeaconFlyerVideoImgURL = authResponse.getUrls().getBeaconFlyerVideoImg();

                        Log.d(TAG, "beacon flyer img URL = " + mBeaconFlyerImgURL);
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

    /**
     * List beacons registered under current application
     * @param callback
     */
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

    /**
     * List stores registered under current application
     * @param callback
     */
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

    /**
     * List beacons in a store
     * @param storeUUID Store UUID
     * @param callback
     */
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

    /**
     * List notifications of a beacon
     * @param beacon
     * @param callback
     */
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

    /**
     * List flyers of a beacon
     * @param beacon
     * @param callback
     */
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

    /**
     * List store(s) that the given beacon belongs to.
     * @param beacon
     * @param callback
     */
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

    /**
     * Start monitoring of a given beacon or a group of beacons.
     * @param beacon    If only UUID is specified, monitoring all the beacons with the given UUID.
     */
    public void startMonitoring(final Beacon beacon) {
        mBeaconMonitor.startMonitoring(beacon);
    }

    /**
     * Stop monitoring of a given beacon or a group of beacons.
     * @param beacon
     */
    public void stopMonitoring(Beacon beacon) {
        mBeaconMonitor.stopMonitoring(beacon);
    }

    /**
     * Set consumer that the beacon monitor result will be delivered to.
     * @param consumer
     */
    public void setBeaconMonitorConsumer(BeaconMonitorConsumer consumer) {
        mBeaconMonitor.setConsumer(consumer);
    }

    /**
     * Returns beacon flyer image content URL
     * @return Beacon flyer image content URL
     */
    public String getBeaconFlyerImgURL() {
        if (mBeaconFlyerImgURL == null) {
            mBeaconFlyerImgURL = "";
        }
        return mBeaconFlyerImgURL;
    }

    /**
     * Returns beacon flyer audio content URL
     * @return Beacon flyer audio content URL
     */
    public String getBeaconFlyerAudioURL() {
        if (mBeaconFlyerAudioURL == null) {
            mBeaconFlyerAudioURL = "";
        }
        return mBeaconFlyerAudioURL;
    }

    /**
     * Returns beacon flyer video content thumbnail URL
     * @return Beacon flyer vidoe content thumbnail URL
     */
    public String getBeaconFlyerVideoImgURL() {
        if (mBeaconFlyerVideoImgURL == null) {
            mBeaconFlyerVideoImgURL = "";
        }
        return mBeaconFlyerVideoImgURL;
    }
}
