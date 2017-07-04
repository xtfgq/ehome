package com.zzu.ehome.activity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;

import android.widget.LinearLayout;
import android.widget.ListView;

import com.yiguo.toast.Toast;/**/

import com.tencent.TIMCallBack;
import com.tencent.TIMFriendshipManager;
import com.tencent.TIMManager;
import com.tencent.TIMUser;
import com.zzu.ehome.R;

import com.zzu.ehome.adapter.InspectionReportAdapter;
import com.zzu.ehome.adapter.MessageItemAdapter;
import com.zzu.ehome.application.Constants;


import com.zzu.ehome.bean.StreamInfo;
import com.zzu.ehome.bean.User;
import com.zzu.ehome.db.EHomeDao;
import com.zzu.ehome.db.EHomeDaoImpl;
import com.zzu.ehome.utils.CommonUtils;

import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.MD5Utils;
import com.zzu.ehome.utils.OkHttpClientManager;


import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.utils.TimeUtils;
import com.zzu.ehome.utils.ToastUtils;
import com.zzu.ehome.view.HeadView;
import com.zzu.ehome.view.PullToRefreshLayout;
import com.zzu.ehome.view.crop.util.Log;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



/**
 * Created by guoqiang on 2017/3/31.
 */

public class MessageLiveActivity extends BaseActivity {

    private ListView listView;
    private LinearLayout layout_none;
    private PullToRefreshLayout pulltorefreshlayout;
    private List<StreamInfo> mList = new ArrayList<StreamInfo>();

    private boolean isFirst = true;
    private boolean isReflash = false;
    private boolean isLoading = false;
    private int page = 1;

    OkHttpClientManager okHttpClientManager;
    private MessageItemAdapter messageItemAdapter;

    private RequestMaker requestMaker;
    private String userid,name;
    private EHomeDao dao;
    private User dbUser;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_messagelive);
        okHttpClientManager = OkHttpClientManager.getInstance();

        userid= SharePreferenceUtil.getInstance(MessageLiveActivity.this).getUserId();
        dao=new EHomeDaoImpl(this);
        dbUser = dao.findUserInfoById(userid);
        name=dbUser.getUsername();
        requestMaker=RequestMaker.getInstance();
        initViews();
        initEvent();
        getUserReginer();
        initDatas();
        if (!CommonUtils.isNotificationEnabled(MessageLiveActivity.this)) {
            showTitleDialog("请打开通知中心");
        }
    }


    public void initViews() {
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "视频教学", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        });
        pulltorefreshlayout = (PullToRefreshLayout) findViewById(R.id.refresh_view);
        listView = (ListView) findViewById(R.id.listView);
        messageItemAdapter=new MessageItemAdapter(MessageLiveActivity.this);
        listView.setAdapter(messageItemAdapter);
        layout_none = (LinearLayout) findViewById(R.id.layout_none);
        layout_none.setVisibility(View.GONE);
    }


    public void initEvent() {


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!isNetWork) {
                    showNetWorkErrorDialog();
                    return;
                }

                        Intent i=new Intent(MessageLiveActivity.this,LiveActivity.class);
                        i.putExtra("playUrl","rtmp://"+mList.get(position).getStream_name().split("_")[0]+".liveplay.myqcloud.com/live/"+mList.get(position).getStream_name());
                        i.putExtra("groupId",Constants.groupId);
                        startActivity(i);




            }
        });
        pulltorefreshlayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                page = 1;
                isFirst = true;
                isReflash = true;
                isLoading = false;
                initDatas();
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                page++;
                isLoading = true;
                isReflash = false;
                initDatas();
            }
        });

    }



    public void initDatas() {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time="";
        try {
            time = TimeUtils.dateToStamp(sdf.format(new Date().getTime()+3000000));
        } catch (Exception e) {

            e.printStackTrace();
        }
        String signValue= MD5Utils.MD5(Constants.AppCheckkey+time);
        String playUrl ="http://statcgi.video.qcloud.com/common_access?"+
                "cmd="+Constants.Appkey+"&interface=Get_LiveStat"+
                "&Param.n.page_no="+page+"&Param.n.page_size=10"+
                "&t="+time+"&sign="+signValue;

        okHttpClientManager.doGet(playUrl,new OkHttpClientManager.RequestCallBack(){
            @Override
            public void onSueecss(String msg) {

                try {
                    JSONObject mySO = new JSONObject(msg);
                    String errmsg=mySO.getString("errmsg");
                    if (isReflash) {
                        mList.clear();
                    }
                    if(errmsg.contains("no")&&page==1){
                        layout_none.setVisibility(View.VISIBLE);
                        pulltorefreshlayout.setVisibility(View.GONE);

                    }else{
                        if(errmsg.contains("no")&& isLoading){
                            isLoading = false;
                            pulltorefreshlayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                            Toast.makeText(MessageLiveActivity.this, "已经没有更多数据了",
                                    Toast.LENGTH_SHORT).show();

                        }else{
                           layout_none.setVisibility(View.GONE);
                            String output=mySO.getString("output");
                            JSONObject out= new JSONObject(output);
                            JSONArray arraySub =
                                    out.getJSONArray("stream_info");
                            List<StreamInfo> tmpList=new ArrayList<StreamInfo>();
                            String id="";
                            for(int i=0;i<arraySub.length();i++){
                                StreamInfo streamInfo= new StreamInfo();
                                streamInfo.setClientip(arraySub.getJSONObject(i).getString("client_ip"));
                                streamInfo.setServer_ip(arraySub.getJSONObject(i).getString("server_ip"));
                                streamInfo.setStream_name(arraySub.getJSONObject(i).getString("stream_name"));
                                streamInfo.setTime(arraySub.getJSONObject(i).getString("time"));
                                streamInfo.setImgUrl("http://a879eccblvb1251918063screenshot-1252813850.file.myqcloud.com/2017-03-31/8575_3ccdd9def7-screenshot-15-48-18-640x360.jpg");
                                if(i==(arraySub.length()-1)){
                                    id = id + streamInfo.getStream_name();
                                }else {
                                    id = id + streamInfo.getStream_name() + ",";
                                }
                                tmpList.add(streamInfo);
                            }
                            getIds(id,tmpList);
                        }


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    isFirst = false;
                }


            }

            @Override
            public void onError(String msg) {
                Log.e("vvv",msg);

            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {

            }
        });




    }
    private void getUserReginer(){


        requestMaker.getUserSign(userid,  new JsonAsyncTask_Info(MessageLiveActivity.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                try {
                    JSONObject mySO = (JSONObject) result;
                    JSONArray array = mySO
                            .getJSONArray("GetUserSig");
                    if (array.getJSONObject(0).getString("MessageCode").toString().equals("0")) {

                        String sig = array.getJSONObject(0).getString("MessageContent").toString().trim();
                        Log.e("ss",sig);
                        final TIMUser user = new TIMUser();
                        user.setIdentifier(userid);
                        user.setAccountType("12001");
                        user.setAppIdAt3rd(String.valueOf(Constants.sdkAppId));
                        TIMManager.getInstance().login(
                                Constants.sdkAppId,                   //sdkAppId，由腾讯分配
                                user,
                                sig,                    //用户帐号签名，由私钥加密获得，具体请参考文档
                                new TIMCallBack() {//回调接口

                                    @Override
                                    public void onSuccess() {//登录成功
//                                        Log.e("vvv","succuss");
//                                        Intent i=new Intent(MessageLiveActivity.this,LiveActivity.class);
//                                        i.putExtra("playUrl","");
//                                        i.putExtra("groupId",Constants.groupId);
//                                        startActivity(i);
                                        setMyNickName(name);

                                    }

                                    @Override
                                    public void onError(int code, String desc) {//登录失败

                                        //错误码code和错误描述desc，可用于定位请求失败原因
                                        //错误码code含义请参见错误码表
                                        Log.e("eer","eeror");

                                    }
                                });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {

            }
        }));
    }

    /**
     * 对房间id信息的匹配
     * @param ids
     */
    private void getIds(String ids,List<StreamInfo> temp){
        mList.addAll(temp);
        messageItemAdapter.setList(mList);
        if (isReflash) {
            isReflash = false;
            messageItemAdapter.notifyDataSetChanged();
            pulltorefreshlayout.refreshFinish(PullToRefreshLayout.SUCCEED);
        } else if (isLoading) {
            isLoading = false;
            messageItemAdapter.notifyDataSetChanged();
            pulltorefreshlayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
        }else{
            messageItemAdapter.notifyDataSetChanged();
        }
        //该接口尚未完善
        okHttpClientManager.doGet(Constants.LiveURL+Constants.imageUrl+"?streamids="+ids,new OkHttpClientManager.RequestCallBack(){
            @Override
            public void onSueecss(String msg) {
                Log.e("=jj==",msg);
            }

            @Override
            public void onError(String msg) {
                Log.e("==pp=",msg);
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {

            }
        } );
    }
    public void setMyNickName(String nickName){
        TIMFriendshipManager.getInstance().setNickName(nickName, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess() {

            }
        });
    }




}
