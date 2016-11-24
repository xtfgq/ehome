package com.zzu.ehome.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zzu.ehome.R;
import com.zzu.ehome.application.Constants;
import com.zzu.ehome.bean.DepartmentBean;
import com.zzu.ehome.bean.OrderInquiryByTopmd;
import com.zzu.ehome.utils.DateUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/9/21.
 */
public class DepartmentBeanAdapter extends BaseListAdapter<DepartmentBean> {
    private List<DepartmentBean> mList;
    private Context mContext;

    public DepartmentBeanAdapter(Context context, List<DepartmentBean> objects) {
        super(context, objects);
        this.mList = objects;
        this.mContext = context;
    }

    @Override
    public View getGqView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = getInflater().inflate(R.layout.office_list_item, null);
            holder.name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.ivright = (ImageView) convertView.findViewById(R.id.icon_lift);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(mList.get(position).getDepartment_FullName());

        return convertView;
    }

    public static class ViewHolder {

        private TextView name;

        private ImageView ivright;

    }
}
