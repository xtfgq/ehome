package com.zzu.ehome.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.activity.ImageECGDetail;
import com.zzu.ehome.activity.SelectItemActivity;
import com.zzu.ehome.application.Constants;
import com.zzu.ehome.bean.HospitalBean;
import com.zzu.ehome.bean.OcrTypeBean;

import java.util.List;

import static android.R.attr.listSelector;

/**
 * Created by Administrator on 2016/11/15.
 */

public class SelectItemAdapter extends BaseListAdapter<OcrTypeBean> {
    private List<OcrTypeBean> list;
    private Context mContext;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    private int index=-1;

    public SelectItemAdapter(Context context, List<OcrTypeBean> objects) {
        super(context, objects);
        this.list = objects;
        this.mContext=context;
    }

    @Override
    public View getGqView(final int position, View convertView, ViewGroup parent) {
       ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = getInflater().inflate(R.layout.item_ocr, null);
            holder.hosptial_name = (TextView) convertView.findViewById(R.id.hosptial_name);
            holder.tv_des=(TextView) convertView.findViewById(R.id.view_model);
            holder.rlclick=(RelativeLayout)convertView.findViewById(R.id.rlclick);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.hosptial_name.setText(list.get(position).getValue());
        holder.rlclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setBackgroundColor(Color.parseColor("#e0e0e0"));
                index=position;
                notifyDataSetChanged();
            }
        });
        holder.tv_des.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, ImageECGDetail.class);
                i.putExtra("imurl", Constants.EhomeURL+list.get(position).getDescription().replace("~", "").replace("\\", "/"));
                mContext.startActivity(i);
            }
        });
        if(position==index){
            holder.rlclick.setBackgroundColor(Color.parseColor("#e0e0e0"));
        }else{
            holder.rlclick.setBackgroundColor(Color.WHITE);
        }
        return convertView;
    }

    public static class ViewHolder {
        private TextView hosptial_name;
        private TextView tv_des;
        private RelativeLayout rlclick;
    }
}
