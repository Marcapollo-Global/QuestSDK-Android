package com.marcapollo.questsdk;

/**
 * Created by shinechen on 11/19/15.
 */
public interface AuthCallback {
    void onComplete();
    void onFailure(Throwable t);
}
