package com.zzu.ehome;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.zzu.ehome.application.Constants;
import com.zzu.ehome.bean.MSDoctorBean;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.RequestMaker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.model.UIConversation;
import io.rong.imkit.widget.provider.FileInputProvider;
import io.rong.imkit.widget.provider.ImageInputProvider;
import io.rong.imkit.widget.provider.InputProvider;
import io.rong.imkit.widget.provider.LocationInputProvider;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ImageMessage;

import static com.zzu.ehome.application.CustomApplcation.finishSingleActivity;
import static com.zzu.ehome.application.CustomApplcation.finishSingleActivityByClass;
import static com.zzu.ehome.application.CustomApplcation.mList;

/**融云taken
 * Created by Bob on 15/8/21.
 */
public class DemoContext{

    private static DemoContext mDemoContext;
    public Context mContext;
    private SharedPreferences mPreferences;
    private ArrayList<UserInfo> mUserInfos;


    public static DemoContext getInstance() {

        if (mDemoContext == null) {
            mDemoContext = new DemoContext();
        }
        return mDemoContext;
    }

    private DemoContext() {
    }

    private DemoContext(Context context) {
        mContext = context;
        mDemoContext = this;

        initListener();
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);

    }
    private void initListener(){
        setInputProvider();
    }
    private void setInputProvider() {

    }


    public static void init(Context context) {
        mDemoContext = new DemoContext(context);
    }

    public SharedPreferences getSharedPreferences() {
        return mPreferences;
    }






//    @Override
//    public UserInfo getUserInfo(String s) {
//
//        if (mList!=null&&mList.size()>0) {
//            for( MSDoctorBean bean:mList)
//                return new UserInfo(bean.getDoctorID(), bean.getDoctorName(),
//                        Uri.parse(Constants.EhomeURL + bean.getImageURL().replace("~", "").replace("\\", "/")));
//        }
//        return null;
//
//    }

}
