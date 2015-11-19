package com.marcapollo.questsdk;

/**
 * Created by shinechen on 11/19/15.
 */
public interface ListRequestCallback<T> {
    void onComplete(T result);
    void onFailure(Throwable t);
}
