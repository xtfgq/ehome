package com.zzu.ehome.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.zzu.ehome.main.ehome.MainActivity;
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
    private RecyclerView gridView;

    private RelativeLayout layout_tel;
    private TextView tv_tel;
    private ScrollView scrollView;
    private String id = null;
    private RequestMaker requestMaker;
    private ImageView img_logo_shop;
    private TextView tv_mame, tv_address, tv_yb, tv_zk;
    private List<String> mList = new ArrayList<>();
    private MyAdapter adapter = null;
    private RelativeLayout rllocation, rl_hui, rl_youhui;
    private View viewline;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_cooperation_pharmacy);
        id = getIntent().getStringExtra(CooperationPharmacyFragment.ID);
        initViews();
        initEvent();
        initDatas();
        if (!CommonUtils.isNotificationEnabled(CooperationPharmacyActivity.this)) {
            showTitleDialog("请打开通知中心");
        }
    }


    public void initViews() {
        adapter = new MyAdapter(CooperationPharmacyActivity.this, mList);
        requestMaker = RequestMaker.getInstance();
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        scrollView.fullScroll(ScrollView.FOCUS_UP);
        gridView = (RecyclerView) findViewById(R.id.gridView);
        GridLayoutManager mgr = new GridLayoutManager(CooperationPharmacyActivity.this, 2);
        gridView.setLayoutManager(mgr);
        gridView.setAdapter(adapter);
        layout_tel = (RelativeLayout) findViewById(R.id.layout_tel);
        tv_tel = (TextView) findViewById(R.id.tv_tel);
        img_logo_shop = (ImageView) findViewById(R.id.img_logo_shop);
        tv_mame = (TextView) findViewById(R.id.tv_mame);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_yb = (TextView) findViewById(R.id.tv_yb);
        tv_zk = (TextView) findViewById(R.id.tv_zk);
        rllocation = (RelativeLayout) findViewById(R.id.rllocation);
        rl_hui = (RelativeLayout) findViewById(R.id.rl_hui);
        rl_youhui = (RelativeLayout) findViewById(R.id.rl_youhui);
        viewline = (View) findViewById(R.id.line);
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "合作药店", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        });
    }

    public void initEvent() {

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


    public void doTel(final String tel) {
        DialogTips dialog = new DialogTips(this, "", tel,
                "拨打", true, true);
        dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int userId) {
                callPhone(tel);
            }
        });

        dialog.show();
        dialog = null;

    }

    private void callPhone(String tel) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (CooperationPharmacyActivity.this.checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                Intent intentTel = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tel));
                startActivity(intentTel);
            }
        } else {
            Intent intentTel = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tel));
            startActivity(intentTel);
        }


    }

    public void setDatas(final PharmacyBean pb) {
        tv_tel.setText(pb.getPharmacyPhone());
        Glide.with(CooperationPharmacyActivity.this).load(Constants.EhomeURL + pb.getPicURL().
                replace("~", "").replace("\\", "/")).
                into(img_logo_shop);
        tv_mame.setText(pb.getPharmacyName());
        tv_address.setText(pb.getPharmacyAddress());
        rllocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final double mLatitude = Double.valueOf(pb.getLatitude());
                final double mLongitude = Double.valueOf(pb.getLongitude());
                final String name = pb.getPharmacyName();
                DialogTips dialog = new DialogTips(CooperationPharmacyActivity.this, "", name,
                        "到这里去", true, true);
                dialog.setCanceledOnTouchOutside(true);
                dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int userId) {
                        Uri mUri = Uri.parse("geo:" + mLatitude + "," + mLongitude + "?" + "q=" +
                                name);
                        Intent mIntent = new Intent(Intent.ACTION_VIEW, mUri);
                        startActivity(mIntent);
                    }
                });

                dialog.show();
                dialog = null;
            }
        });
        String zk = pb.getZhekou();
        if (TextUtils.isEmpty(zk)) {
            tv_zk.setText("暂无折扣");
        } else {
            tv_zk.setText(zk + "折");
        }

        String yb = pb.getYibaoType();
        if (TextUtils.isEmpty(yb)) {
            tv_yb.setVisibility(View.GONE);
        } else{
            tv_yb.setText(yb);
        }
        if (!TextUtils.isEmpty(pb.getPharmacyPicUrl())) {
            String strs[] = pb.getPharmacyPicUrl().split(",");
            for (String s : strs) {
                String imgurl = Constants.EhomeURL + s.replace("~", "").replace("\\", "/");
                mList.add(imgurl);
            }
            adapter.setList(mList);
            adapter. notifyDataSetChanged();

        } else {
            viewline.setVisibility(View.GONE);
            rl_youhui.setVisibility(View.GONE);
        }
        scrollView.smoothScrollTo(0,0);
    }


    public void initDatas() {
        startProgressDialog();
        requestMaker.PharmacyDetailInquiry(id, new JsonAsyncTask_Info(
                this, true, new JsonAsyncTaskOnComplete() {
            public void processJsonObject(Object result) {
                JSONObject mySO = (JSONObject) result;
                stopProgressDialog();
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
                stopProgressDialog();
            }
        }));
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private List<String> list;
        private Context mContext;
        private LayoutInflater inf;

        public MyAdapter(Context context, List<String> list) {
            this.mContext = context;
            this.list = list;
            this.inf = LayoutInflater.from(context);
        }


        public void setList(List<String> list) {
            this.list = list;

        }


        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = inf.inflate(R.layout.gridview_item, parent, false);
            return new ViewHolder(v);
        }


        @Override
        public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
            Glide.with(mContext).load(list.get(position)).into(holder.imageView);


        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return list == null ? 0 : list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            ImageView imageView;

            //在布局中找到所含有的UI组件
            public ViewHolder(View itemView) {
                super(itemView);
                imageView = (ImageView) itemView.findViewById(R.id.imageView);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, ImageAlbumManager.class);
                        intent.putStringArrayListExtra("imgs", (ArrayList<String>) list);
                        intent.putExtra("position", getPosition());
                        intent.putExtra("type", 1);
                        mContext.startActivity(intent);
                    }
                });
            }
        }

    }


}
