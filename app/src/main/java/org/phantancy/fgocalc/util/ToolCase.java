package org.phantancy.fgocalc.util;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.phantancy.fgocalc.R;
import org.phantancy.fgocalc.common.App;
import org.phantancy.fgocalc.entity.ServantEntity;

import com.google.gson.Gson;
import com.spreada.utils.chinese.ZHConverter;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by PY on 2017/2/7.
 */
public class ToolCase {
    static final String TAG = "ToolCase";

    public static boolean notEmpty(String v){
        if (TextUtils.isEmpty(v)) {
            return false;
        }else{
            return true;
        }
    }

    public static boolean notEmpty(View v){
        if (v == null) {
            return false;
        }else{
            if (v instanceof TextView) {
                if (TextUtils.isEmpty(((TextView) v).getText())) {
                    return false;
                }
            }else if (v instanceof EditText) {
                if (TextUtils.isEmpty(((EditText) v).getText())) {
                    return false;
                }
            }else if (v instanceof Button) {
                if (TextUtils.isEmpty(((Button) v).getText())) {
                    return false;
                }
            }
        }
        return true;
    }

    public static String etValue(EditText et){
        if (et.getText() == null) {
            return "";
        }else{
            return et.getText().toString().trim();
        }
    }

    public static void setViewValue(View v,String value){
        String result = "";
        if (v != null) {
            if (notEmpty(value)) {
                result = value;
            }else{
                result = "";
            }
            if (v instanceof TextView) {
                ((TextView) v).setText(result);
            }else if (v instanceof EditText) {
                ((EditText) v).setText(result);
            }else if (v instanceof Button) {
                ((Button) v).setText(result);
            }
        }
    }

    public static String getViewValue(View v){
        String result = "";
        if (v != null) {
            if (v instanceof TextView) {
                result = ((TextView) v).getText().toString();
            }else if (v instanceof EditText) {
                result = ((EditText) v).getText().toString();
            }else if (v instanceof Button) {
                result = ((Button) v).getText().toString();
            }
        }
        return result;
    }

    //只是获取输入框的值int
    public static int getViewInt(View v){
        String s = getViewValue(v);
        try {
            int i = Integer.valueOf(s);
            return i;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }

    //繁体转简体
    public static String tc2sc(@NonNull String str) {
        ZHConverter converter = ZHConverter.getInstance(ZHConverter.SIMPLIFIED);
        String simplifiedStr = converter.convert(str);
        return simplifiedStr;
    }

    public static void spInitCustom(Context context,String[] str,Spinner sp,int layout){
        ArrayAdapter<String> spAdapter = new ArrayAdapter<String>(context,layout,str);
        spAdapter.setDropDownViewResource(layout);
        sp.setAdapter(spAdapter);
    }

    public static void copy2Clipboard(Context ctx,TextView tv){
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("fgocalc_result", tv.getText());
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
        ToastUtils.displayShortToast(ctx,"结果已复制剪切板");
    }

    //复制到剪贴板
    public static boolean copy2Clipboard(String str){
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) App.getAppContext().getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("fgocalc_txt", str);
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
        return true;
    }

    public static void copy2ClipboardCharacter(Context ctx,String str,String hint){
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("fgocalc_txt", str);
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
        //提示
//        showTip(ctx,"tip_clipboard_normal.json",hint);
    }



    //生成servant json数组，为web版创造数据源
    public static void createJson(List<ServantEntity> servantList){
        JSONArray ja = new JSONArray();
        Gson gson = new Gson();
        for (int i = 0;i < servantList.size();i ++){
            String result = gson.toJson(servantList.get(i));
            try {
                JSONObject jo = new JSONObject(result);
                ja.put(i,jo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //写入文件
        try {
            String sdCardDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + "/fgocalc_json.txt";
            File saveFile = new File(sdCardDir);
            FileOutputStream outStream = new FileOutputStream(saveFile);
            outStream.write(ja.toString().getBytes());
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //跳转到浏览器打开web
    public static boolean openBrowser(Context ctx,String url){
        if (!TextUtils.isEmpty(url) && ctx != null){
            if (BaseUtils.isNetworkAvailable(ctx)) {
                Uri uri = Uri.parse(url);
                Intent i = new Intent();
                i.setAction("android.intent.action.VIEW");
                i.setData(uri);
                ctx.startActivity(i);
                return true;//ok
            }else{
                return false;//net error
            }
        }
        return false;//unknown error
    }

    //从asset加载json文件
    public static String loadJsonFromAsset(Context ctx,String jsonName) {
        InputStream in = null;
        String json;
        try {
            in = ctx.getResources().getAssets().open(jsonName);
            int size = in.available();
            byte[] buffer = new byte[size];
            in.read(buffer);
            in.close();
            json = new String(buffer,"UTF-8");
        } catch (IOException e) {
            throw new SQLiteException("database config is not exist");
        } finally {
            closeQuietly(in);
        }
        return json;
    }

    //关闭可关闭的流
    public static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            Log.e("Closeable","close with error");
        }
    }

    //获取资源ID
    public static int getResIdByName(String name, Class c) {
        if (TextUtils.isEmpty(name)) {
            return 0;
        } else {
            try {
                Field field = c.getField(name);
                int resId = field.getInt(null);
                return resId;
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        }
    }

    public static double doubleParser(String data) {
        try {
            return TextUtils.isEmpty(data) ? 0d : Double.parseDouble(data);
        } catch (NumberFormatException e) {
            return 0d;
        }
    }

    /**
     * 自动关闭软键盘
     * @param activity
     */
    public static void closeKeybord(Activity activity) {
        InputMethodManager imm =  (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null) {
            Log.d(TAG,"imm != null");
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

}
