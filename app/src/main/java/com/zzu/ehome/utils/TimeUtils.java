package com.zzu.ehome.utils;


import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {
	 public static String dateToStamp(String s) throws Exception{
	        String res;
	        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        Date date = simpleDateFormat.parse(s);
	        long ts = date.getTime()/1000;
	        res = String.valueOf(ts);
	        return res;
	    }
	 public static String stampToDate(String s){
	        String res;
	        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        long lt = new Long(s);
	        Date date = new Date(lt);
	        res = simpleDateFormat.format(date);
	        return res;
	    }
	
	

}
