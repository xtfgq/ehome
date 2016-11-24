package com.zzu.ehome.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zzu.ehome.R;
import com.zzu.ehome.activity.DoctorDetialActivity;
import com.zzu.ehome.application.Constants;
import com.zzu.ehome.bean.MSDoctorBean;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.WebDatas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mersens on 2016/8/29.
 */
public class HypertensionWithWebFragment extends BaseFragment {
    private RequestMaker requestMaker;
    private View mView;
    private boolean isPrepared;
    public static final String PATH = "path";
    public static final String TYPE = "type";
    private String path = null;
    private WebView webView;
    private ListView listView;
    private LinearLayout layout_tj;
    int type = 0;
    private String goodAt = null;
    private List<MSDoctorBean> mList = null;
    private MyAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_hypertension_web, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requestMaker = RequestMaker.getInstance();
        mView = view;
        path = getArguments().getString(PATH);
        type = getArguments().getInt(TYPE, 0);
//        isPrepared=true;
        initViews();
        initEvent();
//        lazyLoad();
    }

    @Override
    public void onResume() {
        super.onResume();
        initDatas();
        getDoctorListData("", "", goodAt);
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
        initViews();
        initEvent();
        initDatas();
        //如果有异步请求，需在在异步请求结束设置isPrepared=false
        // isPrepared=false;
    }

    public void initViews() {
        goodAt = WebDatas.getTitle(type);
        webView = (WebView) mView.findViewById(R.id.webView);
        listView = (ListView) mView.findViewById(R.id.listView);
        adapter = new MyAdapter(mList);
        listView.setAdapter(adapter);
        layout_tj = (LinearLayout) mView.findViewById(R.id.layout_tj);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(), DoctorDetialActivity.class);
                i.putExtra("doctorid", mList.get(position).getDoctorID());
                i.putExtra("doctorname", mList.get(position).getDoctorName());
                startActivity(i);
            }
        });

    }

    public void initEvent() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setSupportZoom(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setSaveFormData(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setTextSize(WebSettings.TextSize.NORMAL);
        webSettings.setJavaScriptEnabled(true);
        webView.requestFocusFromTouch();

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
//        webView.setWebChromeClient(new WebChromeClient(){
//            @Override
//            public void onProgressChanged(WebView view, int newProgress) {
//                if(newProgress==100){
//                    isPrepared=false;
//                    getDoctorListData("","",goodAt);
//                }
//            }
//        });
    }

    public void initDatas() {
        webView.loadUrl(path);
/*        requestMaker.CommonDiseaseInquiry(new JsonAsyncTask_Info(getActivity(), true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                try {
                    JSONObject mySO = (JSONObject) result;
                    JSONArray array = mySO.getJSONArray("CommonDiseaseInquiry");
                    if (array.getJSONObject(0).has("MessageCode")) {
                        Toast.makeText(getActivity(), array.getJSONObject(0).getString("MessageContent").toString(),
                                Toast.LENGTH_SHORT).show();
                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }));*/


    }

    public void getDoctorListData(String hosptialId, String title, String goodAt) {
        requestMaker.MSDoctorInquiry(hosptialId, title, goodAt, new JsonAsyncTask_Info(getActivity(), true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {

                JSONObject mySO = (JSONObject) result;
                try {
                    JSONArray array = mySO.getJSONArray("MSDoctorInquiry");
                    if (array.getJSONObject(0).has("MessageCode")) {
//                        Toast.makeText(getActivity(), array.getJSONObject(0).getString("MessageContent").toString(),
//                                Toast.LENGTH_SHORT).show();
                    } else {
                        layout_tj.setVisibility(View.VISIBLE);
                        mList = new ArrayList<>();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject json = array.getJSONObject(i);
                            mList.add(getDataFromJson(json));
                        }
                        adapter.setList(mList);
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }));
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
        String DiagnoseCount = json.getString("DiagnoseCount");//就诊量
        bean.setDiagnoseCount(DiagnoseCount);
        String SignCount = json.getString("SignCount");//签约量
        bean.setSignCount(SignCount);
        String Speciaty = json.getString("Speciaty");
        bean.setSpeciaty(Speciaty);
        return bean;
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
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_doctor, null);
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
            String imgurl = Constants.EhomeURL + bean.getImageURL().replace("~", "").replace("\\", "/");
            Glide.with(getActivity()).load(imgurl).error(R.drawable.icon_doctor).into(holder.user_img);
            holder.tv_name.setText(bean.getDoctorName());
            holder.tv_zc.setText(bean.getTitle());
            holder.tv_hospital.setText(bean.getHospitalName());
            holder.tv_sc.setText("擅长：" + bean.getSpeciaty());
            holder.tv_gooat.setText(bean.getGoodAt());
            holder.tv_qyl.setText("签约量：" + bean.getSignCount());
            holder.tv_wzl.setText("问诊量：" + bean.getDiagnoseCount());
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

    public static Fragment getInstance(String path, int type) {
        Bundle b = new Bundle();
        b.putString(PATH, path);
        b.putInt(TYPE, type);
        HypertensionWithWebFragment af = new HypertensionWithWebFragment();
        af.setArguments(b);
        return af;
    }
}
