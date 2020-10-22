package org.phantancy.fgocalc.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import org.phantancy.fgocalc.common.App;
import org.phantancy.fgocalc.util.ToastUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SqlLoC {
    public static Cursor getAllServant() {
        DBManager.getInstance().openDatabase();
        return DBManager.getInstance().database.rawQuery("SELECT * FROM servants", null);
    }

    public static Cursor getServantsByKeyWord(String keyword) {
        DBManager.getInstance().openDatabase();
        return DBManager.getInstance().database.rawQuery("SELECT * FROM servants WHERE name LIKE ? OR nickname LIKE ? ORDER BY CAST(id AS SIGNED) ASC",
                new String[]{"%" + keyword + "%", "%" + keyword + "%"});
    }

    /**
     * Created by PY on 2017/2/21.
     */
    public static class DBManager {
        private String TAG = "DBManager";
        private final int BUFFER_SIZE = 400000;
        public static final String DB_NAME = "fgocalc.db"; //保存的数据库文件名
        public static final String PACKAGE_NAME = "org.phantancy.fgocalc";
        public static final String DB_PATH = "/data"
                + Environment.getDataDirectory().getAbsolutePath() + "/"
                + PACKAGE_NAME;  //在手机里存放数据库的位置
        private Context ctx;
        public SQLiteDatabase database;

        //私有构造函数
        private DBManager(){
            ctx = App.getAppContext();
        }

        //私有静态内部类
        private static class DBManagerHolder{
            private static final DBManager instance = new DBManager();
        }

        //获取实例方法
        public static DBManager getInstance(){
            return DBManagerHolder.instance;
        }

        public void getDatabase(){
            this.database = openDatabase();
        }

        public void getDatabaseExtra(){
            this.database = openDatabaseExtra();
        }

        public SQLiteDatabase openDatabase() {
            String dbfile = DB_PATH + "/" + DB_NAME;
            try {
                if (!(new File(dbfile).exists())) {//判断数据库文件是否存在，若不存在则执行导入，否则直接打开数据库
    //                InputStream is = ctx.getResources().openRawResource(R.raw.fgocalc); //欲导入的数据库
                    InputStream is = null;
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

        public SQLiteDatabase openDatabaseExtra(){
            String dbfile = DB_PATH + "/" + DB_NAME;
            String extra = Environment.getExternalStoragePublicDirectory("Download") + "/fgocalc.db";
            try {
                if (!(new File(dbfile).exists())) {//判断数据库文件是否存在，若不存在则执行导入，否则直接打开数据库
                    File dbExtra = new File(extra);
                    if (dbExtra.exists()) {
                        InputStream is = new FileInputStream(dbExtra); //欲导入的数据库
                        FileOutputStream fos = new FileOutputStream(dbfile);
                        byte[] buffer = new byte[BUFFER_SIZE];
                        int count = 0;
                        while ((count = is.read(buffer)) > 0) {
                            fos.write(buffer, 0, count);
                        }
                        fos.close();
                        is.close();
                    }else{
                        ToastUtils.displayShortToast(ctx,"外部db文件不存在");
                    }
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
    //            SQLiteDatabase db = getDatabase();
    //            db.execSQL("drop table fgocalc");
            }catch (Exception e){

            }
        }

        public void saveImage(int id,String img){
            ContentValues cv=new ContentValues();
            cv.put("pic", img);//图片转为二进制
            database.update("fgocalc", cv,"id = ?",new String[]{id + ""});
            database.close();
        }
    }
}
