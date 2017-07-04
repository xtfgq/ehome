package com.zzu.ehome.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.yiguo.toast.Toast;/**/

import com.zzu.ehome.R;
import com.zzu.ehome.activity.SupperBaseActivity;
import com.zzu.ehome.adapter.CapaingAdapter;
import com.zzu.ehome.bean.CapaingBean;
import com.zzu.ehome.utils.JsonAsyncTaskOnComplete;
import com.zzu.ehome.utils.JsonAsyncTask_Info;
import com.zzu.ehome.utils.RequestMaker;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.view.PullToRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.zzu.ehome.R.id.listView;


/**
 * Created by Administrator on 2016/9/24.
 */
public class WeightChatFragmet extends BaseFragment{


    @Override
    protected void lazyLoad() {

    }
}
