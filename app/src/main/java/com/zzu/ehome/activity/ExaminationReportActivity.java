package com.zzu.ehome.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.zzu.ehome.R;
import com.zzu.ehome.adapter.MedicalExaminationAdapter;
import com.zzu.ehome.bean.MedicalBean;
import com.zzu.ehome.bean.MedicalDate;
import com.zzu.ehome.bean.User;
import com.zzu.ehome.db.EHomeDao;
import com.zzu.ehome.db.EHomeDaoImpl;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.JsonTools;
import com.zzu.ehome.utils.PermissionsChecker;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.view.DialogTips;
import com.zzu.ehome.view.HeadView;
import com.zzu.ehome.view.RefreshLayout;

import org.json.JSONObject;

import java.util.List;

import static android.R.attr.data;
import static android.R.attr.id;
import static com.zzu.ehome.R.id.refreshLayout;
import static com.zzu.ehome.db.DBHelper.mContext;

/**
 * Created by Mersens on 2016/8/20.
 */
public class ExaminationReportActivity extends BaseActivity {
    private String userid;
    private ListView listView;
    private LinearLayout layout_none;
    private RelativeLayout layout_search,layout_add;
    private static final String PACKAGE_URL_SCHEME = "package:";
    private EHomeDao dao;
    private User dbUser;
    private PermissionsChecker mPermissionsChecker; // 权限检测器
    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{

            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private RequestMaker requestMaker;
    private List<MedicalBean> mList;
    private MedicalExaminationAdapter adapter;
    public static final int ADD= 0x25;
    private RefreshLayout refreshLayout;
    private boolean isRefresh=false;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_examination_report);
        requestMaker = RequestMaker.getInstance();
        dao = new EHomeDaoImpl(this);
        mPermissionsChecker = new PermissionsChecker(this);
        initViews();
        initEvent();
        initDatas();
        if(!CommonUtils.isNotificationEnabled(ExaminationReportActivity.this)){
            showTitleDialog("请打开通知中心");
        }
    }

    public void initViews() {
        listView = (ListView) findViewById(R.id.listView);
        layout_none = (LinearLayout) findViewById(R.id.layout_none);
        layout_none.setVisibility(View.GONE);
        layout_search=(RelativeLayout)findViewById(R.id.layout_search);
        layout_add=(RelativeLayout) findViewById(R.id.layout_add);
        refreshLayout=(RefreshLayout) findViewById(R.id.refreshLayout);
        setDefaultViewMethod(R.mipmap.icon_arrow_left, "体检报告", R.mipmap.icon_add_zoushi, new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        }, new HeadView.OnRightClickListener() {
            @Override
            public void onClick() {
                if(!isNetWork){
                    showNetWorkErrorDialog();
                    return;
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
                        showMissingPermissionDialog();
                        return;
                    }
                }
                Intent intent = new Intent(ExaminationReportActivity.this, CreateReportActivity.class);
                intent.putExtra("ADD","");
                startActivityForResult(intent, ADD);
            }
        });

    }

    public void initEvent() {
//        layout_search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dbUser = dao.findUserInfoById(userid);
//                if(dbUser.getUserno()==null|| TextUtils.isEmpty(dbUser.getUserno())){
//                    startActivity(new Intent(ExaminationReportActivity.this,PersonalCenterInfo.class));
//                    return;
//                }
//
//                startActivity(new Intent(ExaminationReportActivity.this, SmartWebView.class));
//            }
//        });
//        layout_add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
//                        showMissingPermissionDialog();
//                        return;
//                    }
//                }
//                Intent intent = new Intent(ExaminationReportActivity.this, CreateReportActivity.class);
//                intent.putExtra("ADD","");
//                startActivityForResult(intent, ADD);
//
//
//            }
//        });
        refreshLayout.setOnRefreshListener(new RefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout pullToRefreshLayout) {
                if(!isNetWork){
                    if(!isNetWork){
                        refreshLayout.refreshFinish(RefreshLayout.FAIL);
                        return;
                    }
                    return;
                }
                isRefresh=true;

                initDatas();
            }
        });

    }

    // 显示缺失权限提示
    private void showMissingPermissionDialog() {
        DialogTips dialog = new DialogTips(this, "请点击设置，打开所需存储权限",
                "确定");
        dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int userId) {
                startAppSettings();

            }
        });

        dialog.show();
        dialog = null;

    }

    // 启动应用的设置
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse(PACKAGE_URL_SCHEME + getPackageName()));
        startActivity(intent);
    }

    public void initDatas() {
        userid = SharePreferenceUtil.getInstance(ExaminationReportActivity.this).getUserId();
        requestMaker.MeidicalReportInquiry(userid, new JsonAsyncTask_Info(ExaminationReportActivity.this, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                try {


                    String value = result.toString();
                    JSONObject mySO = (JSONObject) result;
                    org.json.JSONArray array = mySO
                            .getJSONArray("MeidicalReportInquiry");
                    if (array.getJSONObject(0).has("MessageCode")) {
                        layout_none.setVisibility(View.VISIBLE);
                    } else {
                        layout_none.setVisibility(View.GONE);
                        if (mList != null && mList.size() > 0)
                            mList.clear();
                        MedicalDate date = JsonTools.getData(result.toString(), MedicalDate.class);
                        mList = date.getData();
                        if (adapter == null) {
                            adapter = new MedicalExaminationAdapter(ExaminationReportActivity.this, mList);
                            listView.setAdapter(adapter);
                        } else {
                            adapter.setmList(mList);
                            listView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    if(isRefresh){
                        refreshLayout.refreshFinish(RefreshLayout.SUCCEED);
                        isRefresh=false;
                    }
                }

            }

        }));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!isNetWork){
                    showNetWorkErrorDialog();
                    return;
                }
                Intent i = new Intent(ExaminationReportActivity.this, MedicalExaminationDesActivity.class);
                i.putExtra("ID", mList.get(position).getID());
                i.putExtra("time", mList.get(position).getCheckTime());
              startActivity(i);
            }
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == ADD&& data != null) {
            isRefresh=true;
            initDatas();
        }
    }

}
