package com.marcapollo.questsdk;

import android.text.TextUtils;
import android.util.Log;

import java.security.spec.InvalidParameterSpecException;
import java.util.Objects;

import retrofit.Call;

/**
 * Created by shinechen on 11/19/15.
 */
abstract class QueryRequestFacade<T> {

    private static final String TAG = "ListRequestFacade";

    private String mToken;
    QueryCallback<T> mCallback;

    public QueryRequestFacade(String token, QueryCallback<T> callback) {
        mToken = token;
        mCallback = callback;
    }

    abstract protected Call<T> performRequest();

    public void start() {
        if (TextUtils.isEmpty(mToken)) {
            Log.e(TAG, "Token is not set");
            if (mCallback != null) {
                mCallback.onFailure(new Error("Token is not set"));
            }
            return;
        }

        try {
            performRequest().enqueue(new QueryCallbackWrapper<T>(mCallback));
        } catch (Exception e) {
            e.printStackTrace();
            if (mCallback != null) {
                mCallback.onFailure(e);
            }
        }
    }
}
