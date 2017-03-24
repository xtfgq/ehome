package com.zzu.ehome.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.zzu.ehome.DemoContext;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.SharePreferenceUtil;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * Created by zzu on 2016/4/6.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final int VERSION = 19;
    private static final String NAME = "EHOME.db";

    private static final String SQL_LOGIN_HISTORY_CREAT = "create table login_historytb(_id integer primary key autoincrement,userid text ,username text,nick text,mobile text,imgHead text,password text,sex text,age text,userno text,patientId text,height text,_order text,type integer)";
    private static final String SQL_LOGIN_HISTORY_DROP = "drop table if exists login_historytb";

    private static final String SQL_DISEASE_CREAT = "create table disease_tb(_id integer primary key autoincrement,name text ,type integer,open integer)";
    private static final String SQL_DISEASE_DROP = "drop table if exists disease_tb";

    private static final String SQL_STEP_CREAT = "create table step_tb(_id integer primary key autoincrement,num integer ,startTime text,endTime text,userid text,uploadState integer)";
    private static final String SQL_STEP_DROP = "drop table if exists step_tb";

    private static final String SQL_RS_CREAT = "create table relation_db(_id integer primary key autoincrement,relationid text ,img text,ship text)";
    private static final String SQL_RS_DROP = "drop table if exists relation_db";

    private static final String SQL_CACHE_CREAT = "create table cache_tb(_id integer primary key autoincrement,url text ,json text)";
    private static final String SQL_CACHE_DROP = "drop table if exists cache_tb";

    public static DBHelper helper = null;
    public static Context mContext;

    public static DBHelper getInstance(Context context) {
        if (helper == null) {
            synchronized (DBHelper.class) {
                if (helper == null) {
                    helper = new DBHelper(context.getApplicationContext());
                }
            }
        }
        mContext = context;
        return helper;
    }

    private DBHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_LOGIN_HISTORY_CREAT);
        db.execSQL(SQL_DISEASE_CREAT);
        db.execSQL(SQL_STEP_CREAT);
        db.execSQL(SQL_RS_CREAT);
        db.execSQL(SQL_CACHE_CREAT);
    }

    /**
     * 当数据库更新时，调用该方法
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (TextUtils.isEmpty(SharePreferenceUtil.getInstance(mContext).getUserId())) {
            clearCache(db);
        } else {
            //王id：113397，我163480
            if (SharePreferenceUtil.getInstance(mContext).getUserId().equals("113397") && VERSION == 19) {
                clearCache(db);
                String token = DemoContext.getInstance().getSharedPreferences().getString("DEMO_TOKEN", "default");
                CommonUtils.connent(token, new CommonUtils.RongIMListener() {
                    @Override
                    public void OnSuccess(String userid) {

                        try {
                            RongIM.getInstance().getRongIMClient().clearConversations(new RongIMClient.ResultCallback<Boolean>() {
                                @Override
                                public void onSuccess(Boolean aBoolean) {
                                }

                                @Override
                                public void onError(RongIMClient.ErrorCode errorCode) {
                                }
                            }, Conversation.ConversationType.PRIVATE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            } else if (VERSION > 19) {
                clearCache(db);
            }
        }
    }

    /**
     * 清空数据缓存
     *
     * @param db
     */
    public void clearCache(SQLiteDatabase db) {
        db.execSQL(SQL_LOGIN_HISTORY_DROP);
        db.execSQL(SQL_DISEASE_DROP);
        db.execSQL(SQL_STEP_DROP);
        db.execSQL(SQL_RS_DROP);
        db.execSQL(SQL_CACHE_DROP);
        db.execSQL(SQL_LOGIN_HISTORY_CREAT);
        db.execSQL(SQL_DISEASE_CREAT);
        db.execSQL(SQL_STEP_CREAT);
        db.execSQL(SQL_RS_CREAT);
        db.execSQL(SQL_CACHE_CREAT);
        SharePreferenceUtil.getInstance(mContext).setUserId("");
        SharePreferenceUtil.getInstance(mContext).setIsRemeber(false);
    }
}
