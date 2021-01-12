package org.phantancy.fgocalc.data;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import org.phantancy.fgocalc.entity.ServantEntity;
import org.phantancy.fgocalc.entity.SvtExpEntity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Database(entities = {ServantEntity.class, SvtExpEntity.class},version = 2,exportSchema = true)
public abstract class CalcDatabase extends RoomDatabase {

    final static String TAG = "CalcDatabase";
    public abstract ServantDao servantDao();
    public abstract SvtExpDao svtExpDao();
    public abstract NoblePhantasmDao noblePhantasmDao();

    public static CalcDatabase INSTANCE;

    public static final String DB_NAME = "fgocalc.db"; //保存的数据库文件名

    static CalcDatabase getDatabase(final Context ctx) {
        /**
         * 判断本地数据库是否存在
         * 不存在就将数据库文件复制到安装目录
         */
        File dbPath = ctx.getDatabasePath(DB_NAME);
        if (!dbPath.exists()) {
            try {
                //执行数据库导入
                InputStream is = ctx.getResources().getAssets().open(DB_NAME); //欲导入的数据库
                FileOutputStream fos = new FileOutputStream(dbPath);
                byte[] buffer = new byte[10000];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                Log.d(TAG,"db copy success");
                fos.close();//关闭输出流
                is.close();//关闭输入流
            } catch (FileNotFoundException e) {
                Log.d(TAG,"db copy error");
                e.printStackTrace();
            } catch (IOException e) {
                Log.d(TAG,"db copy error");
                e.printStackTrace();
            }
        }

        if (INSTANCE == null) {
            synchronized (CalcDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(ctx.getApplicationContext(),
                            CalcDatabase.class, DB_NAME)
                            // Wipes and rebuilds instead of migrating if no Migration object.
                            // Migration is not part of this codelab.
                            .fallbackToDestructiveMigration()
//                            .addCallback(sRoomDatabaseCallback)
                            .createFromAsset(DB_NAME)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
