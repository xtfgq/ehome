package com.zzu.ehome.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zzu.ehome.R;
import com.zzu.ehome.application.Constants;
import com.zzu.ehome.bean.MedicationRecord;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.view.HeadView;

import java.util.ArrayList;
import java.util.List;

import static com.zzu.ehome.R.id.scrollView2;
import static com.zzu.ehome.R.id.textView;
import static com.zzu.ehome.R.id.tv;
import static com.zzu.ehome.R.id.view;

/**
 * Created by Administrator on 2016/9/8.
 */
public class YYJLDesActivity extends BaseActivity {
    private Intent mIntent;
    private TextView tv_name;
    private TextView edt_time;
    private TextView edt_jzjg;
    private TextView tvnumber;
    private TextView editText_bz;
    private TextView tvwordnumber;
    private RecyclerView resultRecyclerView;
    private MedicationRecord record;
    private GridAdapter mAdapter;
    private ScrollView scrollViewDes;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.actvity_yyjl_des);
        mIntent = this.getIntent();
        initViews();

        initEvent();
        if (!CommonUtils.isNotificationEnabled(YYJLDesActivity.this)) {
            showTitleDialog("请打开通知中心");
        }
    }

    private void initViews() {
        setLeftWithTitleViewMethod(R.mipmap.icon_arrow_left, "用药记录", new HeadView.OnLeftClickListener() {
            @Override
            public void onClick() {
                finishActivity();
            }
        });
        tv_name = (TextView) findViewById(R.id.tv_name);
        edt_time = (TextView) findViewById(R.id.edt_time);
        tvnumber = (TextView) findViewById(R.id.tvnum);
        editText_bz = (TextView) findViewById(R.id.editText_bz);
        tvwordnumber = (TextView) findViewById(R.id.tvwordnumber);
        scrollViewDes=(ScrollView)findViewById(R.id.scrollView2);
        record = (MedicationRecord) mIntent.getSerializableExtra("YYJLRecords");
        resultRecyclerView = (RecyclerView) findViewById(R.id.result_recycler);


    }

    private void initEvent() {
        tv_name.setText(record.getDrugName().trim());
        tvnumber.setText(record.getNumber().trim() + "片/次");
        edt_time.setText((record.getMedicationTime()).split(" ")[0]);
        editText_bz.setText(record.getRemarks().replace("&lt;", "<").replace("&gt;", ">").replace("&amp;", "&").replace("(null)",""));
        editText_bz.setMovementMethod(ScrollingMovementMethod.getInstance());
        editText_bz.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    scrollViewDes.requestDisallowInterceptTouchEvent(false);
                }else{
                    scrollViewDes.requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });
        String imgstr = record.getDrugImage();
        List<String> mList = new ArrayList<String>();
        if (imgstr.indexOf(",") >= 0) {
            String[] strs = imgstr.split("\\,");
            for (int m = 0; m < strs.length; m++) {
                String imgurl = Constants.PlatformURL + strs[m].replace("~", "").replace("\\", "/");
                mList.add(imgurl);
            }
        } else {
            if (!TextUtils.isEmpty(imgstr)) {
                String imgurl2 = Constants.PlatformURL + imgstr.replace("~", "").replace("\\", "/");
                mList.add(imgurl2);
            }

        }
        mAdapter = new GridAdapter(mList, YYJLDesActivity.this);
        resultRecyclerView.setLayoutManager(new GridLayoutManager(YYJLDesActivity.this, 3));
        resultRecyclerView.setAdapter(mAdapter);
    }



    private class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {
        private Context mContext;
        private ImageLoader imageLoader;

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
            imageLoader = ImageLoader.getInstance();
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_result_report, parent, false);

            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            Glide.with(YYJLDesActivity.this)
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
