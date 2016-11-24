package com.zzu.ehome.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zzu.ehome.R;

import com.zzu.ehome.bean.TreatmentInquirywWithPage;


import java.util.List;

/**
 * Created by Administrator on 2016/9/6.
 */
public class MedicalRecordsAdapter extends BaseAdapter{
    private Context mContext;
    private LayoutInflater mInflater;

    public List<TreatmentInquirywWithPage> getmList() {
        return mList;
    }

    public void setmList(List<TreatmentInquirywWithPage> mList) {
        this.mList = mList;
    }

    private List<TreatmentInquirywWithPage> mList;
    public MedicalRecordsAdapter(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        TreatmentInquirywWithPage itme = (TreatmentInquirywWithPage) getItem(position);
        if(convertView==null){
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_medicalrecords_layout, null);
            holder.name=(TextView) convertView.findViewById(R.id.tv_name);
            holder.tvtime=(TextView)convertView.findViewById(R.id.tv_time);
            holder.tvhop=(TextView)convertView.findViewById(R.id.tv_hop);
            holder.ivright=(ImageView)convertView.findViewById(R.id.iv_right);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(itme.getHosname());
        if(itme.getHosname().equals("郑州大学第五附属医院")){
            holder.tvhop.setVisibility(View.VISIBLE);
        }else{
            holder.tvhop.setVisibility(View.GONE);
        }
        holder.tvtime.setText(itme.getTime().split(" ")[0]);
        return convertView;
    }
    public class ViewHolder {
        TextView name;
        TextView tvtime;
        TextView tvhop;
        ImageView ivright;

    }

}
