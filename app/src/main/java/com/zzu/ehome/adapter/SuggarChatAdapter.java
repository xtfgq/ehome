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

import com.zzu.ehome.bean.SuggarXueBean;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.DateUtils;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Administrator on 2016/5/11.
 */
public class SuggarChatAdapter extends BaseAdapter implements StickyListHeadersAdapter {
    public List<SuggarXueBean> getList() {
        return list;
    }

    public void setList(List<SuggarXueBean> list) {
        this.list = list;
    }

    List<SuggarXueBean> list;
    private Context mContext;
    private LayoutInflater mInflater;


    public SuggarChatAdapter(Context context) {
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
        SuggarXueBean res = (SuggarXueBean) getItem(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_suggar,
                    parent, false);
            holder = new ViewHolder();
            holder.tv_value = (TextView) convertView
                    .findViewById(R.id.tv_xt_num);
            holder.tv_suggartime = (TextView) convertView.findViewById(R.id.tv_xt_time);
            holder.tvtime = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_status = (TextView) convertView.findViewById(R.id.tv_xt_res);
            holder.rl_status = (RelativeLayout) convertView.findViewById(R.id.rl_status);
            holder.tv_ssy_come=(TextView)convertView.findViewById(R.id.tv_ssy_come);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String time = "";
        if (Integer.valueOf(res.getHoursAfterMeal()) == 1) {
            holder.tv_suggartime.setText("餐后");
            time = "餐后";
        } else if (Integer.valueOf(res.getHoursAfterMeal()) == 0) {
            holder.tv_suggartime.setText("空腹");
            time = "空腹";
        } else {
            holder.tv_suggartime.setText("随机");
            time = "随机";
        }
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        String xt = "血糖:" +decimalFormat.format(Float.valueOf(res.getItemValue())) + "mmol/l";
        SpannableString style = new SpannableString(xt);
        style.setSpan(
                new TextAppearanceSpan(mContext, R.style.styleNormalColor), 3, xt.length() - 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.tv_value.setText(style);
        checkSuager(Float.valueOf(res.getItemValue()), holder.tv_status, time, holder.rl_status);
        holder.tvtime.setText(DateUtils.StringPattern(res.getMonitorTime(), "yyyy/MM/dd HH:mm:ss", "dd日 HH:mm"));
        String ss3="来源："+CommonUtils.showFromto(res.getFromTo());
        SpannableString style3 = new SpannableString(ss3);
        style3.setSpan(
                new TextAppearanceSpan(mContext, R.style.styleNormalColor), 3, ss3.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        holder.tv_ssy_come.setText(style3);

        return convertView;

    }

    public static class ViewHolder {
        public TextView tv_value;
        public TextView tv_suggartime;
        public TextView tvtime;
        public TextView tv_status;
        public RelativeLayout rl_status;
        public TextView tv_ssy_come;
    }

    private void checkSuager(float value, TextView tv, String time, RelativeLayout rl) {
        if (time.contains("餐后")) {
            if (Float.compare(value, 7.6F) >= 0 && Float.compare(value, 11.1F) <= 0) {
                tv.setText("正常");
                rl.setBackgroundResource(R.drawable.btn_yuyue_4);

            } else if (Float.compare(value, 11.1F) > 0) {
                tv.setText("偏高");
                rl.setBackgroundResource(R.drawable.btn_yuyue_6);

            } else {
                tv.setText("偏低");
                rl.setBackgroundResource(R.drawable.btn_yuyue_5);

            }

        } else if (time.contains("空腹")) {
            if (Float.compare(value, 3.9F) < 0) {
                tv.setText("偏低");
                rl.setBackgroundResource(R.drawable.btn_yuyue_5);
            } else if (Float.compare(value, 3.9F) >= 0 && Float.compare(value, 6.1F) <= 0) {
                tv.setText("正常");
                rl.setBackgroundResource(R.drawable.btn_yuyue_4);
            } else {
                tv.setText("偏高");
                rl.setBackgroundResource(R.drawable.btn_yuyue_6);
            }

        } else {
            if (Float.compare(value, 3.9F) < 0) {
                tv.setText("偏低");
                rl.setBackgroundResource(R.drawable.btn_yuyue_5);
            } else if (Float.compare(value, 3.9F) >= 0 && Float.compare(value, 11.1F) <= 0) {
                tv.setText("正常");
                rl.setBackgroundResource(R.drawable.btn_yuyue_4);
            } else {
                tv.setText("偏高");
                rl.setBackgroundResource(R.drawable.btn_yuyue_6);
            }
        }
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        SuggarXueBean res = (SuggarXueBean) getItem(position);
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

    @Override
    public long getHeaderId(int position) {
        SuggarXueBean res = (SuggarXueBean) getItem(position);
        return CommonUtils.returnLongNew(DateUtils.StringPattern(res.getMonitorTime(), "yyyy/MM/dd HH:mm:ss", "yyyy-MM"));
    }
}
