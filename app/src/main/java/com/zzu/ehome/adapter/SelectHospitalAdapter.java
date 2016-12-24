package com.zzu.ehome.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.bean.HospitalBean;

import java.util.List;

/**
 * Created by zzu on 2016/4/13.
 */
public class SelectHospitalAdapter extends BaseListAdapter<HospitalBean> {
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    private int index=0;
    private List<HospitalBean> list;

    public SelectHospitalAdapter(Context context, List<HospitalBean> objects) {
        super(context, objects);
        this.list = objects;
    }

    @Override
    public View getGqView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = getInflater().inflate(R.layout.listview_select_hospital_item, null);
            holder.hosptial_name = (TextView) convertView.findViewById(R.id.hosptial_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index=position;
                notifyDataSetChanged();
            }
        });
        if(position==index){
            convertView.setBackgroundColor(Color.parseColor("#e0e0e0"));
        }else{
            convertView.setBackgroundColor(Color.WHITE);
        }
        holder.hosptial_name.setText(list.get(position).getHospital_FullName());
        return convertView;
    }

    public static class ViewHolder {
        private TextView hosptial_name;
    }
}
