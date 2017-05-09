package com.zzu.ehome.db;

import android.content.Context;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import com.zzu.ehome.utils.SharePreferenceUtil;
import com.zzu.ehome.utils.ToastUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Administrator on 2017/3/23.
 */

public class DBRongHelper extends SQLiteOpenHelper {
    private static String DB_PATH = "";
    private static String DB_NAME = "storage";
    private final Context myContext;
    String userid;

    public DBRongHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
        userid = SharePreferenceUtil.getInstance(myContext).getUserId();
        try {
            DB_PATH = "/data/data/com.zzu.ehome.main.ehome/files" + File.separator +
                    myContext.getApplicationContext().getPackageManager().getApplicationInfo("com.zzu.ehome.main.ehome",
                            PackageManager.GET_META_DATA).metaData.getString("RONG_CLOUD_APP_KEY") + File.separator;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void removeDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if (dbExist) {
            this.getReadableDatabase();
            try {
                delDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        } else {
            ToastUtils.showMessage(myContext, "数据库不存在！");
        }

    }


    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DB_PATH + userid + File.separator + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            ToastUtils.showMessage(myContext, "无数据库");
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }

    /**
     * 清除问题数据库
     *
     * @throws IOException
     */
    private void delDataBase() throws IOException {
        File file = new File(DB_PATH + userid + File.separator + DB_NAME);
        file.delete();
    }

    /**
     * 拷贝数据库，但只能在没有问题时使用
     *
     * @throws IOException
     */
    private void copyDataBase() throws IOException {
        File file = new File(DB_PATH + userid + File.separator + DB_NAME);
        FileInputStream myInput = new FileInputStream(file);
        String outFileName = "/sdcard/ehomecrash/" + DB_NAME;
        File fileOut = new File(outFileName);
        OutputStream myOutput = new FileOutputStream(fileOut);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
