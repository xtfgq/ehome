package com.zzu.ehome.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import com.zzu.ehome.R;
import com.zzu.ehome.bean.RepatBean;
import java.util.List;

/**
 * Created by Administrator on 2016/9/18.
 */
public class RepeatAdapter extends BaseAdapter {
    private Context mContext;
    private List<RepatBean> mList;
    private LayoutInflater mInflater;

    public RepeatAdapter(Context mContext, List<RepatBean> mList) {
        this.mContext = mContext;
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
        RepatBean item = mList.get(position);
        TextView tv_time = (TextView) v.findViewById(R.id.tv_time);
        CheckBox cb = (CheckBox) v.findViewById(R.id.radioButton);
        cb.setChecked(item.getSelct());
        tv_time.setText(item.getNaem());
        return v;
    }
}
