package com.zzu.ehome.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;


import com.yiguo.toast.Toast;
import com.zzu.ehome.view.CustomDialog;


public class ToastUtils {
//    private static Handler handler = new Handler(Looper.getMainLooper());
//    private static Toast toast = null;

    public static void showMessage(final Context act, final String msg) {
        if (act != null) {
            Toast.makeText(act,msg,
                    Toast.LENGTH_LONG).show();
//            showMessage(act, msg, Toast.LENGTH_LONG);
//            showCustomDialog(msg,act);
        }

    }

    public static void showMessage(final Context act, final int msg) {
        if (act != null) {
            Toast.makeText(act,msg,
                    Toast.LENGTH_LONG).show();
//            showMessage(act, msg, Toast.LENGTH_LONG);
        }

    }

//    public static void showMessage(final Context act, final int msg,
//                                   final int len) {
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                if (toast != null) {
//                    toast.setText(msg);
////                    toast.setDuration(len);
//                } else {
//                    toast = Toast.makeText(act, msg, len);
//               }
//                toast.show();
//            }
//        });
//    }
//
//    public static void showMessage(final Context act, final String msg,
//                                   final int len) {
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                if (toast != null) {
//                    toast.setText(msg);
////                    toast.setDuration(len);
//                } else {
//                    toast = Toast.makeText(act, msg, len);
//                }
//                toast.show();
//            }
//        });
//    }
//    public static void showCustomDialog(String warmInfo, Context context) {
//        CustomDialog.Builder customBuilder = new
//                CustomDialog.Builder(context);
//        customBuilder.setMessage(warmInfo);
//        final CustomDialog customDialog = customBuilder.create();
//        customDialog.show();
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            public void run() {
//                customDialog.dismiss();
//            }
//        }, 3000);
//    }
}
