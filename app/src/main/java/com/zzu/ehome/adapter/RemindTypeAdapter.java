package com.zzu.ehome.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.bean.RepatBean;

import java.util.List;

/**
 * Created by Administrator on 2016/9/18.
 */
public class RemindTypeAdapter extends BaseAdapter{
    private Context mContext;
    private List<RepatBean> mList;
    private LayoutInflater mInflater;


    public RemindTypeAdapter(Context mContext, List<RepatBean> mList) {
        this.mContext=mContext;
        this.mList = mList;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = mInflater.inflate(R.layout.repeat_item, null);
      RepatBean item=mList.get(position);
        TextView tv_time = (TextView) v.findViewById(R.id.tv_time);
        CheckBox cb=(CheckBox)v.findViewById(R.id.radioButton);

        if(item.getSelct()){
            cb.setChecked(true);
        }else{
            cb.setChecked(false);
        }

        tv_time.setText(item.getNaem());
        return v;
    }
}
