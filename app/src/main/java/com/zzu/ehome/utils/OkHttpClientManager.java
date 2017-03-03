package com.zzu.ehome.utils;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Mersens on 2016/9/12.
 */
public class OkHttpClientManager {
    public final static int CONNECT_TIMEOUT =80;
    public final static int READ_TIMEOUT=120;
    public final static int WRITE_TIMEOUT=80;

    /**
     * 网络请求接口，用于数据回传
     */
    public interface RequestCallBack {
        //以字符串的形式返回请求成功数据
        void onSueecss(String msg);
        //以字符串的形式返回请求失败数据
        void onError(String msg);
        void onStart();
        void onFinish();
    }
    private static OkHttpClientManager manager;//管理者实例
    private OkHttpClient mClient;//OkHttpClient实例
    private OkHttpClientManager() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(READ_TIMEOUT, TimeUnit.MICROSECONDS);
        builder.writeTimeout(WRITE_TIMEOUT,TimeUnit.SECONDS);
        builder.connectTimeout(CONNECT_TIMEOUT,TimeUnit.SECONDS);
        builder.retryOnConnectionFailure(true);
        mClient=builder.build();

    }

    //单例模式，对提供管理者实例
    public static OkHttpClientManager getInstance() {
        if (manager == null) {
            synchronized (OkHttpClientManager.class){
                if(manager==null){
                    manager = new OkHttpClientManager();
                }
            }
        }
        return manager;
    }

    /**
     * get请求
     * @param url
     * @param callBack
     */
    public void doGet(String url, RequestCallBack callBack) {
        final Request request = new Request.Builder().url(url).get().build();
        doRequest(request, callBack);
    }

    /**
     * post请求
     * @param url
     * @param map
     * @param callBack
     */
    public void doPost(String url, Map<String, String> map, RequestCallBack callBack) {
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder().post(requestBody).url(url).build();
        doRequest(request, callBack);
    }

    /**
     * 上传文件
     * @param url
     * @param file
     * @param callBack
     */
    public void doUpload(String url, File file, String filename,final RequestCallBack callBack) {
        if (file == null) {
            Log.e("OkHttpClientManager", "文件为空！");
            return;
        }

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        addParams(builder);
        RequestBody fileBody = RequestBody.create(MediaType.parse(guessMimeType(file.getName())), file);

        builder.addFormDataPart("upload", filename,fileBody);
        RequestBody requestBody=builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)

                .build();
        doRequest(request, callBack);


    }

    /**
     * OKHTTP+Rxjava执行异步请求
     * @param request
     * @param callBack
     */
    public void doRequest(final Request request, final RequestCallBack callBack) {
        callBack.onStart();
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(final Subscriber<? super Object> subscriber) {

                try {
                    Response response = mClient.newCall(request).execute();
                    if (response.isSuccessful()) {
                        subscriber.onNext(response.body().string());
                        subscriber.onCompleted();
                    } else {
                        subscriber.onError(new IOException("Unexpected code " + response));
                        subscriber.onCompleted();
                    }
                } catch (IOException e) {
                    callBack.onError(e.toString());
                    e.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.io())//设置执行耗时操作（线程池）
                .observeOn(AndroidSchedulers.mainThread())//Rxjava的返回结果运行在主线程

                .subscribe(new Observer<Object>() {

                    @Override
                    public void onCompleted() {
                        callBack.onFinish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.onError(e.toString());
                    }

                    @Override
                    public void onNext(Object o) {
                        callBack.onSueecss(o.toString());
                    }
                });
    }
    private String guessMimeType(String path)
    {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null)
        {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }
    private void addParams(MultipartBody.Builder builder)
    {

                builder.addPart(Headers.of("Content-Disposition", "form-data; name=img"),
                        RequestBody.create(null, "img"));


    }


}
