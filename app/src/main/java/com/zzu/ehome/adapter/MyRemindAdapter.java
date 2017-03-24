package com.zzu.ehome.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.kyleduo.switchbutton.SwitchButton;
import com.zzu.ehome.R;
import com.zzu.ehome.bean.MyRemindBean;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.RequestMaker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2016/9/19.
 */
public class MyRemindAdapter extends BaseListAdapter<MyRemindBean> {
    private List<MyRemindBean> mList;
    public List<MyRemindBean> getmList() {
        return mList;
    }

    public void setmList(List<MyRemindBean> mList) {
        this.mList = mList;
    }


    private Context mContext;
    private RequestMaker requestMaker;

    public MyRemindAdapter(Context context, List<MyRemindBean> objects) {
        super(context, objects);
        this.mList = objects;
        this.mContext = context;
        requestMaker = RequestMaker.getInstance();
    }

    @Override
    public View getGqView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = getInflater().inflate(R.layout.item_my_remind, null);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tvtype = (TextView) convertView.findViewById(R.id.tvtype);
            holder.tvweekday = (TextView) convertView.findViewById(R.id.tvweekday);
            holder.sbutn_md = (SwitchButton) convertView.findViewById(R.id.sbutn_md);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final MyRemindBean item = getItem(position);
        holder.tv_time.setText(item.getTime());
        int type = Integer.parseInt(item.getType());
        switch (type) {
            case 1:
                holder.tvtype.setText("用药提醒");
                break;
            case 2:
                holder.tvtype.setText("测量提醒");
                break;
            case 3:
                holder.tvtype.setText("私人问诊提醒");
                break;

        }

        if (item.getWeekday().equals("周一,周二,周三,周四,周五,周六,周日")) {
            holder.tvweekday.setText("每天");
        } else {
            holder.tvweekday.setText(item.getWeekday());
        }
        if (Integer.parseInt(item.getEnabled()) == 1) {
            holder.sbutn_md.setChecked(true);
        } else {
            holder.sbutn_md.setChecked(false);
        }
        holder.sbutn_md.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    update(item, 1,position);
                } else {
                    update(item, 0,position);
                }
            }
        });
//        holder.sbutn_md.setOnCheckedChangeListener(OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    update(item,1);
//                }else{
//                    update(item,0);
//                }
//
//            }
//        });


        return convertView;
    }

    public class ViewHolder {
        private TextView tv_time;
        private TextView tvtype;
        private TextView tvweekday;
        private SwitchButton sbutn_md;
        private ImageView ivhead;
        private ImageView ivstatus;
    }

    public void update(MyRemindBean item, final int i,final int p) {
        requestMaker.RemindUpdate(item.getID(), i + "", new JsonAsyncTask_Info(mContext, true, new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                try {
                    JSONObject mySO = (JSONObject) result;
                    JSONArray array = mySO
                            .getJSONArray("RemindUpdate");
                    if (array.getJSONObject(0).has("MessageCode")) {

//                        Toast.makeText(mContext, array.getJSONObject(0).getString("MessageContent").toString(),
//                                Toast.LENGTH_SHORT).show();
//                        if(Integer.valueOf(array.getJSONObject(0).getString("MessageCode"))==0) {
//
//                        }
                        mList.get(p).setEnabled(i+"");
                        notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Exception e) {

            }
        }));
    }


}
