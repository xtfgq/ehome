package com.zzu.ehome.activity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.zzu.ehome.R;
import com.zzu.ehome.view.HeadView;


/**
 * Created by Administrator on 2017/1/4.
 */

public class DeclarationWebActivity extends BaseActivity {
    private WebView mWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.declaration_layout);
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "免责声明", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {

            finishActivity();
            }
        });
        mWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setSaveFormData(false);
        webSettings.setJavaScriptEnabled(true);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webSettings.setSupportZoom(false);
        mWebView.loadUrl(" file:///android_asset/shengming.html ");

    }
}
