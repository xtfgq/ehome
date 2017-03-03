package com.zzu.ehome.activity;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;

import android.os.Parcelable;
import android.view.KeyEvent;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
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

import android.webkit.WebView.HitTestResult;

import java.util.ArrayList;
import java.util.List;

import static com.zzu.ehome.R.id.view;


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
    private boolean isFirst=false;

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
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        mIntent = this.getIntent();
        dao = new EHomeDaoImpl(this);
        userid = SharePreferenceUtil.getInstance(SmartWebView.this).getUserId();
        String url;
        user = dao.findUserInfoById(userid);

//url="http://ehome.staging.topmd.cn/chaxun/select.html";

        if (!isNetWork) {
            showNetWorkErrorDialog();
            return;
        }
        url = Constants.EhomeURL + "/LaiKang/HealthDataList.aspx?CardNo=" + user.getUserno();
//        url="http://wxsdk.lkang.cn/LoginZ.aspx?cardno=410322198708063857&mobile=15986816294";
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    stopProgressDialog();
                } else {
                    if(!isFirst) {
                        startProgressDialogTitle("正在加载中...");
                        isFirst=true;
                    }
                }
            }
        });
        mWebView.loadUrl(url);


    }

    class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (url.contains("BackToAppHomePage")) {
                finish();
            }else {
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
//            Uri uri = Uri.parse(url);
//            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//            startActivity(intent);
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
/*             intent.setData(Uri.parse(url));
            intent.setType("text/html");*//*
            // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(intent, 0);
            int i=-1;
            int j=-1;
            if (!resInfo.isEmpty()) {
                List<Intent> targetedIntents = new ArrayList<Intent>();
                for (int n = 0; n < resInfo.size(); n++) {
                    ResolveInfo info = resInfo.get(n);
                    String pkg = info.activityInfo.packageName.toLowerCase();

                    if (pkg.contains("com.zzu.ehome.main.ehome")) {
                        i=n;
*//*                         Uri ur1 = Uri.parse(url);
                        Intent intentContents = new Intent(Intent.ACTION_VIEW, ur1);
                        intentContents.setPackage(pkg);
                        intentContents.setData(ur1);
                        intentContents.setType("text/html");
*//**//*                                Intent chit = new Intent();
                                chit.setPackage(pkg);
                                chit.setAction(Intent.ACTION_VIEW);*//**//*
//                                chit.setData(Uri.parse(url));
                        targetedIntents.add(intentContents);*//*
                    }
                    else if( pkg.contains("com.zzu.ehome.ehomefordoctor")){
                       j=n;
                    }
                }
                if(i!=-1){
                    getPackageManager().queryIntentActivities(intent, 0).remove(i);
                }
                if(j!=-1){
                    getPackageManager().queryIntentActivities(intent, 0).remove(j);
                }
               Intent chooserIntent = Intent.createChooser(getPackageManager().queryIntentActivities(intent, 0).remove(0), "选择浏览方式");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedIntents.toArray(new Parcelable[]{}));
//                        chooserIntent.setData(Uri.parse(url));
                chooserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*//*
                startActivity(;*/
            }
        }

}
