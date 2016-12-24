package com.zzu.ehome.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.bean.MedicationRecord;
import com.zzu.ehome.bean.MessageBean;

import java.util.List;

import static com.zzu.ehome.R.id.tv_dot;

/**
 * Created by Administrator on 2016/9/24.
 */
public class MessageAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context context;

    public List<MessageBean> getList() {
        return list;
    }

    public void setList(List<MessageBean> list) {
        this.list = list;
    }

    private List<MessageBean> list;

    public MessageAdapter(Context mcontext,List<MessageBean> list) {
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
        MessageBean  item = list.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.message_item, null);
            holder.name=(TextView) convertView.findViewById(R.id.tv_name);
            holder.tvtime=(TextView)convertView.findViewById(R.id.tvtime);
            holder.tv_dot=(TextView)convertView.findViewById(R.id.tv_dot);
            holder.tv_tips=(TextView)convertView.findViewById(R.id.tv_tips);
            holder.ivmessage=(ImageView)convertView.findViewById(R.id.ivmessage);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(position==0){
            holder.tv_tips.setText(item.getTips());
            holder.ivmessage.setBackgroundResource(R.drawable.icon_sys_mes);
        }else{
            holder.tv_tips.setText("在线问诊记录");
            holder.ivmessage.setBackgroundResource(R.mipmap.message);
        }
        holder.name.setText(item.getContent());
        if(item.getNum()>0) {
            holder.tv_dot.setVisibility(View.VISIBLE);
            holder.tv_dot.setText(item.getNum()+"");
        }else{
            holder.tv_dot.setVisibility(View.GONE);
            holder.tv_dot.setText("");
        }


        return convertView;
    }

    public static class ViewHolder {
        TextView name;
        TextView tvtime;
        ImageView ivmessage;
        TextView tv_dot;
        TextView tv_tips;

    }
}
