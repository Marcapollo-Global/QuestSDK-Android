package com.marcapollo.questsdk;

import android.text.TextUtils;
import android.util.Log;

import retrofit.Call;

/**
 * Created by shinechen on 11/19/15.
 */
abstract public class ListRequestFacade<T> {

    private static final String TAG = "ListRequestFacade";

    ListRequestCallback<ListResult<T>> mCallback;
    private String mToken;

    public ListRequestFacade(String token, ListRequestCallback<ListResult<T>> callback) {
        mToken = token;
        mCallback = callback;
    }

    abstract protected Call<ListResult<T>> performRequest();

    public void start() {
        if (TextUtils.isEmpty(mToken)) {
            Log.e(TAG, "Token is not set");
            if (mCallback != null) {
                mCallback.onFailure(new Error("Token is not set"));
            }
            return;
        }

        try {
            performRequest().enqueue(new ListCallbackWrapper<T>(mCallback));
        } catch (Exception e) {
            e.printStackTrace();
            if (mCallback != null) {
                mCallback.onFailure(e);
            }
        }
    }
}
