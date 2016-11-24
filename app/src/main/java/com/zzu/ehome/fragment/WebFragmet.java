package com.zzu.ehome.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.zzu.ehome.R;
import com.zzu.ehome.activity.AppointmentDetailActivity;
import com.zzu.ehome.adapter.MyAppointmetAdapter;
import com.zzu.ehome.adapter.WebAdapter;
import com.zzu.ehome.bean.OrderInquiryByTopmd;
import com.zzu.ehome.bean.OrderInquiryByTopmdDate;
import com.zzu.ehome.bean.TreatmentSearch;
import com.zzu.ehome.bean.TreatmentSearchDate;
import com.zzu.ehome.db.EHomeDao;
import com.zzu.ehome.db.EHomeDaoImpl;
import com.zzu.ehome.utils.DateUtils;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.JsonTools;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.ScreenUtils;
import com.zzu.ehome.utils.SharePreferenceUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2016/9/13.
 */
public class WebFragmet extends BaseFragment {
    private View mView;
    private SwipeMenuListView listView;
    private String userid;

    private LinearLayout layout_none;
    private RequestMaker requestMaker;
    private WebAdapter adapter;
    private EHomeDao dao;
    private String parentid;
    List<TreatmentSearch> list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_appointment, null);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requestMaker = RequestMaker.getInstance();
        userid = SharePreferenceUtil.getInstance(getActivity()).getUserId();
        dao = new EHomeDaoImpl(getActivity());
        parentid = dao.findUserInfoById(userid).getPatientId();
        mView = view;
        initViews();
        initEvent();
        initDatas();
    }


    public void initViews() {
        listView = (SwipeMenuListView) mView.findViewById(R.id.listView);
        layout_none = (LinearLayout) mView.findViewById(R.id.layout_none);
        layout_none.setVisibility(View.GONE);

    }

    public void initEvent() {


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    public void initDatas() {
        requestMaker.TreatmentSearch(parentid, 1 + "", 10000 + "", new JsonAsyncTask_Info(getActivity(), true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                try {
                    JSONObject mySO = (JSONObject) result;

                    org.json.JSONArray array = mySO
                            .getJSONArray("TreatmentSearch");
                    stopProgressDialog();
                    if (array.getJSONObject(0).has("MessageCode")) {
                        layout_none.setVisibility(View.VISIBLE);
                    } else {

                        TreatmentSearchDate date = JsonTools.getData(result.toString(), TreatmentSearchDate.class);
                        list = date.getDate();
                        adapter = new WebAdapter(getActivity(), list);
                        for (int i = 0; i < list.size(); i++) {
                            TreatmentSearch ts = list.get(i);
//                            list.get(i).setOverdue(DateUtils.Compare_date(ts.getTime(), DateUtils.getFormatTime()));
                            list.get(i).setOpen(DateUtils.Compare_date(ts.getTime(), DateUtils.getFormatTime()));
//                            if (DateUtils.Compare_date(ts.getTime(), DateUtils.getFormatTime())) {
//                                list.get(i).setOpen(false);
//                            }
                        }
                        listView.setAdapter(adapter);
                        SwipeMenuCreator creator = new SwipeMenuCreator() {

                            @Override
                            public void create(SwipeMenu menu) {
                                // Create different menus depending on the view type
                                switch (menu.getViewType()) {
                                    case 0:
                                        createMenu1(menu);
                                        break;
                                    case 1:
                                        createMenu2(menu);
                                        break;

                                }
                            }

                            private void createMenu1(SwipeMenu menu) {
                                SwipeMenuItem delItem = new SwipeMenuItem(
                                        getActivity());
                                // set item background

                                delItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                                // set item width
                                delItem.setWidth(ScreenUtils.dip2px(getActivity(), 90));
                                // set item title
                                delItem.setTitle("取消预约");
                                // set item title fontsize
                                delItem.setTitleSize(18);
                                // set item title font color
                                delItem.setTitleColor(Color.WHITE);
                                // add to menu
                                menu.addMenuItem(delItem);
                            }
                            private void createMenu2(SwipeMenu menu) {
                                SwipeMenuItem item1 = new SwipeMenuItem(
                                        getActivity());

                                menu.addMenuItem(item1);


                            }


                        };

                        listView.setMenuCreator(creator);
                        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                            @Override
                            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
                                TreatmentSearch item = list.get(position);
                                switch (index) {
                                    case 0:
                                        // open
                                        deleteRecent(item, position);
                                        break;

                                }
                            }
                        });



                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }));


    }


    @Override
    protected void lazyLoad() {

    }

    public void deleteRecent(TreatmentSearch bean, final int position) {

        String id = bean.getReservationId();
        requestMaker.TreatmentCancel(id, new JsonAsyncTask_Info(getActivity(), true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                try {

                    JSONObject mySO = (JSONObject) result;
                    org.json.JSONArray array = mySO
                            .getJSONArray("TreatmentCancel");
                    if (array.getJSONObject(0).has("MessageCode")) {
                        Toast.makeText(getActivity(), array.getJSONObject(0).getString("MessageContent").toString(),
                                Toast.LENGTH_SHORT).show();
                        list.remove(position);
                        if (list.size() == 0) {
                            layout_none.setVisibility(View.VISIBLE);
                        }
                        adapter.notifyDataSetChanged();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));

    }

    public static Fragment getInstance() {
        return new WebFragmet();
    }
}
