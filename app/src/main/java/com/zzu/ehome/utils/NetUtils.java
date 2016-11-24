package com.zzu.ehome.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;

import com.zzu.ehome.DemoContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NetUtils {
    private static HttpClient httpClient = new DefaultHttpClient();
    private static final String BASE_URL = "http://webim.demo.rong.io/";

    /**
     * 发送GET请求方法
     *
     * @param requestUrl 请求的URL
     * @return 响应的数据
     */
    public static String sendGetRequest(String requestUrl) {

        HttpGet httpGet = new HttpGet(BASE_URL + requestUrl);
        if (DemoContext.getInstance().getSharedPreferences() != null) {
            httpGet.addHeader("cookie", DemoContext.getInstance().getSharedPreferences().getString("DEMO_COOKIE", null));
        }
        try {
            HttpResponse response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                getCookie(httpClient);
                return EntityUtils.toString(entity); // 当返回的类型为Json数据时，调用此返回方法
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 发送post请求
     *
     * @param requestUrl 请求的URL
     * @param params     请求的参数
     * @return 响应的数据
     */
    public static String sendPostRequest(String requestUrl, Map<String, String> params) {

        HttpPost httpPost = new HttpPost(BASE_URL + requestUrl);

        if (DemoContext.getInstance().getSharedPreferences() != null) {

            httpPost.addHeader("cookie", DemoContext.getInstance().getSharedPreferences().getString("DEMO_COOKIE", null));
        }
        try {
            if (params != null && params.size() > 0) {
                List<NameValuePair> paramLists = new ArrayList<NameValuePair>();
                for (Map.Entry<String, String> map : params.entrySet()) {
                    paramLists.add(new BasicNameValuePair(map.getKey(), map.getValue()));
                }
                httpPost.setEntity(new UrlEncodedFormEntity(paramLists, "UTF-8"));
            }
            HttpResponse response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();

                getCookie(httpClient);
                return EntityUtils.toString(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获得cookie
     *
     * @param httpClient
     */
    public static void getCookie(HttpClient httpClient) {
        List<Cookie> cookies = ((AbstractHttpClient) httpClient).getCookieStore().getCookies();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < cookies.size(); i++) {
            Cookie cookie = cookies.get(i);
            String cookieName = cookie.getName();
            String cookieValue = cookie.getValue();
            if (!TextUtils.isEmpty(cookieName)
                    && !TextUtils.isEmpty(cookieValue)) {
                sb.append(cookieName + "=");
                sb.append(cookieValue + ";");
            }
        }
        SharedPreferences.Editor edit = DemoContext.getInstance().getSharedPreferences().edit();
        edit.putString("DEMO_COOKIE", sb.toString());
        edit.apply();
    }

    // 判断网络连接状态
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    // 判断wifi状态
    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    // 判断移动网络
    public static boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    // 获取连接类型
    public static int getConnectedType(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                return mNetworkInfo.getType();
            }
        }
        return -1;
    }

    /**
     * 设置网络
     *
     * @param paramContext
     */
    public static void startToSettings(Context paramContext) {
        if (paramContext == null)
            return;
        try {
            if (Build.VERSION.SDK_INT > 10) {
                paramContext.startActivity(new Intent(
                        "android.settings.SETTINGS"));
                return;
            }
        } catch (Exception localException) {
            localException.printStackTrace();
            return;
        }
        paramContext.startActivity(new Intent(
                "android.settings.WIRELESS_SETTINGS"));
    }
    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     * @param context
     * @return true 表示开启
     */
    public static final boolean gPSIsOPen(final Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }
        return false;
    }
}
