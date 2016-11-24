package com.zzu.ehome.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.bean.UserContactor;

import java.util.List;

/**
 * Created by Mersens on 2016/8/9.
 */
public class SelectPatientAdapter extends BaseListAdapter<UserContactor> {
    private List<UserContactor> mList;
    public SelectPatientAdapter(Context context, List<UserContactor> objects) {
        super(context, objects);
        mList=objects;
    }

    @Override
    public View getGqView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh1 = null;
        UserContactor item=(UserContactor)getItem(position);
        if (convertView == null) {

            convertView = getInflater().inflate(R.layout.item_contact, parent, false);
            vh1 = new ViewHolder();
            vh1.tvName=(TextView) convertView
                    .findViewById(R.id.tv_name);
            convertView.setTag(vh1);
        }else{
            vh1 = (ViewHolder) convertView.getTag();
        }
        vh1.tvName.setText(item.getUserName());

        return convertView;
    }
    class ViewHolder {
        TextView tvName;
    }

}
