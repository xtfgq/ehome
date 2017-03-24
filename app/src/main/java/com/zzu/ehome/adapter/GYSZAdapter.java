package com.zzu.ehome.adapter;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emilsjolander.components.stickylistheaders.StickyListHeadersAdapter;
import com.zzu.ehome.R;
import com.zzu.ehome.bean.UricAcidBean;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.DateUtils;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Administrator on 2016/5/11.
 */
public class GYSZAdapter extends BaseAdapter implements StickyListHeadersAdapter {
    public List<UricAcidBean> getList() {
        return list;
    }

    public void setList(List<UricAcidBean> list) {
        this.list = list;
    }

    List<UricAcidBean> list;
    private Context mContext;
    private LayoutInflater mInflater;


    public GYSZAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);

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
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        UricAcidBean res = (UricAcidBean) getItem(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_health_bloodpress2,
                    parent, false);
            holder = new ViewHolder();
            holder.tv_ssy = (TextView) convertView
                    .findViewById(R.id.tv_ssy_msg);

            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tvmb = (TextView) convertView.findViewById(R.id.tv_mb_msg);
            holder.tv_status = (TextView) convertView
                    .findViewById(R.id.tv_xy_msg);
            holder.rl_status = (RelativeLayout) convertView.findViewById(R.id.rl_status);
            holder.tv_ssy_come=(TextView)convertView.findViewById(R.id.tv_ssy_come);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        double n=Double.valueOf(res.getItemValue());
        String mb = "甘油三酯: " + decimalFormat.format(n)+" mmol/L";
        SpannableString style1 = new SpannableString(mb);
        style1.setSpan(
                new TextAppearanceSpan(mContext, R.style.styleNormalColor), 5, mb.length()-6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.tv_ssy.setText(style1);
        if(Double.compare(n,0.56d)<0){
            holder.tv_status.setText("偏低");
            holder.rl_status.setBackgroundResource(R.drawable.btn_yuyue);

        }else if(Double.compare(n,1.7d)>0){
            holder.tv_status.setText("偏高");
            holder.rl_status.setBackgroundResource(R.drawable.btn_yuyue_2);
        }else{
            holder.tv_status.setText("正常");
            holder.rl_status.setBackgroundResource(R.drawable.btn_yuyue_4);
        }
        String ss3="来源："+CommonUtils.showFromto(res.getFromTo());
        SpannableString style3 = new SpannableString(ss3);
        style3.setSpan(
                new TextAppearanceSpan(mContext, R.style.styleNormalColor), 3, ss3.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        holder.tv_ssy_come.setText(style3);
        holder.tv_time.setText(DateUtils.StringPattern(res.getMonitorTime(), "yyyy/MM/dd HH:mm:ss", "dd日 HH:mm"));
        return convertView;

    }

    public static class ViewHolder {

        public TextView tv_ssy;
        public RelativeLayout rl_status;
        public TextView tvmb;
        public TextView tv_time;
        public TextView tv_status;
        public TextView tv_ssy_come;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        UricAcidBean res = (UricAcidBean) getItem(position);
        View headView = mInflater.inflate(R.layout.item_chat_header,
                parent, false);
        TextView tvheader = (TextView) headView.findViewById(R.id.tv_time);
        String date = DateUtils.StringPattern(res.getMonitorTime(), "yyyy/MM/dd HH:mm:ss", "M月yyyy年");
        int bstart = date.indexOf("月");
        SpannableString style = new SpannableString(date);
        style.setSpan(
                new TextAppearanceSpan(mContext, R.style.styleItemColor), 0, bstart, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvheader.setText(style);
        return headView;
    }

    /**
     * 决定header出现的时机，如果当前的headerid和前一个headerid不同时，就会显示
     */
    @Override
    public long getHeaderId(int position) {

        UricAcidBean res = (UricAcidBean) getItem(position);
        return CommonUtils.returnLongNew(DateUtils.StringPattern(res.getMonitorTime(), "yyyy/MM/dd HH:mm:ss", "yyyy-MM"));

    }
}
