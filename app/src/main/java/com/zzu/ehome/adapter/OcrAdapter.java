package com.zzu.ehome.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.yiguo.toast.Toast;/**/

import com.bumptech.glide.Glide;
import com.zzu.ehome.R;
import com.zzu.ehome.application.Constants;
import com.zzu.ehome.bean.OcrBean;
import com.zzu.ehome.bean.OrderInquiryByTopmd;
import com.zzu.ehome.utils.DateUtils;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/9/14.
 */
public class OcrAdapter extends BaseListAdapter<OcrBean>{


    private List<OcrBean> mList;
    private Context mContext;



    public OcrAdapter(Context context, List<OcrBean> objects) {
        super(context, objects);
        this.mList = objects;
        this.mContext = context;

    }

    @Override
    public View getGqView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = getInflater().inflate(R.layout.item_ocr_save, null);
            holder.name = (TextView) convertView.findViewById(R.id.tvname);
            holder.num = (EditText) convertView.findViewById(R.id.ednum);
            holder.AToB = (TextView) convertView.findViewById(R.id.tvAtoB);
            holder.tvrange=(TextView)convertView.findViewById(R.id.tvRange) ;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        OcrBean item = getItem(position);
        holder.name.setText(item.getName());
        holder.num.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        holder.num.setText(item.getNum());
        holder.AToB.setText(item.getAToB());
        holder.tvrange.setText(item.getRange());
        return convertView;
    }


    public class ViewHolder {

        private TextView name;
        private EditText num;
        private TextView AToB;
        private TextView tvrange;
    }
}
