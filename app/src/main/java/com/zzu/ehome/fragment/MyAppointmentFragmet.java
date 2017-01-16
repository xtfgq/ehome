package com.zzu.ehome.fragment;

import android.content.Context;
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
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.zzu.ehome.R;
import com.zzu.ehome.activity.SupperBaseActivity;
import com.zzu.ehome.adapter.MyAppointmetAdapter;
import com.zzu.ehome.bean.OrderInquiryByTopmd;
import com.zzu.ehome.bean.OrderInquiryByTopmdDate;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_ECGInfo;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.JsonTools;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.ScreenUtils;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.view.RefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.zzu.ehome.R.id.refreshLayout;

/**
 * Created by Administrator on 2016/9/13.
 */
public class MyAppointmentFragmet extends BaseFragment {

    private View mView;
    private SwipeMenuListView listView;
    private String userid;

    private LinearLayout layout_none;
    private RequestMaker requestMaker;
    private MyAppointmetAdapter adapter;
    List<OrderInquiryByTopmd> list = new ArrayList<OrderInquiryByTopmd>();
    private SupperBaseActivity activity;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (SupperBaseActivity) context;

    }

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
        if (!activity.isNetWork) {
            activity.showNetWorkErrorDialog();
            return;
        }
        requestMaker.OrderInquiryByTopmd(userid, new JsonAsyncTask_Info(getActivity(), true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                try {
                    JSONObject mySO = (JSONObject) result;
                    JSONArray array = mySO.getJSONArray("OrderInquiryByTopmd");
                    if (array.getJSONObject(0).has("MessageCode")) {
//                        Toast.makeText(getActivity(), array.getJSONObject(0).getString("MessageContent").toString(),
//                                Toast.LENGTH_SHORT).show();
                        layout_none.setVisibility(View.VISIBLE);
                    } else {
                        OrderInquiryByTopmdDate date = JsonTools.getData(result.toString(), OrderInquiryByTopmdDate.class);
                        List<OrderInquiryByTopmd> temp = date.getData();
                        list.clear();
                        for (OrderInquiryByTopmd bean : temp) {
                            if (!bean.getOrderStatus().equals("02")) {
                                if (bean.getOrderStatus().equals("07")) {
                                    bean.setType(1);
                                } else {
                                    bean.setType(0);
                                }
                                list.add(bean);
                            }
                        }
                        if (list.size() == 0) {
                            layout_none.setVisibility(View.VISIBLE);
                        }

                        if (adapter == null) {
                            adapter = new MyAppointmetAdapter(getActivity(), list);
                            listView.setAdapter(adapter);
                        } else {
                            adapter.notifyDataSetChanged();
                        }

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
                            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                                OrderInquiryByTopmd item = list.get(position);
                                switch (index) {
                                    case 0:
                                        // open
                                        deleteRecent(item, position);
                                        break;

                                }
                                return false;
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

    public static Fragment getInstance() {
        return new MyAppointmentFragmet();
    }

    public void deleteRecent(OrderInquiryByTopmd bean, final int position) {

        String id = bean.getOrderID();
        requestMaker.OrderCancel(id, new JsonAsyncTask_ECGInfo(getActivity(), true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                try {
                    JSONObject mySO = (JSONObject) result;
                    JSONArray array = mySO
                            .getJSONArray("Result");
                    if (array.getJSONObject(0).has("MessageCode")) {
                        Toast.makeText(getActivity(), "订单取消成功",
                                Toast.LENGTH_SHORT).show();
                        if (Integer.valueOf(array.getJSONObject(0).getString("MessageCode")) == 1) {
                            list.remove(position);
                            adapter.notifyDataSetChanged();
                            if (list.size() == 0) {
                                layout_none.setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        Toast.makeText(getActivity(), array.getJSONObject(0).getString("MessageContent").toString(),
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }));


    }
}
