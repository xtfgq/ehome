package com.zzu.ehome.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.zzu.ehome.R;
import com.zzu.ehome.adapter.MyRemindAdapter;
import com.zzu.ehome.bean.MyRemindBean;
import com.zzu.ehome.bean.MyRemindDate;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.JsonTools;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.ScreenUtils;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.view.HeadView;
import com.zzu.ehome.view.PullToRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Mersens on 2016/8/18.
 */
public class MyRemindActivity1 extends BaseActivity {
    private SwipeMenuListView listView;
    private RequestMaker requestMaker;
    private String userid;
    List<MyRemindBean> list = null;
    private MyRemindAdapter myRemindAdapter;
    public static final int ADD_REMIND= 0x19;
    private LinearLayout layout_no_msg;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        requestMaker = RequestMaker.getInstance();
        userid = SharePreferenceUtil.getInstance(MyRemindActivity1.this).getUserId();
        setContentView(R.layout.activity_remind1);
        initViews();
        initEvent();
        if(!isNetWork){
            showNetWorkErrorDialog();
            return;
        }
        initDatas();
        if(!CommonUtils.isNotificationEnabled(MyRemindActivity1.this)){
            showTitleDialog("请打开通知中心");
        }
    }

    public void initViews() {
        listView = (SwipeMenuListView) findViewById(R.id.listView);
        layout_no_msg=(LinearLayout)findViewById(R.id.layout_no_msg);
        setDefaultTXViewMethod(R.mipmap.icon_arrow_left, "我的提醒", "添加", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        }, new HeadView.OnRightClickListener() {
            @Override
            public void onClick() {
                Intent intent = new Intent(MyRemindActivity1.this, AddRemindActivity.class);
                startActivityForResult(intent, ADD_REMIND);
            }
        });

    }

    public void initEvent() {

    }


    public void initDatas() {

        requestMaker.RemindInquiry(userid, new JsonAsyncTask_Info(MyRemindActivity1.this, true, new JsonAsyncTaskOnComplete() {

            @Override
            public void processJsonObject(Object result) {

                try {
                    JSONObject mySO = (JSONObject) result;
                    org.json.JSONArray array = mySO
                            .getJSONArray("RemindInquiry");
                    if (array.getJSONObject(0).has("MessageCode")) {
//                        Toast.makeText(MyRemindActivity1.this, array.getJSONObject(0).getString("MessageContent").toString(),
//                                Toast.LENGTH_SHORT).show();
                        layout_no_msg.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);
                    } else {
                        layout_no_msg.setVisibility(View.GONE);
                        listView.setVisibility(View.VISIBLE);
                        MyRemindDate date = JsonTools.getData(result.toString(), MyRemindDate.class);
                        list = date.getData();
                        myRemindAdapter = new MyRemindAdapter(MyRemindActivity1.this, list);
                        listView.setAdapter(myRemindAdapter);
                        SwipeMenuCreator creator = new SwipeMenuCreator() {

                            @Override
                            public void create(SwipeMenu menu) {
                                // Create different menus depending on the view type
                                switch (menu.getViewType()) {
                                    case 0:
                                        createMenu1(menu);
                                        break;
                                }
                            }

                            private void createMenu1(SwipeMenu menu) {
                                SwipeMenuItem delItem = new SwipeMenuItem(
                                        MyRemindActivity1.this);
                                // set item background

                                delItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                                // set item width
                                delItem.setWidth(ScreenUtils.dip2px(MyRemindActivity1.this, 90));
                                // set item title
                                delItem.setTitle("取消提醒");
                                // set item title fontsize
                                delItem.setTitleSize(18);
                                // set item title font color
                                delItem.setTitleColor(Color.WHITE);
                                // add to menu
                                menu.addMenuItem(delItem);
                            }
                        };

                        listView.setMenuCreator(creator);
                        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                            @Override
                            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
                                MyRemindBean item = list.get(position);
                                switch (index) {
                                    case 0:
                                        if(!isNetWork){
                                          showNetWorkErrorDialog();
                                            return;
                                        }
                                        deleteRecent(item, position);
                                        break;

                                }
                            }
                        });


                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    layout_no_msg.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                }

            }
        }));


    }

    public void deleteRecent(MyRemindBean bean, final int position) {
        String id = bean.getID();
        requestMaker.RemindDelete(id, new JsonAsyncTask_Info(MyRemindActivity1.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                try {
                    JSONObject mySO = (JSONObject) result;
                    JSONArray array = mySO
                            .getJSONArray("RemindDelete");
                    if (Integer.valueOf(array.getJSONObject(0).getString("MessageCode")) == 0) {
                        list.remove(position);
                        myRemindAdapter.notifyDataSetChanged();
                        if(list.size()==0){
                            layout_no_msg.setVisibility(View.VISIBLE);
                            listView.setVisibility(View.GONE);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }));


    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_REMIND && data != null) {
            layout_no_msg.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            initDatas();
        }
    }

}
