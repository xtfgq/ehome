package com.zzu.ehome.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.activity.OfficeListActivity;
import com.zzu.ehome.bean.HospitalBean;

import java.util.List;

/**
 * Created by Administrator on 2016/12/6.
 */

public class ExaminationTestAadpter extends BaseListAdapter<String> {
    private List<String> mList;
    private Context mContext;


    public ExaminationTestAadpter(Context context, List<String> objects) {
        super(context, objects);
        this.mList = objects;
        this.mContext = context;
    }

    @Override
    public View getGqView(int position, View convertView, ViewGroup parent) {
        View mView = getInflater().inflate(R.layout.item_string, null);
        TextView name = (TextView) mView.findViewById(R.id.hosptial_name);
        name.setText(mList.get(position));

        return mView;
    }
}
