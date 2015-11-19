package com.marcapollo.questsdk;

import com.squareup.moshi.Json;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shinechen on 11/19/15.
 */
public class ListResult<T> {
    @Json(name="count")
    private int count;
    @Json(name="data")
    private List<T> data;

    public int getCount() {
        return count;
    }

    public List<T> getData() {
        if (data == null) {
            data = new ArrayList<T>();
        }
        return data;
    }

}
