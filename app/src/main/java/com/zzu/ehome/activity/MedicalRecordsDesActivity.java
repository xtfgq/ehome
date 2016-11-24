package com.zzu.ehome.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zzu.ehome.R;
import com.zzu.ehome.application.Constants;
import com.zzu.ehome.bean.DoctorSchemaByTopmdBean;
import com.zzu.ehome.bean.TreatmentInquirywWithPage;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.view.HeadView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/6.
 */
public class MedicalRecordsDesActivity extends BaseActivity{
    private Intent mIntent;
    private TextView tv_name;
    private TextView edt_time;
    private TextView edt_jzjg;
    private TextView tvnumber;
    private TextView tvyyjl;
    private TextView edt_yyjy;
    private TreatmentInquirywWithPage records;
    private GridAdapter mAdapter;
    private RecyclerView resultRecyclerView;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.acitivty_medical_records_des);
        initViews();
        initEvent();
        initDatas();
    }
    public void initViews() {
        tv_name=(TextView)findViewById(R.id.tv_name);
        edt_time=(TextView)findViewById(R.id.edt_time);
        edt_jzjg=(TextView)findViewById(R.id.edt_jzjg);
        edt_yyjy=(TextView)findViewById(R.id.edt_yyjy);
        resultRecyclerView = (RecyclerView) findViewById(R.id.result_recycler);
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "就诊记录", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {

                finishActivity();

            }
        });
    }
    public void initEvent(){
        mIntent=this.getIntent();
        records=(TreatmentInquirywWithPage)mIntent.getSerializableExtra("MedicalRecords");
        tv_name.setText(records.getHosname());
        edt_time.setText(records.getTime().split(" ")[0]);
        edt_jzjg.setText(records.getZhenduan().replace("&lt;", "<").replace("&gt;", ">").replace("&amp;", "&"));
        edt_yyjy.setText(records.getOpinion().replace("&lt;", "<").replace("&gt;", ">").replace("&amp;", "&"));

        String imgstr = records.getImages();
        List<String> mList = new ArrayList<String>();
        if (imgstr.indexOf(",") >= 0) {
            String[] strs = imgstr.split("\\,");
            for (int m = 0; m < strs.length; m++) {
                String imgurl = Constants.EhomeURL + strs[m].replace("~", "").replace("\\", "/");
                mList.add(imgurl);
            }
        } else {
            if (!TextUtils.isEmpty(imgstr)) {
                String imgurl2 = Constants.EhomeURL + imgstr.replace("~", "").replace("\\", "/");
                mList.add(imgurl2);
            }

        }
        mAdapter = new GridAdapter(mList, MedicalRecordsDesActivity.this);
        resultRecyclerView.setLayoutManager(new GridLayoutManager(MedicalRecordsDesActivity.this, 3));
        resultRecyclerView.setAdapter(mAdapter);

    }
    public void initDatas(){

    }
    private class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {
        private Context mContext;
//        private ImageLoader imageLoader;

        public List<String> getmList() {
            return mList;
        }

        public void setmList(List<String> mList) {
            this.mList = mList;
        }

        private List<String> mList;

        public GridAdapter(List<String> list, Context context) {
            super();
            this.mList = list;
            this.mContext = context;
//            imageLoader = ImageLoader.getInstance();
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_result_report, parent, false);

            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            Glide.with(MedicalRecordsDesActivity.this)
                    .load(mList.get(position))
                    .centerCrop()
                    .into(holder.imageView);
//            imageLoader.displayImage(mList.get(position),holder.imageView);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ImageAlbumManager.class);
                    intent.putStringArrayListExtra("imgs", (ArrayList<String>) mList);
                    intent.putExtra("position", position);
                    intent.putExtra("type", 1);
                    mContext.startActivity(intent);
                }
            });


        }

        @Override
        public int getItemCount() {
            return mList == null ? 0 : mList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;

            public ViewHolder(View itemView) {
                super(itemView);
                imageView = (ImageView) itemView.findViewById(R.id.image);

            }
        }
    }

}
