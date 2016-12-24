package com.zzu.ehome.db;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zzu.ehome.bean.CacheBean;
import com.zzu.ehome.bean.RelationDes;
import com.zzu.ehome.bean.StepBean;
import com.zzu.ehome.bean.User;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.type;

/**
 * Created by zzu on 2016/4/6.
 */

public class EHomeDaoImpl implements EHomeDao {
    private DBHelper helper;
    private Context context;

    public EHomeDaoImpl(Context context) {
        helper = DBHelper.getInstance(context);
        this.context = context;
    }
    @Override
    public void delUserInfoById(String userid) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("delete from login_historytb where userid=?",
                new Object[]{userid});
        db.close();
    }

    @Override
    public boolean findUserIsExist(String userid) {
        boolean flag = false;
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery(
                "select * from login_historytb where userid=? ",
                new String[]{userid});
        while (cursor.moveToNext()) {
            flag = true;
        }
        cursor.close();
        db.close();
        return flag;
    }
    @Override
    public List<User> findAllUser() {
        List<User> list = new ArrayList<User>();
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery(
                "select * from login_historytb",
                null);
        while (cursor.moveToNext()) {
            User user = new User();
            String userid = cursor.getString(cursor.getColumnIndex("userid"));
            user.setUserid(userid);
            String username = cursor.getString(cursor.getColumnIndex("username"));
            user.setUsername(username);
            String nick = cursor.getString(cursor.getColumnIndex("nick"));
            user.setNick(nick);
            String mobile = cursor.getString(cursor.getColumnIndex("mobile"));
            user.setMobile(mobile);
            String imgHead = cursor.getString(cursor.getColumnIndex("imgHead"));
            user.setImgHead(imgHead);
            String password = cursor.getString(cursor.getColumnIndex("password"));
            user.setPassword(password);
            String sex = cursor.getString(cursor.getColumnIndex("sex"));
            user.setSex(sex);
            String age = cursor.getString(cursor.getColumnIndex("age"));
            user.setAge(age);
            String userno = cursor.getString(cursor.getColumnIndex("userno"));
            user.setUserno(userno);
            String patientId = cursor.getString(cursor.getColumnIndex("patientId"));
            user.setPatientId(patientId);
            String height = cursor.getString(cursor.getColumnIndex("height"));
            user.setUserHeight(height);
            String order=cursor.getString(cursor.getColumnIndex("_order"));
            user.setOrder(order);
            int type= Integer.valueOf(cursor.getString(cursor.getColumnIndex("type")));
            user.setType(type);
            list.add(user);
        }
        cursor.close();
        db.close();
        return list;
    }

    @Override
    public User findUserInfoById(String userid) {
        List<User> list = new ArrayList<User>();
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from login_historytb where userid=?",
                new String[]{userid});
        while (cursor.moveToNext()) {
            User user = new User();
            String id = cursor.getString(cursor.getColumnIndex("userid"));
            user.setUserid(id);
            String username = cursor.getString(cursor.getColumnIndex("username"));
            user.setUsername(username);
            String nick = cursor.getString(cursor.getColumnIndex("nick"));
            user.setNick(nick);
            String mobile = cursor.getString(cursor.getColumnIndex("mobile"));
            user.setMobile(mobile);
            String imgHead = cursor.getString(cursor.getColumnIndex("imgHead"));
            user.setImgHead(imgHead);
            String password = cursor.getString(cursor.getColumnIndex("password"));
            user.setPassword(password);
            String sex = cursor.getString(cursor.getColumnIndex("sex"));
            user.setSex(sex);
            String age = cursor.getString(cursor.getColumnIndex("age"));
            user.setAge(age);
            String userno = cursor.getString(cursor.getColumnIndex("userno"));

            user.setUserno(userno);
            String patientId = cursor.getString(cursor.getColumnIndex("patientId"));
            user.setPatientId(patientId);
            String height = cursor.getString(cursor.getColumnIndex("height"));
            user.setUserHeight(height);
            String order=cursor.getString(cursor.getColumnIndex("_order"));
            user.setOrder(order);
            int type= Integer.valueOf(cursor.getString(cursor.getColumnIndex("type")));
            user.setType(type);
            list.add(user);
        }
        cursor.close();
        db.close();
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }

    }



    @Override
    public void addUserInfo(User user) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL(
                "insert into login_historytb(userid,username,nick,mobile,imgHead,password,sex,age,userno,patientId,height,_order,type) values(?,?,?,?,?,?,?,?,?,?,?,?,?)",
                new Object[]{user.getUserid(), user.getUsername(),
                        user.getNick(), user.getMobile(), user.getImgHead(), user.getPassword(), user.getSex(), user.getAge(), user.getUserno(), user.getPatientId(), user.getUserHeight(),user.getOrder(),user.getType()});
        db.close();
    }
    @Override
    public void addRelationInfo(RelationDes rs) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL(
                "insert into relation_db(relationid,img,ship) values(?,?,?)",
                new Object[]{rs.getRUserID(), rs.getUser_Icon(),rs.getRelationship()});
        db.close();
    }
    @Override
    public RelationDes findRelationInfoById(String rsid) {
        List<RelationDes> list = new ArrayList<RelationDes>();
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from relation_db where relationid=?",
                new String[]{rsid});
        while (cursor.moveToNext()) {
            RelationDes user = new RelationDes();
            String id = cursor.getString(cursor.getColumnIndex("relationid"));
            user.setRUserID(id);
            String usericon = cursor.getString(cursor.getColumnIndex("img"));
            user.setUser_Icon(usericon);
            String ship = cursor.getString(cursor.getColumnIndex("ship"));
            user.setRelationship(ship);
            list.add(user);
        }
        cursor.close();
        db.close();
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }

    }




    @Override
    public boolean findRsIsExist(String rsid) {
        boolean flag = false;
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery(
                "select * from relation_db where relationid=? ",
                new String[]{rsid});
        while (cursor.moveToNext()) {
            flag = true;
        }
        cursor.close();
        db.close();
        return flag;
    }


    @Override
    public void updateUserInfo(User user, String userid) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("UPDATE login_historytb SET username=?,nick=?,mobile=?,password=?,imgHead=? ,sex=?,age=?,userno=?,patientId=?,height=?,_order=?,type=?  where userid=?", new Object[]{
                user.getUsername(), user.getNick(), user.getMobile(), user.getPassword(), user.getImgHead(), user.getSex(), user.getAge(), user.getUserno(), user.getPatientId(), user.getUserHeight(),user.getOrder(),user.getType(), userid});
        db.close();
    }
    @Override
    public void updateResInfo(RelationDes rs, String rsid) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("UPDATE relation_db SET relationid=?,img=?,ship=? where relationid=?", new Object[]{
                rs.getRUserID(),rs.getUser_Icon(),rs.getRelationship(), rsid});
        db.close();
    }

    @Override
    public void saveStep(StepBean step) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL(
                "insert into step_tb(num,startTime,endTime,userid,uploadState) values(?,?,?,?,?)",
                new Object[]{step.getNum(), step.getStartTime(),
                        step.getEndTime(), step.getUserid(), step.getUploadState()});
        db.close();
    }

    @Override
    public void updateStep(StepBean step) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("UPDATE step_tb SET num=?,startTime=?,endTime=?,uploadState=? where userid=?", new Object[]{
                step.getNum(), step.getStartTime(), step.getEndTime(), step.getUploadState(), step.getUserid()});
        db.close();
    }

    @Override
    public StepBean loadSteps(String userid, String time) {
        List<StepBean> list = new ArrayList<StepBean>();
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from step_tb where userid=? and startTime like ?",
                new String[]{userid, "%" + time + "%"});
        while (cursor.moveToNext()) {
            StepBean step = new StepBean();
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            step.setId(id);

            int uploadState = cursor.getInt(cursor.getColumnIndex("uploadState"));
            step.setUploadState(uploadState);

            int num = cursor.getInt(cursor.getColumnIndex("num"));
            step.setNum(num);

            String startTime = cursor.getString(cursor.getColumnIndex("startTime"));
            step.setStartTime(startTime);

            String endTime = cursor.getString(cursor.getColumnIndex("endTime"));
            step.setEndTime(endTime);

            String uid = cursor.getString(cursor.getColumnIndex("userid"));
            step.setUserid(uid);
            list.add(step);
        }
        cursor.close();
        db.close();
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }
    @Override
    public void addCacheInfo(CacheBean cache) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL(
                "insert into cache_tb(url,json) values(?,?)",
                new Object[]{cache.getUrl(),cache.getJson()});
        db.close();
    }

    @Override
    public boolean findCacheIsExist(String url) {
        boolean flag = false;
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery(
                "select * from cache_tb where url=? ",
                new String[]{url});
        while (cursor.moveToNext()) {
            flag = true;
        }
        cursor.close();
        db.close();
        return flag;
    }

    @Override
    public void updateCacheInfo(CacheBean chache, String url) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("UPDATE cache_tb SET url=?,json=? where url=?", new Object[]{
                chache.getUrl(),chache.getJson(), url});
        db.close();

    }

    @Override
    public CacheBean findCache(String url) {
        List<CacheBean> list = new ArrayList<CacheBean>();
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from cache_tb where url=?",
                new String[]{url});
        while (cursor.moveToNext()) {
            CacheBean cache = new CacheBean();
            String urldata = cursor.getString(cursor.getColumnIndex("url"));
            cache.setUrl(urldata);
            String json = cursor.getString(cursor.getColumnIndex("json"));
            cache.setJson(json);

            list.add(cache);
        }
        cursor.close();
        db.close();
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }


}
