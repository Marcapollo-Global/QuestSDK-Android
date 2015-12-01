package com.marcapollo.questsdk;

import android.util.Log;

import retrofit.Callback;
import retrofit.Response;

/**
 * Created by shinechen on 11/19/15.
 */
class QueryCallbackWrapper<T> implements Callback<T> {
    private static final String TAG = "ListCallbackWrapper";

    QueryCallback<T> callback;

    public QueryCallbackWrapper(QueryCallback<T> callback) {
        this.callback = callback;
    }

    @Override
    public void onResponse(Response<T> response) {
        Log.d(TAG, "onResponse");
        Log.d(TAG, response.raw().toString());

        T result = response.body();

        if (result == null) {
            Log.e(TAG, "null result");
            if (callback != null) {
                callback.onFailure(new Error("null result"));
            }
            return;
        }

        if (callback != null) {
            callback.onComplete(result);
        }
    }

    @Override
    public void onFailure(Throwable t) {
        callback.onFailure(t);
    }
}
