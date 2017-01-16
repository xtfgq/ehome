package com.zzu.ehome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zzu.ehome.R;
import com.zzu.ehome.view.HeadView;


/**
 * Created by Administrator on 2017/1/4.
 */

public class MyPingguActivity extends BaseActivity {
    private WebView mWebView;
    private Intent mIntent;
    private String title,url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.declaration_layout);
        mIntent=this.getIntent();
        title=mIntent.getStringExtra("title");
        url=mIntent.getStringExtra("url");
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, title, new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {

            finishActivity();
            }
        });
        mWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setSaveFormData(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(false);

        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                mWebView.loadUrl(url);
                return true;
            }
        });
        mWebView.loadUrl(url);
    }
}
