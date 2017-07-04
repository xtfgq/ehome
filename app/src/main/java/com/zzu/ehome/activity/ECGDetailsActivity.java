package com.zzu.ehome.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
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
import com.zzu.ehome.bean.User;
import com.zzu.ehome.db.EHomeDao;
import com.zzu.ehome.db.EHomeDaoImpl;
import com.zzu.ehome.network.DownloadProgressListener;
import com.zzu.ehome.network.FileDownloader;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.PermissionsChecker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.utils.ToastUtils;
import com.zzu.ehome.view.HeadView;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import static u.aly.au.i;

/**
 * Created by dell on 2016/6/17.
 */
public class ECGDetailsActivity extends BaseActivity {

    private Intent mIntent;
    private String filename, filemd5;
    private TextView tvresult, tv_statu, tvtime, tvname;
    private String url = "", status;
    long filesize;
    int result;
    private CircleProgressBar circleProgressBar;
    private TextView tvcontent;

    private RelativeLayout rl_check;
    private User user;
    private EHomeDao dao;

    private DownloadManager downloadManager;
    private DownloadInfo downloadInfo;

    public static final String SDCARD_PATH = Environment.getExternalStorageDirectory().toString();
    public final String DOWM_FOLDER = SDCARD_PATH + File.separator + "ehome2" + File.separator + "download2" + File.separator;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_dynamic_detail);
        dao = new EHomeDaoImpl(this);
        mIntent = this.getIntent();

        filemd5 = mIntent.getStringExtra("FileMD5");
        status = mIntent.getStringExtra("ReportType");

        user = dao.findUserInfoById(SharePreferenceUtil.getInstance(ECGDetailsActivity.this).getUserId());
        filename = mIntent.getStringExtra("Fid").trim()+".pdf";
        url = ( mIntent.getStringExtra("Download")).replace("\\", "/");
        try {
            url = new String(url.getBytes("UTF-8"));
            url= URLEncoder.encode(url,"utf-8").replaceAll("\\+", "%20");
            url = url.replaceAll("%3A", ":").replaceAll("%2F", "/");
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        downloadManager = DownloadService.getDownloadManager();
        downloadManager.setTargetFolder(DOWM_FOLDER);
        downloadInfo = downloadManager.getDownloadInfo(url);
        initViews();
        tv_statu.setText(status);
        tvtime.setText(mIntent.getStringExtra("time"));
        tvname.setText(user.getUsername());
        if (status.contains("低")) {
            tv_statu.setTextColor(Color.parseColor("#00c07d"));
        } else if (status.contains("中")) {
            tv_statu.setTextColor(Color.parseColor("#fb9c2e"));
        } else if (status.contains("高")) {
            tv_statu.setTextColor(Color.parseColor("#f95935"));
        }
        circleProgressBar.setVisibility(View.GONE);
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
        initEvent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getPermission();
        }
        if(!CommonUtils.isNotificationEnabled(ECGDetailsActivity.this)){
            showTitleDialog("请打开通知中心");
        }

    }

    public void initViews() {

        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "动态心电报告", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        });
        tvresult = (TextView) findViewById(R.id.tvresult);
        tvresult.setText(mIntent.getStringExtra("Result"));
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
                        downloadManager.restartTask(url);
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
                            downloadManager.pauseTask(url);
                            tvcontent.setText("继续下载详细报告内容");
                        } else if (downloadInfo.getState() == DownloadManager.ERROR) {
                            circleProgressBar.setVisibility(View.VISIBLE);
                            downloadManager.restartTask(url);
                            tvcontent.setText("正在下载详细报告内容...");
                        }
                    }
                } else {
                    GetRequest request = OkGo.get(url);
                    downloadManager.addTask(filename,url, request, null);
                    downloadInfo = downloadManager.getDownloadInfo(url);
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
            Toast.makeText(ECGDetailsActivity.this, "下载完成:" + downloadInfo.getTargetPath(), Toast.LENGTH_SHORT).show();
            tvcontent.setText("打开文件");
        }

        @Override
        public void onError(DownloadInfo downloadInfo, String errorMsg, Exception e) {
            if (errorMsg != null) {
                Toast.makeText(ECGDetailsActivity.this, "下载失败", Toast.LENGTH_SHORT).show();
                tvcontent.setText("重新下载详细报告内容");
            }
        }
    }
}
