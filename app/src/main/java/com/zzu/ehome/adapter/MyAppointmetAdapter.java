package com.zzu.ehome.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zzu.ehome.R;
import com.zzu.ehome.activity.DoctorDetialActivity;
import com.zzu.ehome.activity.ECGDetailsActivity;
import com.zzu.ehome.application.Constants;
import com.zzu.ehome.bean.ECGDynamicBean;
import com.zzu.ehome.bean.OrderInquiryByTopmd;
import com.zzu.ehome.bean.TreatmentSearch;
import com.zzu.ehome.utils.DateUtils;

import java.util.List;

import static com.zzu.ehome.R.id.holder;

/**
 * Created by Administrator on 2016/9/13.
 */
public class MyAppointmetAdapter extends BaseListAdapter<OrderInquiryByTopmd> {
    private List<OrderInquiryByTopmd> mList;
    private Context mContext;

    public MyAppointmetAdapter(Context context, List<OrderInquiryByTopmd> objects) {
        super(context, objects);
        this.mList = objects;
        this.mContext = context;
    }
    @Override
    public int getViewTypeCount() {
        // menu type count
        return 2;
    }
    @Override
    public int getItemViewType(int position) {
        // current menu type

     return mList.get(position).getType();


    }

    @Override
    public View getGqView(int position, View convertView, ViewGroup parent) {
        ViewHolder0 holder0 = null;
        ViewHolder1 holder1 = null;
        int type = getItemViewType(position);
        if (convertView == null) {
            switch (type) {
                case 0:
                    holder0 = new ViewHolder0();
                    convertView = getInflater().inflate(R.layout.appointment_item, null);
                    holder0.tvtitle = (TextView) convertView.findViewById(R.id.tvtitle);
                    holder0.name = (TextView) convertView.findViewById(R.id.tvname);
                    holder0.tvtime = (TextView) convertView.findViewById(R.id.tvtime);
                    holder0.ivhead = (ImageView) convertView.findViewById(R.id.ivhead);
                    holder0.ivstatus = (ImageView) convertView.findViewById(R.id.iv_status);
                    convertView.setTag(holder0);
                    break;
                case 1:
                    holder1 = new ViewHolder1();
                    convertView = getInflater().inflate(R.layout.appointment_item, null);
                    holder1.tvtitle = (TextView) convertView.findViewById(R.id.tvtitle);
                    holder1.name = (TextView) convertView.findViewById(R.id.tvname);
                    holder1.tvtime = (TextView) convertView.findViewById(R.id.tvtime);
                    holder1.ivhead = (ImageView) convertView.findViewById(R.id.ivhead);
                    holder1.ivstatus = (ImageView) convertView.findViewById(R.id.iv_status);
                    convertView.setTag(holder1);
                    break;



        }
    }else{
            switch (type){
                case 0:
                    holder0 = (ViewHolder0) convertView.getTag();
                    break;
                case 1:
                    holder1= (ViewHolder1) convertView.getTag();
                    break;

            }

    }
        switch (type) {
            case 0:
                OrderInquiryByTopmd item = getItem(position);
                holder0.name.setText(item.getDoctorName());
                holder0.tvtitle.setText(item.getHospitalName() + "  " + item.getDepartmentName());
                if (TextUtils.isEmpty(item.getPictureURL())||item.getPictureURL().contains("vine")) {
                    holder0.ivhead.setBackgroundResource(R.drawable.icon_doctor);
                } else {

                    Glide.with(mContext)
                            .load(Constants.JE_BASE_URL3 + item.getPictureURL().replace("~", "").replace("\\", "/"))
                            .centerCrop().error(R.drawable.icon_doctor)
                            .into(holder0.ivhead);
                }
                holder0.ivstatus.setVisibility(View.GONE);
                if(TextUtils.isEmpty(item.getBeginTime())){
                    holder0.tvtime.setText("预约时间：" + DateUtils.StringPattern(item.getGoTime(), "yyyy/MM/dd HH:mm:ss", "yyyy/M/dd") + "  " + item.getSchemaWeek() );
                }else if(TextUtils.isEmpty(item.getEndTime())){
                    holder0.tvtime.setText("预约时间：" + DateUtils.StringPattern(item.getGoTime(), "yyyy/MM/dd HH:mm:ss", "yyyy/M/dd") + "  " + item.getSchemaWeek() + "  " + item.getBeginTime());
                }else {

                    holder0.tvtime.setText("预约时间：" + DateUtils.StringPattern(item.getGoTime(), "yyyy/MM/dd HH:mm:ss", "yyyy/M/dd") + "  " + item.getSchemaWeek() + "  " + item.getBeginTime() + "-" + item.getEndTime());
                }
                break;
            case 1:
                OrderInquiryByTopmd item1 = getItem(position);
                holder1.name.setText(item1.getDoctorName());
                holder1.tvtitle.setText(item1.getHospitalName() + "  " + item1.getDepartmentName());
                if (TextUtils.isEmpty(item1.getPictureURL())||item1.getPictureURL().contains("vine")) {
                    holder1.ivhead.setBackgroundResource(R.drawable.icon_doctor);
                } else {



                    Glide.with(mContext)
                            .load(Constants.JE_BASE_URL3 + item1.getPictureURL().replace("~", "").replace("\\", "/"))
                            .centerCrop().error(R.drawable.icon_doctor)
                            .into(holder1.ivhead);
                }
                holder1.ivstatus.setVisibility(View.VISIBLE);
                if(TextUtils.isEmpty(item1.getBeginTime())){
                    holder1.tvtime.setText("预约时间：" + DateUtils.StringPattern(item1.getGoTime(), "yyyy/MM/dd HH:mm:ss", "yyyy/M/dd") + "  " + item1.getSchemaWeek());
                }else if(TextUtils.isEmpty(item1.getEndTime())){
                    holder1.tvtime.setText("预约时间：" + DateUtils.StringPattern(item1.getGoTime(), "yyyy/MM/dd HH:mm:ss", "yyyy/M/dd") + "  " + item1.getSchemaWeek() + "  " + item1.getBeginTime()) ;
                }else {
                    holder1.tvtime.setText("预约时间：" + DateUtils.StringPattern(item1.getGoTime(), "yyyy/MM/dd HH:mm:ss", "yyyy/M/dd") + "  " + item1.getSchemaWeek() + "  " + item1.getBeginTime() + "-" + item1.getEndTime());
                }
                    break;
        }

        return convertView;
    }

    public static class ViewHolder0 {
        private TextView tvtitle;
        private TextView name;
        private TextView tvtime;
        private ImageView ivhead;
        private ImageView ivstatus;
    }
    public static class ViewHolder1{
        private TextView tvtitle;
        private TextView name;
        private TextView tvtime;
        private ImageView ivhead;
        private ImageView ivstatus;
    }
}
