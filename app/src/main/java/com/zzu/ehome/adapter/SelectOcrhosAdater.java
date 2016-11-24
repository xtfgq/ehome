package com.zzu.ehome.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.bean.HospitalBean;

import java.util.List;

/**
 * Created by Administrator on 2016/11/15.
 */

public class SelectOcrhosAdater extends BaseListAdapter<HospitalBean> {
    private List<HospitalBean> list;

    public SelectOcrhosAdater(Context context, List<HospitalBean> objects) {
        super(context, objects);
        this.list = objects;
    }

    @Override
    public View getGqView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = getInflater().inflate(R.layout.item_yygh, null);
            holder.hosptial_name = (TextView) convertView.findViewById(R.id.hosptial_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.hosptial_name.setText(list.get(position).getHospital_FullName());
        return convertView;
    }

    public static class ViewHolder {
        private TextView hosptial_name;
    }
}