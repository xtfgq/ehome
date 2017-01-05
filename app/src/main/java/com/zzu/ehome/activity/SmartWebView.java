package com.zzu.ehome.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.umeng.analytics.MobclickAgent;
import com.zzu.ehome.R;
import com.zzu.ehome.application.Constants;
import com.zzu.ehome.bean.User;
import com.zzu.ehome.db.EHomeDao;
import com.zzu.ehome.db.EHomeDaoImpl;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.view.HeadView;

import static com.zzu.ehome.R.id.webView;

/**
 * Created by Administrator on 2016/5/16.
 */
public class SmartWebView extends BaseSimpleActivity {
    private final String mPageName = "SmartWebView";
    private WebView mWebView;
    private Intent mIntent;
    private EHomeDao dao;
    private String userid;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_webview);
        mWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = mWebView.getSettings();
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setDownloadListener(new MyWebViewDownLoadListener());
        webSettings.setSaveFormData(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(false);
        mIntent = this.getIntent();
        dao = new EHomeDaoImpl(this);
        userid= SharePreferenceUtil.getInstance(SmartWebView.this).getUserId();
       String url;
            user=dao.findUserInfoById(userid);

//url="http://ehome.staging.topmd.cn/chaxun/select.html";

        if(!isNetWork){
            showNetWorkErrorDialog();
            return;
        }
        url=Constants.EhomeURL+"/LaiKang/HealthDataList.aspx?CardNo="+user.getUserno();
//        url="http://wxsdk.lkang.cn/LoginZ.aspx?cardno=410322198708063857&mobile=15986816294";

        mWebView.loadUrl(url);


    }

    class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            http://ehome.staging.topmd.cn:81/LaiKang/BackToAppHomePage
            if(url.contains("BackToAppHomePage")){
                finish();
            }else{
                view.loadUrl(url);
            }
            return true;

        }

    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        MobclickAgent.onPageEnd(mPageName);
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        MobclickAgent.onPageStart(mPageName);
        MobclickAgent.onResume(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            // 返回键退回
            if (mWebView.canGoBack()) {
                mWebView.goBack();
            } else {
                finish();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);

    }
    private class MyWebViewDownLoadListener implements DownloadListener {

        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                    long contentLength) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }

    }
}
