package com.zzu.ehome.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zzu.ehome.R;

import com.zzu.ehome.application.Constants;
import com.zzu.ehome.bean.StreamInfo;
import com.zzu.ehome.view.GlideRoundTransform;

import java.util.List;

import static com.zzu.ehome.db.DBHelper.mContext;

/**
 * Created by guoqiang on 2017/3/31.
 */

public class MessageItemAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context context;

    public List<StreamInfo> getList() {
        return list;
    }

    public void setList(List<StreamInfo> list) {
        this.list = list;
    }

    private List<StreamInfo> list;

    public MessageItemAdapter(Context mcontext) {
        this.context = mcontext;
        this.list=list;
        mInflater = LayoutInflater.from(mcontext);
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }


    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StreamInfo  item = list.get(position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.home_item, null);
            holder = new ViewHolder();
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            holder.iv_head = (ImageView) convertView.findViewById(R.id.iv_head);
            holder.tvtime=(TextView)convertView.findViewById(R.id.timetv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Glide.with(context)
                .load(item.getImgUrl())
                .centerCrop().error(R.drawable.pic_defaultads).transform(new GlideRoundTransform(mContext, 5))
                .into(holder.iv_head);

        holder.tv_title.setText(item.getStream_name());
        holder.tv_content.setText(item.getServer_ip());
        return convertView;
    }

    public static class ViewHolder {
        private TextView tv_title;
        private TextView tv_content;
        private ImageView iv_head;
        private TextView tvtime;

    }
}
