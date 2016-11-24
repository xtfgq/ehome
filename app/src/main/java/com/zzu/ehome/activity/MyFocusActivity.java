package com.zzu.ehome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zzu.ehome.R;
import com.zzu.ehome.application.Constants;
import com.zzu.ehome.bean.MSDoctorBean;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.view.HeadView;
import com.zzu.ehome.view.PullToRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by zzu on 2016/4/15.
 */
public class MyFocusActivity extends BaseActivity {
    private ListView listView;
    private String userid;
    private RequestMaker requestMaker;
    private RelativeLayout rlFocus;
    private PullToRefreshLayout pulltorefreshlayout;
    private MyAdapter adapter=null;
    private List<MSDoctorBean> mList = new ArrayList<MSDoctorBean>();
    private int page=1;
    private int pageSize=10;
    private boolean isFirst = true;
    private boolean isReflash=false;
    private boolean isLoading=false;



    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_focus);
//        EventBus.getDefault().register(this);
        requestMaker = RequestMaker.getInstance();
        userid = SharePreferenceUtil.getInstance(MyFocusActivity.this).getUserId();
        initViews();
        initEvent();

    }

    @Override
    protected void onResume() {
        super.onResume();
        isFirst = true;
        isReflash=false;
        isLoading=false;
        mList.clear();
        initDatas();
    }

    public void initViews() {
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "我的医生", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        });
        listView = (ListView) findViewById(R.id.listView);
        rlFocus = (RelativeLayout) findViewById(R.id.rl_guan);
        pulltorefreshlayout = (PullToRefreshLayout) findViewById(R.id.refresh_view);
    }

    public void initEvent() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i=new Intent(MyFocusActivity.this, DoctorDetialActivity.class);
                i.putExtra("doctorid",mList.get(position).getDoctorID());
                i.putExtra("doctorname",mList.get(position).getDoctorName());
                startActivity(i);
            }
        });
        pulltorefreshlayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                page=1;
                isFirst = true;
                isReflash=true;
                isLoading=false;
                getDoctorListData(userid,page);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                page++;
                isLoading=true;
                isReflash=false;
                getDoctorListData(userid,page);

            }
        });
    }

    public void initDatas(){
        adapter=new MyAdapter(mList);
        listView.setAdapter(adapter);
        getDoctorListData(userid,page);
    }
    class MyAdapter extends BaseAdapter {
        private List<MSDoctorBean> list;

        public MyAdapter(List<MSDoctorBean> list) {
            this.list = list;
        }

        public void setList(List<MSDoctorBean> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
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
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(MyFocusActivity.this).inflate(R.layout.item_doctor, null);
                holder.icon_state = (ImageView) convertView.findViewById(R.id.icon_state);
                holder.user_img = (ImageView) convertView.findViewById(R.id.user_img);
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tv_zc = (TextView) convertView.findViewById(R.id.tv_zc);
                holder.tv_hospital = (TextView) convertView.findViewById(R.id.tv_hospital);
                holder.tv_sc = (TextView) convertView.findViewById(R.id.tv_sc);
                holder.tv_gooat = (TextView) convertView.findViewById(R.id.tv_gooat);
                holder.tv_qyl = (TextView) convertView.findViewById(R.id.tv_qyl);
                holder.tv_wzl = (TextView) convertView.findViewById(R.id.tv_wzl);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            MSDoctorBean bean = list.get(position);
            if(bean.getIsLine().equals("1")){
                holder.icon_state.setImageResource(R.mipmap.icon_online_g);
            }
            String imgurl = Constants.EhomeURL + bean.getImageURL().replace("~", "").replace("\\", "/");
            Glide.with(MyFocusActivity.this).load(imgurl).error(R.drawable.icon_doctor).into(holder.user_img);
            holder.tv_name.setText(bean.getDoctorName());
            holder.tv_zc.setText(bean.getTitle());
            holder.tv_hospital.setText(bean.getHospitalName());
            holder.tv_sc.setText("擅长："+bean.getSpeciaty());
            holder.tv_gooat.setText(bean.getGoodAt());
            holder.tv_qyl.setText("签约量："+bean.getSignCount());
            holder.tv_wzl.setText("问诊量："+bean.getDiagnoseCount());
            return convertView;
        }
    }

    public static class ViewHolder {
        public ImageView icon_state;
        public ImageView user_img;
        public TextView tv_name;
        public TextView tv_zc;
        public TextView tv_hospital;
        public TextView tv_sc;
        public TextView tv_gooat;
        public TextView tv_qyl;
        public TextView tv_wzl;

    }
    /**
     * 解析json数据
     *
     * @param json
     * @return
     * @throws JSONException
     */
    public MSDoctorBean getDataFromJson(JSONObject json) throws JSONException {
        MSDoctorBean bean = new MSDoctorBean();
        String ImageURL = json.getString("ImageURL");
        bean.setImageURL(ImageURL);
        String Description = json.getString("Description");
        bean.setDescription(Description);
        String IsLine = json.getString("IsLine");
        bean.setIsLine(IsLine);
        String GoodCode = json.getString("GoodCode");
        bean.setGoodCode(GoodCode);
        String DoctorName = json.getString("DoctorName");
        bean.setDoctorName(DoctorName);
        String Title = json.getString("Title");
        bean.setTitle(Title);
        String GoodAt = json.getString("GoodAt");
        bean.setGoodAt(GoodAt);
        String HospitalName = json.getString("HospitalName");
        bean.setHospitalName(HospitalName);
        String HospitalID = json.getString("HospitalID");
        bean.setHospitalID(HospitalID);
        String Mobile = json.getString("Mobile");
        bean.setMobile(Mobile);
        String DoctorID = json.getString("DoctorID");
        bean.setDoctorID(DoctorID);
        String Department = json.getString("Department");
        bean.setDepartment(Department);
        String TitleCode = json.getString("TitleCode");
        bean.setTitleCode(TitleCode);
        String DoctorNo = json.getString("DoctorNo");
        bean.setDoctorNo(DoctorNo);
        String DiagnoseCount=json.getString("DiagnoseCount");//就诊量
        bean.setDiagnoseCount(DiagnoseCount);
        String SignCount=json.getString("SignCount");//签约量

        bean.setSignCount(SignCount);
        String Speciaty=json.getString("Speciaty");
        bean.setSpeciaty(Speciaty);

        return bean;
    }
    public void getDoctorListData(String id,int page) {
        requestMaker.MSDoctorSignInquiry(id, pageSize+"",page+"","", "","", new JsonAsyncTask_Info(MyFocusActivity.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                JSONObject mySO = (JSONObject) result;
                try {
                    JSONArray array = mySO.getJSONArray("MSDoctorSignInquiry");
                    if (array.getJSONObject(0).has("MessageCode")) {
                        if(isFirst){
                            rlFocus.setVisibility(View.VISIBLE);
                            listView.setVisibility(View.GONE);
                            isFirst=false;
                        }
//                        Toast.makeText(MyFocusActivity.this, array.getJSONObject(0).getString("MessageContent").toString(),
//                                Toast.LENGTH_SHORT).show();

                    } else {
                        if (isFirst) {
                            rlFocus.setVisibility(View.GONE);
                            listView.setVisibility(View.VISIBLE);
                            isFirst=false;
                        }

                        List<MSDoctorBean> list = new ArrayList<>();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject json = array.getJSONObject(i);
                            list.add(getDataFromJson(json));
                        }
                        if(isReflash){
                            mList.clear();
                        }
                        mList.addAll(list);
                        adapter.setList(mList);
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    if(isReflash){
                        isReflash=false;
                        isLoading=false;
                        pulltorefreshlayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                    }else if(isLoading){
                        isLoading=false;
                        isReflash=false;
                        pulltorefreshlayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    }
                }
            }
        }));
    }
}
