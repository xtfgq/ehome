package com.zzu.ehome.utils;

/**
 * Created by Dell on 2016/3/14.
 */
public interface JsonAsyncTaskOnComplete {
 void processJsonObject(Object result);
 void onError(Exception e);
}
