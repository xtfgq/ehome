package com.zzu.ehome.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.yiguo.toast.Toast;/**/

import com.dinuscxj.progressbar.CircleProgressBar;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okserver.download.DownloadInfo;
import com.lzy.okserver.download.DownloadManager;
import com.lzy.okserver.download.DownloadService;
import com.lzy.okserver.listener.DownloadListener;
import com.zzu.ehome.R;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.DateUtils;
import com.zzu.ehome.view.HeadView;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


/**
 * Created by dell on 2016/6/17.
 */
public class ECGPDFStaticActivity extends BaseActivity{

    private Intent mIntent;
    private TextView tvresult, tv_statu, tvtime, tvname;
    private String imurl, Diagnosis, PatientName, CollectTime, filename;
    int result=0;
    private CircleProgressBar circleProgressBar;
    private TextView tvcontent;
    private RelativeLayout rl_check;

    public static final String SDCARD_PATH = Environment.getExternalStorageDirectory().toString();
    public final String DOWM_FOLDER = SDCARD_PATH + File.separator + "ehome2" + File.separator + "download2" + File.separator;

    private DownloadManager downloadManager;
    private DownloadInfo downloadInfo;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_dynamic_detail);
        mIntent = this.getIntent();
        imurl = mIntent.getStringExtra("imurl");
        Diagnosis = mIntent.getStringExtra("Diagnosis");
        PatientName = mIntent.getStringExtra("PatientName");
        CollectTime = mIntent.getStringExtra("CollectTime");
//        imurl="http://gdown.baidu.com/data/wisegame/df65a597122796a4/weixin_821.apk";
        filename = imurl.substring(imurl.lastIndexOf("/") + 1, imurl.length());
        downloadManager = DownloadService.getDownloadManager();
        downloadManager.setTargetFolder(DOWM_FOLDER);
        try {
            imurl = new String(imurl.getBytes("UTF-8"));
            imurl = URLEncoder.encode(imurl, "utf-8").replaceAll("\\+", "%20");
            imurl = imurl.replaceAll("%3A", ":").replaceAll("%2F", "/");
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        downloadInfo = downloadManager.getDownloadInfo(imurl);
        initViews();
        tv_statu.setVisibility(View.GONE);
        tvtime.setText(DateUtils.StringPattern(CollectTime, "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd"));
        tvname.setText(PatientName);
        circleProgressBar.setVisibility(View.GONE);
        initEvent();
        if (downloadInfo != null) {
            if(downloadInfo.getState() == DownloadManager.FINISH){

                circleProgressBar.setVisibility(View.GONE);
                File file=new File(DOWM_FOLDER + filename);
                if(file.exists()) {
                    tvcontent.setText("打开文件");

                }else{
                    tvcontent.setText("重新下载详细报告内容");
                }
            }else if(downloadInfo.getState() == DownloadManager.PAUSE){
                circleProgressBar.setVisibility(View.VISIBLE);
                result=(int)(Math.round(downloadInfo.getProgress() * 10000) * 1.0f / 100);
                circleProgressBar.setProgress(result);
                tvcontent.setText("继续下载细报告内容");
            }else if(downloadInfo.getState()==DownloadManager.ERROR){
                circleProgressBar.setVisibility(View.GONE);
                tvcontent.setText("重新下载细报告内容");
            }
        }else{
            tvcontent.setText("下载详细报告内容");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getPermission();
        }
        initDatas();
        if(!CommonUtils.isNotificationEnabled(ECGPDFStaticActivity.this)){
            showTitleDialog("请打开通知中心");
        }
    }

    public void initViews() {

        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "静态心电报告", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        });
        tvresult = (TextView) findViewById(R.id.tvresult);
        tvresult.setText(Diagnosis);
        rl_check = (RelativeLayout) findViewById(R.id.rl_check);
        circleProgressBar = (CircleProgressBar) findViewById(R.id.line_progress);
        tvcontent = (TextView) findViewById(R.id.tvcontent);
        tv_statu = (TextView) findViewById(R.id.tv_statu);
        tvname = (TextView) findViewById(R.id.tvname);
        tvtime = (TextView) findViewById(R.id.tvtime);
    }

    public void initEvent() {
        rl_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isNetWork){
                    showNetWorkErrorDialog();
                    return;
                }
                if (downloadInfo != null) {
                    if(tvcontent.getText().toString().contains("重新下载详细报告内容")){
                        circleProgressBar.setVisibility(View.VISIBLE);
                        downloadManager.restartTask(imurl);
                        tvcontent.setText("正在下载详细报告内容...");
                    }else {
                        if (downloadInfo.getState() == DownloadManager.FINISH) {
                            tvcontent.setText("打开文件");
                            circleProgressBar.setVisibility(View.GONE);
                            openFolder(DOWM_FOLDER + filename);

                        } else if (downloadInfo.getState() == DownloadManager.PAUSE) {
                            downloadManager.startAllTask();
                            circleProgressBar.setVisibility(View.VISIBLE);
                            tvcontent.setText("正在下载详细报告内容...");
                        } else if (downloadInfo.getState() == DownloadManager.DOWNLOADING) {
                            circleProgressBar.setVisibility(View.VISIBLE);
                            downloadManager.pauseTask(imurl);
                            tvcontent.setText("继续下载详细报告内容");
                        } else if (downloadInfo.getState() == DownloadManager.ERROR) {
                            circleProgressBar.setVisibility(View.VISIBLE);
                            downloadManager.restartTask(imurl);
                            tvcontent.setText("正在下载详细报告内容...");
                        }
                    }
                } else {
                    GetRequest request = OkGo.get(imurl);
                    downloadManager.addTask(filename,imurl, request, null);
                    downloadInfo = downloadManager.getDownloadInfo(imurl);
                    circleProgressBar.setVisibility(View.VISIBLE);
                    downloadManager.startAllTask();
                    DownloadListener downloadListener = new MyDownloadListener();
                    downloadInfo.setListener(downloadListener);
                    tvcontent.setText("正在下载详细报告内容...");
                }

            }
        });
        if(downloadInfo!=null) {
            DownloadListener downloadListener = new MyDownloadListener();
            downloadInfo.setListener(downloadListener);
        }
    }

    public void initDatas() {

    }



    public void openFolder(String filename) {
        circleProgressBar.setVisibility(View.GONE);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new
                File(filename));
        intent.setDataAndType(uri, "application/pdf");
        this.startActivity(intent);
    }
    private class MyDownloadListener extends DownloadListener {

        @Override
        public void onProgress(DownloadInfo downloadInfo) {
            Long length=downloadInfo.getDownloadLength();
            Long size=downloadInfo.getTotalLength();
            result=(int)(Math.round(downloadInfo.getProgress() * 10000) * 1.0f / 100);
            circleProgressBar.setProgress(result);
        }

        @Override
        public void onFinish(DownloadInfo downloadInfo) {
            Toast.makeText(ECGPDFStaticActivity.this, "下载完成:" + downloadInfo.getTargetPath(), Toast.LENGTH_SHORT).show();
            tvcontent.setText("打开文件");
        }

        @Override
        public void onError(DownloadInfo downloadInfo, String errorMsg, Exception e) {
            if (errorMsg != null) {
                Toast.makeText(ECGPDFStaticActivity.this, "下载失败", Toast.LENGTH_SHORT).show();
                tvcontent.setText("重新下载详细报告内容");
            }
        }
    }

}
