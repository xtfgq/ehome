package com.zzu.ehome.utils;

import android.provider.ContactsContract;

import com.zzu.ehome.application.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mersens on 2016/9/9.
 */
public class WebDatas {
    public static final String BASEURL= Constants.EhomeURL+"/manbing/";
    private Map<Integer,List<String>> map;
    private WebDatas(){
        initData();

    }
    private static WebDatas wd;
    public static WebDatas getInstance(){
        if(wd==null){
            wd=new WebDatas();
        }
        return wd;
    }
    public static String getTitle(int type){
        String name=null;
        switch (type){
            case 1:
                name="高血压";
                break;
            case 2:
                name="高血脂";
                break;
            case 3:
                name="糖尿病";
                break;
            case 4:
                name= "冠心病";
                break;
            case 5:
                name= "心脏病";
                break;
        }

        return name;

    }
    private void initData(){
        map=new HashMap<>();
        List<String> gxyList=new ArrayList<>();
        gxyList.add(BASEURL+"gxygaishu.html");
        gxyList.add(BASEURL+"gxybingyin.html");
        gxyList.add(BASEURL+"gxyzhengzhuang.html");
        gxyList.add(BASEURL+"gxyzhenduan.html");
        gxyList.add(BASEURL+"gxyzhiliao.html");
        map.put(1,gxyList);
        List<String> xxgList=new ArrayList<>();
        xxgList.add(BASEURL+"gxzgaishu.html");
        xxgList.add(BASEURL+"gxzbingyin.html");
        xxgList.add(BASEURL+"gxzzhengzhuang.html");
        xxgList.add(BASEURL+"gxzzhenduan.html");
        xxgList.add(BASEURL+"gxzzhiliao.html");
        map.put(2,xxgList);
        List<String> tnbList=new ArrayList<>();
        tnbList.add(BASEURL+"tnbgaishu.html");
        tnbList.add(BASEURL+"tnbbingyin.html");
        tnbList.add(BASEURL+"tnbzhengzhuang.html");
        tnbList.add(BASEURL+"tnbzhenduan.html");
        tnbList.add(BASEURL+"tnbzhiliao.html");
        map.put(3,tnbList);
        List<String> gxbList=new ArrayList<>();
        gxbList.add(BASEURL+"gxbgaishu.html");
        gxbList.add(BASEURL+"gxbbingyin.html");
        gxbList.add(BASEURL+"gxbzhengzhuang.html");
        gxbList.add(BASEURL+"gxbzhenduan.html");
        gxbList.add(BASEURL+"gxbzhiliao.html");
        map.put(4,gxbList);
        List<String> xzbList=new ArrayList<>();
        xzbList.add(BASEURL+"xzbgaishu.html");
        xzbList.add(BASEURL+"xzbbingyin.html");
        xzbList.add(BASEURL+"xzbzhengzhuang.html");
        xzbList.add(BASEURL+"xzbzhenduan.html");
        xzbList.add(BASEURL+"xzbzhiliao.html");
        map.put(5,xzbList);
    }

    public Map  getMap(){
        return map;
    }

}
