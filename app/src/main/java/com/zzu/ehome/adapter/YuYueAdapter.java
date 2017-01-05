package com.zzu.ehome.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.activity.OfficeListActivity;
import com.zzu.ehome.activity.SupperBaseActivity;
import com.zzu.ehome.bean.HospitalBean;

import java.util.List;

/**
 * Created by Mersens on 2016/8/9.
 */
public class YuYueAdapter extends BaseListAdapter<HospitalBean> {
    private List<HospitalBean> mList;
    private Context mContext;
    private Activity activity;


    public YuYueAdapter(Context context, List<HospitalBean> objects) {
        super(context, objects);
        this.mList = objects;
        this.mContext = context;
        this.activity=(SupperBaseActivity)context;
    }

    @Override
    public View getGqView(int position, View convertView, ViewGroup parent) {
        View mView = getInflater().inflate(R.layout.item_yygh, null);
        TextView name = (TextView) mView.findViewById(R.id.hosptial_name);
        name.setText(mList.get(position).getHospital_FullName());


        return mView;
    }
}
