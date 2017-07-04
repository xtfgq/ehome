package com.zzu.ehome.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.adapter.BaseListAdapter;
import com.zzu.ehome.bean.CityBean;
import com.zzu.ehome.fragment.BaseFragment;
import com.zzu.ehome.fragment.DoctorListFragment;
import com.zzu.ehome.reciver.EventType;
import com.zzu.ehome.reciver.RxBus;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.view.DialogTips;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Mersens on 2016/8/26.
 */
public class PrivateDoctorFragment extends BaseFragment implements DoctorListFragment.OnSearchResultListener {
    private View mView;
    private RelativeLayout layout_all_yiyuan, layout_all_zhicheng, layout_all_manbing;
    private ImageView img_arrow_yy, img_arrow_zc, img_arrow_mb;
    private PopupWindow pop;
    private RequestMaker requestMaker;
    private List<CityBean> zcList = new ArrayList<CityBean>();
    private List<CityBean> mbList = new ArrayList<CityBean>();
    private List<CityBean> yyList = new ArrayList<CityBean>();
    private List<CityBean> allList = new ArrayList<CityBean>();
    //区域map
    private Map<String,List<CityBean>> yMap=new ArrayMap<String,List<CityBean>>();
    //街道map
    private Map<String,List<CityBean>> yyMap=new ArrayMap<String,List<CityBean>>();
    public static final String QBYY = "全部医院";
    public static final String QBZC = "全部职称";
    public static final String QBMB = "全部慢病";
    private MyAdapter adapter = null;
    private View vTop;
    private Type type;
    private String hosptialId = "";
    private String title = "";
    private String goodAt = "";
    private TextView tv_yy, tv_zc, tv_mb;
    private boolean isNoData=false;
    private boolean isFisrt=true;
    private SupperBaseActivity activity;
    private CompositeSubscription compositeSubscription;
    private int first=0,second=0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_pmd, null);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity=(SupperBaseActivity)context;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;
        requestMaker = RequestMaker.getInstance();
        initViews();
        allList.clear();
        zcList.clear();

        mbList.clear();
//        yyList.clear();
        yyList.clear();
        initDatas();
        initEvent();


    }


    public void initViews() {
        tv_yy = (TextView) mView.findViewById(R.id.tv_yy);
        tv_zc = (TextView) mView.findViewById(R.id.tv_zc);
        tv_mb = (TextView) mView.findViewById(R.id.tv_mb);
        layout_all_yiyuan = (RelativeLayout) mView.findViewById(R.id.layout_all_yiyuan);
        layout_all_zhicheng = (RelativeLayout) mView.findViewById(R.id.layout_all_zhicheng);
        layout_all_manbing = (RelativeLayout) mView.findViewById(R.id.layout_all_manbing);
        img_arrow_yy = (ImageView) mView.findViewById(R.id.img_arrow_yy);
        img_arrow_zc = (ImageView) mView.findViewById(R.id.img_arrow_zc);
        img_arrow_mb = (ImageView) mView.findViewById(R.id.img_arrow_mb);
        setOnlyTileViewMethod(mView, "家庭医生");
        vTop = mView.findViewById(R.id.v_top);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int h = CommonUtils.getStatusHeight(getActivity());
            ViewGroup.LayoutParams params = vTop.getLayoutParams();
            params.height = h;
            params.width = ViewGroup.LayoutParams.FILL_PARENT;
            vTop.setLayoutParams(params);
            vTop.setBackgroundResource(R.color.actionbar_color);
        } else {
            vTop.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void initEvent() {
        layout_all_yiyuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!activity.isNetWork){
                    activity.showNetWorkErrorDialog();
                    return;
                }
                showPop(Type.HOSPTIAL);
                type = Type.HOSPTIAL;
            }
        });
        layout_all_zhicheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!activity.isNetWork){
                    activity.showNetWorkErrorDialog();
                    return;
                }
                showPop(Type.POSITIONAL_TITLES);
                type = Type.POSITIONAL_TITLES;
            }
        });
        layout_all_manbing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!activity.isNetWork){
                    activity.showNetWorkErrorDialog();
                    return;
                }
                showPop(Type.DISEASE);
                type = Type.DISEASE;
            }
        });
        compositeSubscription = new CompositeSubscription();

        //监听订阅事件
        Subscription subscription = RxBus.getInstance().toObservable()
                .observeOn(AndroidSchedulers.mainThread())

                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object event) {
                        if (event == null) {
                            return;
                        }

                        if (event instanceof EventType){
                            EventType type=(EventType)event;
                            if("area".equals(type.getType())){
                                allList.clear();
                                zcList.clear();
                                mbList.clear();
                                yyList.clear();
                                reflushDatas();
                            }

                        }


                    }
                });
        //subscription交给compositeSubscription进行管理，防止内存溢出
        compositeSubscription.add(subscription);
    }

    public void initDatas() {

        requestMaker.CategoryInfoInquiry("",new JsonAsyncTask_Info(getActivity(), true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                JSONObject mySO = (JSONObject) result;
                try {
                    JSONArray array = mySO.getJSONArray("CategoryInfoInquiry");
                    if (array.getJSONObject(0).has("MessageCode")) {
//                        Toast.makeText(getActivity(), array.getJSONObject(0).getString("MessageContent").toString(),
//                                Toast.LENGTH_SHORT).show();
                    } else {

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject json = array.getJSONObject(i);
                            CityBean cb = new CityBean();
                            String id = json.getString("ID");
                            cb.setId(id);
                            String Value = json.getString("Title");
                            cb.setTitle(Value);
                            String parentId=json.getString("ParentID");
                            cb.setParentID(parentId);
                            allList.add(cb);
                            if(parentId.equals("0")) {
                                yyList.add(cb);
                            }
                        }
                        for(int i=0;i<yyList.size();i++){
                            List<CityBean> clist=new ArrayList<CityBean>();
                            for(CityBean bean:allList){
                              if(yyList.get(i).getId().equals(bean.getParentID())){
                                  clist.add(bean);
                              }
                            }
                            yMap.put(yyList.get(i).getId(),clist);
                        }

                        for (Map.Entry<String, List<CityBean>> entry : yMap.entrySet()) {
                            for(int i=0;i<entry.getValue().size();i++){
                                List<CityBean> clist=new ArrayList<CityBean>();
                                for(CityBean bean:allList){

                                    if(entry.getValue().get(i).getId().equals(bean.getParentID())){
                                        clist.add(bean);

                                    }

                                }
                                yyMap.put(entry.getValue().get(i).getId(),clist);
                            }

                        }
                        zcList.addAll(yMap.get(yyList.get(first).getId()));
                        mbList.addAll(yyMap.get(zcList.get(first).getId()));
                        tv_yy.setText(yyList.get(0).getTitle());
                        tv_zc.setText(zcList.get(0).getTitle());
                        tv_mb.setText(yyMap.get(zcList.get(0).getId()).get(0).getTitle());
                        hosptialId=yyMap.get(zcList.get(0).getId()).get(0).getId();

                        addFragment(Type.HOSPTIAL);
//                        getArea(yyList.get(0).getId());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {

            }

        }));

//        CityBean cb = new CityBean();
//        cb.setTitle("郑州市");
//
//        yyList.add(cb);
//        tv_yy.setText(yyList.get(0).getTitle());
//        CityBean cb2 = new CityBean();
//        cb2.setTitle("高新区");
//        zcList.add(cb2);
//        tv_zc.setText("高新区");
//        CityBean cb3 = new CityBean();
//        cb3.setTitle("盛和苑");
//        mbList.add(cb3);
//        tv_mb.setText("盛和苑");
//        addFragment(Type.HOSPTIAL);

    }
    public void reflushDatas() {

        requestMaker.CategoryInfoInquiry("",new JsonAsyncTask_Info(getActivity(), true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                JSONObject mySO = (JSONObject) result;
                try {
                    JSONArray array = mySO.getJSONArray("CategoryInfoInquiry");
                    if (array.getJSONObject(0).has("MessageCode")) {
//                        Toast.makeText(getActivity(), array.getJSONObject(0).getString("MessageContent").toString(),
//                                Toast.LENGTH_SHORT).show();
                    } else {

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject json = array.getJSONObject(i);
                            CityBean cb = new CityBean();
                            String id = json.getString("ID");
                            cb.setId(id);
                            String Value = json.getString("Title");
                            cb.setTitle(Value);
                            String parentId=json.getString("ParentID");
                            cb.setParentID(parentId);
                            allList.add(cb);
                            if(parentId.equals("0")) {
                                yyList.add(cb);
                            }
                        }
                        for(int i=0;i<yyList.size();i++){
                            List<CityBean> clist=new ArrayList<CityBean>();
                            for(CityBean bean:allList){
                                if(yyList.get(i).getId().equals(bean.getParentID())){
                                    clist.add(bean);
                                }
                            }
                            yMap.put(yyList.get(i).getId(),clist);
                        }

                        for (Map.Entry<String, List<CityBean>> entry : yMap.entrySet()) {
                            for(int i=0;i<entry.getValue().size();i++){
                                List<CityBean> clist=new ArrayList<CityBean>();
                                for(CityBean bean:allList){

                                    if(entry.getValue().get(i).getId().equals(bean.getParentID())){
                                        clist.add(bean);

                                    }

                                }
                                yyMap.put(entry.getValue().get(i).getId(),clist);
                            }
                            zcList.addAll(yMap.get(yyList.get(first).getId()));
                            mbList.addAll(yyMap.get(zcList.get(second).getId()));

                        }

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


    public enum Type {
        HOSPTIAL, POSITIONAL_TITLES, DISEASE;
    }

    class MyOnItemClickListener implements AdapterView.OnItemClickListener {
        private Type t;

        public MyOnItemClickListener(Type t) {
            this.t = t;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(!activity.isNetWork){
                activity.showTitleDialog("网络连接已断开，请检查网络设置");
                return;
            }
            switch (t) {
                case HOSPTIAL:
                    String value1=yyList.get(position).getTitle();
//                    if(QBYY.equals(value1)){
//                        hosptialId ="";
//                    }else{
//                        hosptialId =yyList.get(position).getId();
//                    }
                    if(position==first) {
                        turnToDown(img_arrow_yy);

                    }else {
                        tv_yy.setText(yyList.get(position).getTitle());
                        zcList.clear();
                        zcList.addAll(yMap.get(yyList.get(position).getId()));
                        mbList.clear();
                        mbList.addAll(yyMap.get(zcList.get(0).getId()));
                        tv_zc.setText(zcList.get(0).getTitle());
                        tv_mb.setText(yyMap.get(zcList.get(0).getId()).get(position).getTitle());
                        hosptialId = yyMap.get(zcList.get(0).getId()).get(0).getId();
                        Log.e("hosptialId", hosptialId);
                        first = position;
                        addFragment(Type.HOSPTIAL);
                        turnToDown(img_arrow_yy);
                    }

//                    getArea(yyList.get(position).getId());
                    popDismiss();
                    break;
                case POSITIONAL_TITLES:
                   String value2= zcList.get(position).getTitle();
                    if(QBZC.equals(value2)){
                        title="";
                    }else{
                        title=value2;
                    }
                    Log.e("title", title);
                    second=position;
                    mbList.clear();
                    mbList.addAll(yyMap.get(zcList.get(position).getId()));
                    tv_mb.setText(yyMap.get(zcList.get(position).getId()).get(0).getTitle());
                    hosptialId=yyMap.get(zcList.get(position).getId()).get(0).getId();
                    tv_zc.setText(zcList.get(position).getTitle());
                    turnToDown(img_arrow_zc);
                    addFragment(Type.POSITIONAL_TITLES);
//                    getOrganization(zcList.get(position).getId());
                    popDismiss();
                    break;
                case DISEASE:
//                    String value3= yyMap.get(zcList.get(position).getId()).get(position).getTitle();
//                    hosptialId="3";
//                    mbList.addAll(yyMap.get(zcList.get(position).getId()));
                    hosptialId=mbList.get(position).getId();
                    tv_mb.setText(mbList.get(position).getTitle());
                    Log.e("goodAt", goodAt);
                    addFragment(Type.DISEASE);
                    turnToDown(img_arrow_mb);
                    popDismiss();
                    break;
            }
        }
    }

    public void showPop(Type t) {
        View mView = LayoutInflater.from(getActivity()).inflate(R.layout.pop_listview, null);
        ListView listView = (ListView) mView.findViewById(R.id.listView);
        adapter = new MyAdapter(getActivity(), null);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new MyOnItemClickListener(t));
        pop = new PopupWindow(mView);
        pop.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        pop.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        pop.setTouchable(true);
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                pop = null;
                popDismiss();

            }
        });
        pop.setBackgroundDrawable(new BitmapDrawable());
        switch (t) {
            case HOSPTIAL:
                adapter.setList(yyList);
                adapter.notifyDataSetChanged();
                pop.showAsDropDown(layout_all_yiyuan);
                turnToUp(img_arrow_yy);
                break;
            case POSITIONAL_TITLES:
                adapter.setList(zcList);
                adapter.notifyDataSetChanged();
                pop.showAsDropDown(layout_all_zhicheng);
                turnToUp(img_arrow_zc);
                break;
            case DISEASE:
                adapter.setList(mbList);
                adapter.notifyDataSetChanged();
                pop.showAsDropDown(layout_all_manbing);
                turnToUp(img_arrow_mb);
                break;
        }
/*        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (type) {
                    case HOSPTIAL:
                        turnToDown(img_arrow_yy);
                        popDismiss();
                        break;
                    case POSITIONAL_TITLES:
                        turnToDown(img_arrow_zc);
                        popDismiss();
                        break;
                    case DISEASE:
                        turnToDown(img_arrow_mb);
                        popDismiss();
                        break;
                }
            }
        });*/
    }

    public void popDismiss() {
        if (pop != null && pop.isShowing()) {
            pop.dismiss();
            pop = null;
        }
        switch (type) {
            case HOSPTIAL:
                turnToDown(img_arrow_yy);
                break;
            case POSITIONAL_TITLES:
                turnToDown(img_arrow_zc);
                break;
            case DISEASE:
                turnToDown(img_arrow_mb);
                break;
        }
    }
    public void getArea(String id){
        requestMaker.CategoryInfoInquiry(id,new JsonAsyncTask_Info(getActivity(), true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                JSONObject mySO = (JSONObject) result;
                try {
                    JSONArray array = mySO.getJSONArray("CategoryInfoInquiry");
                    if (array.getJSONObject(0).has("MessageCode")) {
//                        Toast.makeText(getActivity(), array.getJSONObject(0).getString("MessageContent").toString(),
//                                Toast.LENGTH_SHORT).show();
                    } else {
                        zcList.clear();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject json = array.getJSONObject(i);
                            CityBean cb = new CityBean();
                            String id = json.getString("ID");
                            cb.setId(id);
                            String Value = json.getString("Title");
                            cb.setTitle(Value);
                            zcList.add(cb);
                        }
                        tv_zc.setText(zcList.get(0).getTitle());
                        getOrganization(zcList.get(0).getId());
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
    public void getOrganization(String id){
        requestMaker.CategoryInfoInquiry(id,new JsonAsyncTask_Info(getActivity(), true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                JSONObject mySO = (JSONObject) result;
                try {
                    JSONArray array = mySO.getJSONArray("CategoryInfoInquiry");
                    if (array.getJSONObject(0).has("MessageCode")) {
//                        Toast.makeText(getActivity(), array.getJSONObject(0).getString("MessageContent").toString(),
//                                Toast.LENGTH_SHORT).show();
                    } else {
                        mbList.clear();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject json = array.getJSONObject(i);
                            CityBean cb = new CityBean();
                            String id = json.getString("ID");
                            cb.setId(id);
                            String Value = json.getString("Title");
                            cb.setTitle(Value);
                            mbList.add(cb);
                        }
                        tv_mb.setText(mbList.get(0).getTitle());

//                            hosptialId =yyList.get(0).getId();


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


    class MyAdapter extends BaseListAdapter<CityBean> {
        private List<CityBean> mList;

        public MyAdapter(Context context, List objects) {
            super(context, objects);
            this.mList = objects;
        }

        public void setList(List<CityBean> mList) {
            this.mList = mList;
            super.setList(mList);
        }

        @Override
        public View getGqView(int position, View convertView, ViewGroup parent) {
            View mView = getInflater().inflate(R.layout.pop_item, null);
            TextView tv_name = (TextView) mView.findViewById(R.id.tv_name);
            tv_name.setText(mList.get(position).getTitle());
            return mView;
        }
    }

    public void addFragment(Type type) {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, creatFragment(type));
        ft.commitAllowingStateLoss();
    }

    public Fragment creatFragment(Type type) {
        Fragment mFragment = null;
        switch (type) {
            case HOSPTIAL:
                mFragment = DoctorListFragment.getInstance(hosptialId);
                break;
            case POSITIONAL_TITLES:
                mFragment = DoctorListFragment.getInstance(hosptialId);
                break;
            case DISEASE:
                mFragment = DoctorListFragment.getInstance(hosptialId);
                break;
        }
        if(mFragment!=null){
            ((DoctorListFragment)mFragment).setOnSearchResultListener(this);
        }
        return mFragment;
    }

    public static Fragment getInstance() {
        return new PrivateDoctorFragment();
    }

    @Override
    protected void lazyLoad() {

    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){
        }else{
            if(isNoData){
                if (TextUtils.isEmpty(hosptialId)) {
                    isFisrt=false;
                    show("正在签约，敬请期待");
                }
            }
        }
    }
    @Override
    public void onNoData(boolean isNoData) {
        this.isNoData=isNoData;
    }


    private void turnToUp(ImageView iv) {
        Matrix matrix = new Matrix();
        Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(R.mipmap.icon_arrow_gray)).getBitmap();
        // 设置旋转角度
        matrix.setRotate(180f);
        // 重新绘制Bitmap
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        iv.setImageBitmap(bitmap);
    }

    private void turnToDown(ImageView iv) {
        Matrix matrix = new Matrix();
        Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(R.mipmap.icon_arrow_gray)).getBitmap();
        // 设置旋转角度
        matrix.setRotate(360f);
        // 重新绘制Bitmap
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        iv.setImageBitmap(bitmap);
    }

    public  void show(String message) {

        DialogTips dialog = new DialogTips(getActivity(), message, "确定");
        dialog.show();
        dialog = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeSubscription.unsubscribe();
    }
}
