package com.zzu.ehome.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.bean.CapaingBean;
import com.zzu.ehome.bean.TreatmentInquirywWithPage;
import com.zzu.ehome.utils.DateUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/9/24.
 */
public class CapaingAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;

    public List<CapaingBean> getmList() {
        return mList;
    }

    public void setmList(List<CapaingBean> mList) {
        this.mList = mList;
    }

    private List<CapaingBean> mList;

    public CapaingAdapter(Context context) {
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
        CapaingBean itme = (CapaingBean) getItem(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.capaing_list_item, null);
            holder.name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tvtime = (TextView) convertView.findViewById(R.id.tvtime);
            holder.tvcontent=(TextView)convertView.findViewById(R.id.tvcontent);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(itme.getName());
        holder.tvtime.setText(DateUtils.StringPattern(itme.getCreateDate(),"yyyy-MM-dd HH:mm:ss","yyyy/MM/dd HH:mm"));

        return convertView;
    }

    public class ViewHolder {
        TextView name;
        TextView tvtime;
        TextView tvcontent;


    }
}
