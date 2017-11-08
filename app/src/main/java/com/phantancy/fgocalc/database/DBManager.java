package com.phantancy.fgocalc.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.phantancy.fgocalc.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by PY on 2017/2/21.
 */
public class DBManager {
    private String TAG = "DBManager";
    private final int BUFFER_SIZE = 400000;
    public static final String DB_NAME = "servants.db"; //保存的数据库文件名
    public static final String PACKAGE_NAME = "com.phantancy.fgocalc";
    public static final String DB_PATH = "/data"
            + Environment.getDataDirectory().getAbsolutePath() + "/"
            + PACKAGE_NAME;  //在手机里存放数据库的位置

    private SQLiteDatabase database;
    private Context context;

    public DBManager(Context context) {
        this.context = context;
        Log.d(TAG,"DB_PATH->" + DB_PATH);
    }

    public void openDatabase() {
        this.database = this.openDatabase(DB_PATH + "/" + DB_NAME);
    }

    public SQLiteDatabase getDatabase(){
        return openDatabase(DB_PATH + "/" + DB_NAME);
    }

    public SQLiteDatabase openDatabase(String dbfile) {
        try {
            if (!(new File(dbfile).exists())) {//判断数据库文件是否存在，若不存在则执行导入，否则直接打开数据库
                InputStream is = this.context.getResources().openRawResource(
                        R.raw.servants); //欲导入的数据库
                FileOutputStream fos = new FileOutputStream(dbfile);
                byte[] buffer = new byte[BUFFER_SIZE];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
            }
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbfile,
                    null);
            return db;
        } catch (FileNotFoundException e) {
            Log.e("Database", "File not found");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("Database", "IO exception");
            e.printStackTrace();
        }
        return null;
    }
//do something else here<br>
    public void closeDatabase() {
        try{
            this.database.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    //清空表
    public void deleteTable(SQLiteDatabase db){
        db.execSQL("delete from tab_name");
    }

    //删除表,模拟异常情况
    public void dropTable(){
        try{
            SQLiteDatabase db = getDatabase();
            db.execSQL("drop table servants");
        }catch (Exception e){

        }
    }
}
