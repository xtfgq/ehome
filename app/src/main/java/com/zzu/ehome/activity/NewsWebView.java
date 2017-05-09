package com.zzu.ehome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.umeng.analytics.MobclickAgent;
import com.zzu.ehome.R;
import com.zzu.ehome.application.Constants;
import com.zzu.ehome.bean.ShareModel;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.ImageUtil;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.ToastUtils;
import com.zzu.ehome.view.HeadView;
import com.zzu.ehome.view.SharePopupWindow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/5/16.
 */
public class NewsWebView extends BaseActivity {
    private final String mPageName = "WebViewPage";
    private WebView mWebView;
    private Intent mIntent;
    String title="";
    String ID = "";
    String url;
    private SharePopupWindow share;
    private RequestMaker requestMaker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testwebview_layout);
        requestMaker=RequestMaker.getInstance();
        mWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = mWebView.getSettings();
        mWebView.setWebViewClient(new MyWebViewClient());
        webSettings.setSaveFormData(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(false);
        webSettings.setBlockNetworkImage(false);

        mIntent = this.getIntent();
        if (mIntent != null && mIntent.getStringExtra("ID") != null) {
            ID = mIntent.getStringExtra("ID");
            getTitle(ID);
        }
        if(!isNetWork){
            showNetWorkErrorDialog();
            return;
        }

        url = Constants.URLIMAGENEW + ID;
//        if (title.length() > 10) {
//            title = title.substring(0, 9) + "...";
//        }

        setDefaultViewMethod(R.mipmap.icon_arrow_left, "健康资讯", R.drawable.icon_share
                , new HeadView.OnLeftClickListener() {
                    @Override
                    public void onClick() {
                        finishActivity();
                    }
                }, new HeadView.OnRightClickListener() {
                    @Override
                    public void onClick() {
                        doShare();
                    }
                });
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        mWebView.loadUrl(url);
        if(!CommonUtils.isNotificationEnabled(NewsWebView.this)){
            showTitleDialog("请打开通知中心");
        }


    }
    private void getTitle(String id){
        requestMaker.NewsDetailInquiry(id,new JsonAsyncTask_Info(NewsWebView.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                try {
                    JSONObject mySO = (JSONObject) result;
                    JSONArray array = mySO
                            .getJSONArray("NewsDetailInquiry");
                    title=array.getJSONObject(0).getString("Title");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                }
            }

            @Override
            public void onError(Exception e) {

            }
        }));
    }

    class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
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
    public void doShare() {
        share = new SharePopupWindow(NewsWebView.this);
        ShareModel model = new ShareModel();
        model.setImgPath(ImageUtil.saveResTolocal(NewsWebView.this.getResources(), R.drawable.share, "home_logo"));
        model.setText(title);
        model.setTitle("名医网·健康E家");
        model.setUrl(url+"&share=1");
        share.initShareParams(model);
        share.showShareWindow();
        share.showAtLocation(NewsWebView.this.findViewById(R.id.news),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }
}
