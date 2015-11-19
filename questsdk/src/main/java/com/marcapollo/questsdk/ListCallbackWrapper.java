package com.marcapollo.questsdk;

import android.util.Log;

import retrofit.Callback;
import retrofit.Response;

/**
 * Created by shinechen on 11/19/15.
 */
public class ListCallbackWrapper<T> implements Callback<ListResult<T>> {
    private static final String TAG = "ListCallbackWrapper";

    ListRequestCallback<ListResult<T>> callback;

    public ListCallbackWrapper(ListRequestCallback<ListResult<T>> callback) {
        this.callback = callback;
    }

    @Override
    public void onResponse(Response<ListResult<T>> response) {
        Log.d(TAG, "onResponse");
        Log.d(TAG, response.raw().toString());

        ListResult<T> result = response.body();

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
