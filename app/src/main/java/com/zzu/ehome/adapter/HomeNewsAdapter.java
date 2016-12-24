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
import com.zzu.ehome.bean.News;
import com.zzu.ehome.utils.DateUtils;
import com.zzu.ehome.view.GlideRoundTransform;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/8/18
 * 首页慢病.
 */
public class HomeNewsAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
//    private ImageLoader mImageLoader;

    public List<News> getList() {
        return list;
    }

    public void setList(List<News> list) {
        this.list = list;
    }

    private List<News> list;

    public HomeNewsAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
//        mImageLoader = ImageLoader.getInstance();
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
        ViewHolder holder;
        News item = (News) getItem(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.home_item,
                    parent, false);
            holder = new ViewHolder();
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            holder.iv_head = (ImageView) convertView.findViewById(R.id.iv_head);
            holder.tvtime=(TextView)convertView.findViewById(R.id.timetv);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_title.setText(item.getTitle());
        holder.tv_content.setText(item.getZhaiyao());
        Date date=DateUtils.str2Date(item.getCreatedDate());
        try {
            int t=DateUtils.isYeaterday(date,null);
            if(t==-1){
                holder.tvtime.setText("今天 "+DateUtils.StringPattern(item.getCreatedDate(),"yyyy/MM/dd HH:mm:ss","HH:mm"));
            }else if(t==0){
                holder.tvtime.setText("昨天 "+DateUtils.StringPattern(item.getCreatedDate(),"yyyy/MM/dd HH:mm:ss","HH:mm"));
            }else if(t==1){
                holder.tvtime.setText(DateUtils.StringPattern(item.getCreatedDate(),"yyyy/MM/dd HH:mm:ss","MM/dd HH:mm"));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String url=Constants.EhomeURL + item.getPic().replace("~", "").replace("\\", "/");
        Glide.with(mContext)
                .load(Constants.EhomeURL + item.getPic().replace("~", "").replace("\\", "/"))
                .centerCrop().error(R.drawable.pic_defaultads).transform(new GlideRoundTransform(mContext, 5))
                .into(holder.iv_head);
//        mImageLoader.displayImage(Constants.EhomeURL + item.getPic(), holder.iv_head);
        return convertView;
    }

    public class ViewHolder {
        private TextView tv_title;
        private TextView tv_content;
        private ImageView iv_head;
        private TextView tvtime;

    }
}
