package com.zzu.ehome.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zzu.ehome.R;
import com.zzu.ehome.activity.LoginActivity1;
import com.zzu.ehome.activity.SelectPatientActivity;
import com.zzu.ehome.bean.DoctorSchemaByTopmdBean;
import com.zzu.ehome.main.ehome.MainActivity;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.view.DialogTips;

import java.util.List;

import static com.zzu.ehome.R.color.actionbar_color;
import static com.zzu.ehome.R.id.tv;

/**
 * Created by Mersens on 2016/8/9.
 */
public class DoctorTimeAdapter extends BaseListAdapter<DoctorSchemaByTopmdBean> {
    private List<DoctorSchemaByTopmdBean> mList;
    private Context context;
    private String usrid,hosid,depid,doctorid,DepartmentName;

    public DoctorTimeAdapter(Context context, List<DoctorSchemaByTopmdBean> objects,String hosid,String depid,String doctorid,String DepartmentName) {
        super(context, objects);
        this.mList=objects;
        this.context=context;
        this.hosid=hosid;
        this.depid=depid;
        this.doctorid=doctorid;
        this.DepartmentName=DepartmentName;
    }

    @Override
    public View getGqView(int position, View convertView, ViewGroup parent) {
        View mView=getInflater().inflate(R.layout.doctor_time_item,null);
        TextView tv_gh=(TextView) mView.findViewById(R.id.tv_gh);
        TextView tvcontent=(TextView)mView.findViewById(R.id.tvcontent);
        TextView tvresidue=(TextView)mView.findViewById(R.id.tvresidue);

        tvcontent.setText(mList.get(position).getScheduleTimeContent());
        tvresidue.setText("剩余号源"+mList.get(position).getRegistNum());
       int status= Integer.valueOf(mList.get(position).getSchemaStatu());
        if(status==1){
            tv_gh.setText("挂号");
            tv_gh.setBackgroundColor(mContext.getResources().getColor(R.color.actionbar_color));
            tv_gh.setEnabled(true);
        }else if(status==0||Integer.valueOf(mList.get(position).getRegistNum())<0){
            tv_gh.setText("停诊");
            tv_gh.setEnabled(false);
            tv_gh.setBackgroundColor(mContext.getResources().getColor(R.color.bottom_text_color_normal));
        }
        tv_gh.setOnClickListener(new MyClickListener(position));
        return mView;
    }

    class MyClickListener implements View.OnClickListener{
        private int pos;
        public  MyClickListener(int pos){
            this.pos=pos;
        }

        @Override
        public void onClick(View v) {
            usrid= SharePreferenceUtil.getInstance(context).getUserId();
            if(TextUtils.isEmpty(usrid)){
                context.startActivity(new Intent(context, LoginActivity1.class));
                return;
            }
            if(Integer.parseInt(mList.get(pos).getRegistNum())==0){
                shownDialog();
            }else {
                Intent intent = new Intent();
                intent.setClass(mContext, SelectPatientActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("HospitalID",hosid);
                bundle.putString("DepartmentID",depid);
                bundle.putString("DoctorID",doctorid);
                bundle.putString("DepartName",DepartmentName);
                bundle.putSerializable("DoctorTime", mList.get(pos));
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }

        }
    }
    private void shownDialog() {
        DialogTips dialog = new DialogTips(context, "该时段暂无可预约号源",
                "确定");
        dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int userId) {
            }
        });

        dialog.show();
        dialog = null;

    }


}
