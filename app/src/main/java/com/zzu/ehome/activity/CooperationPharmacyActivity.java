package com.zzu.ehome.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zzu.ehome.R;
import com.zzu.ehome.application.Constants;
import com.zzu.ehome.bean.PharmacyBean;
import com.zzu.ehome.fragment.CooperationPharmacyFragment;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.view.DialogTips;
import com.zzu.ehome.view.HeadView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mersens on 2016/8/17.
 */
public class CooperationPharmacyActivity extends BaseActivity {
    private GridView gridView;
    private RelativeLayout layout_tel;
    private TextView tv_tel;
    private ScrollView scrollView;
    private String id = null;
    private RequestMaker requestMaker;
    private ImageView img_logo_shop;
    private TextView tv_mame, tv_address, tv_yb, tv_zk;
    private List<String> mList=new ArrayList<>();
    private MyAdapter adapter=null;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_cooperation_pharmacy);
        id = getIntent().getStringExtra(CooperationPharmacyFragment.ID);
        initViews();
        initEvent();
        initDatas();
        if(!CommonUtils.isNotificationEnabled(CooperationPharmacyActivity.this)){
            showTitleDialog("请打开通知中心");
        }
    }


    public void initViews() {
        adapter=new MyAdapter(mList);
        requestMaker = RequestMaker.getInstance();
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        scrollView.fullScroll(ScrollView.FOCUS_UP);
        gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(adapter);
        layout_tel = (RelativeLayout) findViewById(R.id.layout_tel);
        tv_tel = (TextView) findViewById(R.id.tv_tel);
        img_logo_shop = (ImageView) findViewById(R.id.img_logo_shop);
        tv_mame = (TextView) findViewById(R.id.tv_mame);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_yb = (TextView) findViewById(R.id.tv_yb);
        tv_zk = (TextView) findViewById(R.id.tv_zk);

        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "合作药店", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        });
    }

    public void initEvent() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        layout_tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTel();

            }
        });
    }

    public void getTel() {
        String tel = tv_tel.getText().toString();
        if (!TextUtils.isEmpty(tel)) {
            doTel(tel);
        }
    }


    public void doTel(String tel) {
        DialogTips dialog = new DialogTips(this, "", tel,
                "拨打", true, true);
        dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int userId) {

            }
        });

        dialog.show();
        dialog = null;
    }

    public void setDatas(PharmacyBean pb) {
        tv_tel.setText(pb.getPharmacyPhone());
        String imgurl= Constants.EhomeURL + pb.getPicURL().replace("~", "").replace("\\", "/");
        Glide.with(CooperationPharmacyActivity.this).load(imgurl).into(img_logo_shop);
        tv_mame.setText(pb.getPharmacyName());
                tv_address.setText(pb.getPharmacyAddress());
        String zk=pb.getZhekou();
        if(TextUtils.isEmpty(zk)){
            tv_zk.setVisibility(View.GONE);
        }else{
            tv_zk.setText(zk+"折");
        }

        String yb=pb.getYibaoType();
        if(TextUtils.isEmpty(yb)){
            tv_yb.setVisibility(View.GONE);
        }else if(yb.contains(",")){
            String ybs[]=yb.split(",");
            tv_yb.setText("省,市保");
        }else if("1".equals(yb)){
            tv_yb.setText("市保");

        }else if("0".equals(yb)){
            tv_yb.setText("省保");
        }
        if(!TextUtils.isEmpty(pb.getPharmacyPicUrl())){
            String strs[]=pb.getPharmacyPicUrl().split(",");
            for(String s:strs){
                mList.add(s);
            }
            adapter.setList(mList);
            adapter.notifyDataSetChanged();
        }
    }

    public void initDatas() {
        requestMaker.PharmacyDetailInquiry(id, new JsonAsyncTask_Info(
                this, true, new JsonAsyncTaskOnComplete() {
            public void processJsonObject(Object result) {
                JSONObject mySO = (JSONObject) result;
                try {
                    JSONArray array = mySO.getJSONArray("PharmacyDetailInquiry");
                    if (array.getJSONObject(0).has("MessageCode")) {
                        Toast.makeText(CooperationPharmacyActivity.this, array.getJSONObject(0).getString("MessageContent").toString(),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        JSONObject json = array.getJSONObject(0);
                        PharmacyBean pb = new PharmacyBean();
                        String PharmacyPhone = json.getString("PharmacyPhone");
                        pb.setPharmacyPhone(PharmacyPhone);
                        String PharmacyName = json.getString("PharmacyName");
                        pb.setPharmacyName(PharmacyName);
                        String PharmacyAddress = json.getString("PharmacyAddress");
                        pb.setPharmacyAddress(PharmacyAddress);
                        String YibaoType = json.getString("YibaoType");
                        pb.setYibaoType(YibaoType);
                        String Zhekou = json.getString("Zhekou");
                        pb.setZhekou(Zhekou);
                        String Latitude = json.getString("Latitude");
                        pb.setLatitude(Latitude);
                        String Longitude = json.getString("Longitude");
                        pb.setLongitude(Longitude);
                        String id = json.getString("ID");
                        pb.setId(id);
                        pb.setPicURL(json.getString("PicURL"));
                        pb.setPharmacyPicUrl(json.getString("PharmacyPicUrl"));
                        setDatas(pb);
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

    class MyAdapter extends BaseAdapter{
        private List<String> list;
        public MyAdapter(List<String> list){
            this.list=list;
        }

        public void setList(List<String> list){
            this.list=list;
        }

        @Override
        public int getCount() {
            return list==null?0:list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = LayoutInflater.from(CooperationPharmacyActivity.this).inflate(R.layout.gridview_item, null);
            ImageView imageView=(ImageView)v.findViewById(R.id.imageView);
            String str=list.get(position);
            String imgurl= Constants.EhomeURL + str.replace("~", "").replace("\\", "/");
            Glide.with(CooperationPharmacyActivity.this).load(imgurl).into(imageView);
            return v;
        }
    }
}
