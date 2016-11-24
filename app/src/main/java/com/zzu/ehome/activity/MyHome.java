package com.zzu.ehome.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;
import com.zzu.ehome.R;
import com.zzu.ehome.bean.RelationBean;
import com.zzu.ehome.bean.RelationDes;
import com.zzu.ehome.fragment.MyHomeFragment;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.JsonTools;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.view.HeadView;
import com.zzu.ehome.view.crop.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * 添加亲友
 * Created by Administrator on 2016/8/6.
 */
public class MyHome extends BaseActivity implements View.OnClickListener {
    private Button addFaimily;
    private String userid = "";
    private int current;
    private HorizontalInfiniteCycleViewPager horizontalInfiniteCycleViewPager;
    private FrameLayout llcontainer;
    private MyHorizontalPagerAdapter adapter;
    private List<RelationDes> mlist = new ArrayList<RelationDes>();

    private RequestMaker requestMaker;
    ArrayList<Fragment> listFragmentsa;
    public int selectNum = 0;//全局变量，保存被选中的item
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    adapter = new MyHorizontalPagerAdapter(getSupportFragmentManager(), listFragmentsa);
                    stopProgressDialog();
                    horizontalInfiniteCycleViewPager.setVisibility(View.VISIBLE);
                    horizontalInfiniteCycleViewPager.setAdapter(adapter);
                    break;


                default:
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_family);
        requestMaker = RequestMaker.getInstance();
        userid = SharePreferenceUtil.getInstance(MyHome.this).getUserId();
        initViews();
        startProgressDialog();
    }

    private void initViews() {


        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "我的家人", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        });
        horizontalInfiniteCycleViewPager = (HorizontalInfiniteCycleViewPager) findViewById(R.id.hicvp);
       listFragmentsa = new ArrayList<Fragment>();



       getUserRelation(userid);
    }

    class MyHorizontalPagerAdapter extends FragmentStatePagerAdapter {
        public List<Fragment> getListFragments() {
            return listFragments;
        }

        public void setListFragments(List<Fragment> listFragments) {
            this.listFragments = listFragments;
        }

        private List<Fragment> listFragments;
        public MyHorizontalPagerAdapter(FragmentManager fm,List<Fragment> al) {
            super(fm);
            listFragments = al;
        }

        @Override
        public Fragment getItem(int position) {
            return MyHomeFragment.getInstance(mlist.get(position));
        }

        @Override
        public int getCount() {
            return mlist.size();
        }
    }

    @Override
    public void onClick(View v) {


    }

    private void getUserRelation(String userid) {


        requestMaker.UserRelationshipInquiry(userid, new JsonAsyncTask_Info(MyHome.this, true, new JsonAsyncTaskOnComplete() {

            @Override
            public void processJsonObject(Object result) {
                Log.e("TAG",result.toString());
                try {
                    JSONObject mySO = (JSONObject) result;
                    org.json.JSONArray array = mySO
                            .getJSONArray("UserRelationshipInquiry");
                    RelationDes res = new RelationDes();
            res.setType(MyHomeFragment.NONEDATA);
            mlist.add(res);
            listFragmentsa.add(MyHomeFragment.getInstance(res));

                    if (array.getJSONObject(0).has("MessageCode")) {
//                        Toast.makeText(MyHome.this, array.getJSONObject(0).getString("MessageContent").toString(),
//                                Toast.LENGTH_SHORT).show();

                    } else {
                        RelationBean date = JsonTools.getData(result.toString(), RelationBean.class);
                        List<RelationDes> list = date.getData();

                        for (RelationDes rs : list) {
                            rs.setType(MyHomeFragment.HASDATA);
                            mlist.add(rs);

                            listFragmentsa.add(MyHomeFragment.getInstance(rs));
                        }
                    }

                    Message message = Message.obtain();
                    message.what = 0;
                    mHandler.sendMessage(message);



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }));
    }

}
