package org.phantancy.fgocalc.util;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.phantancy.fgocalc.R;
import org.phantancy.fgocalc.dialog.TipDialog;
import org.phantancy.fgocalc.item.ServantItem;
import org.phantancy.fgocalc.item.TipItem;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spreada.utils.chinese.ZHConverter;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
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

    //设置沉浸式状态栏
    public static void setStatusBar(LinearLayout llStatusBar,int statusColor, Activity acty,boolean ifStatus,boolean ifNavigation){
        llStatusBar = (LinearLayout)acty.findViewById(R.id.status_bar);
        if (Build.VERSION.SDK_INT == 19) {
            if (ifStatus) {
                acty.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                int height = BaseUtils.getStatusBarHeight(acty);
                if (0 != statusColor) {
                    llStatusBar.setBackgroundColor(statusColor);
                }
                llStatusBar.setPadding(0, height, 0, 0);
            }
            if (ifNavigation) {
                acty.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0 全透明实现
            Window window = acty.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS|
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            if (ifStatus) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                if (0 != statusColor) {
                    window.setStatusBarColor(statusColor);
                }else{
                    window.setStatusBarColor(Color.TRANSPARENT);//calculateStatusColor(Color.WHITE, (int) alphaValue)
                }
//                int height = BaseUtils.getStatusBarHeight(acty);
//                llStatusBar.setPadding(0, height, 0, 0);
            }
            if (ifNavigation) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            window.setNavigationBarColor(Color.TRANSPARENT);
            }
        }
    }

    //繁体转简体
    public static String tc2sc(@NonNull String str) {
        ZHConverter converter = ZHConverter.getInstance(ZHConverter.SIMPLIFIED);
        String simplifiedStr = converter.convert(str);
        return simplifiedStr;
    }

    //spinner绑定数据源,simple样式限定
    public static void spInitSimple(Context context, String[] str, Spinner sp){
        ArrayAdapter<String> spAdapter = new ArrayAdapter<String>(context, R.layout.item_simple_spinner,str);
        spAdapter.setDropDownViewResource(R.layout.item_content_spinner);
        sp.setAdapter(spAdapter);
    }

    public static void spInitSimple(Context context, int[] str, Spinner sp){
        if (str != null) {
            String[] arr = new String[str.length];
            for (int i = 0;i < str.length;i ++){
                arr[i] = str + "";
            }
            ArrayAdapter<String> spAdapter = new ArrayAdapter<String>(context, R.layout.item_simple_spinner,arr);
            spAdapter.setDropDownViewResource(R.layout.item_content_spinner);
            sp.setAdapter(spAdapter);
        }
    }

    //spinner绑定数据源,deep样式
    public static void spInitDeep(Context context,String[] str,Spinner sp){
        ArrayAdapter<String> spAdapter = new ArrayAdapter<String>(context,R.layout.item_content_spinner_light,str);
        spAdapter.setDropDownViewResource(R.layout.item_content_spinner_light);
        sp.setAdapter(spAdapter);
    }

    public static void spInitDeep(Context context,int[] inte,Spinner sp){
        String[] str = new String[inte.length];
        for(int i = 0;i < inte.length;i ++){
            str[i] = String.valueOf(inte[i]);
        }
        ArrayAdapter<String> spAdapter = new ArrayAdapter<String>(context,R.layout.item_content_spinner_light,str);
        spAdapter.setDropDownViewResource(R.layout.item_content_spinner_light);
        sp.setAdapter(spAdapter);
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

    public static void copy2Clipboard(Context ctx,String str,String hint){
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("fgocalc_txt", str);
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
        ToastUtils.displayShortToast(ctx, hint);
    }

    public static void copy2ClipboardCharacter(Context ctx,String str,String hint){
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("fgocalc_txt", str);
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
        //提示
        showTip(ctx,"tip_clipboard_normal.json",hint);
    }

    //获取指令卡列表
    public static List<Map<String, Object>> getCommandCards() {

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        //每个Map结构为一条数据，key与Adapter中定义的String数组中定义的一一对应。

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("img", R.drawable.buster);
        map.put("name", "Buster");
        list.add(map);

        HashMap<String, Object> map2 = new HashMap<String, Object>();
        map2.put("img", R.drawable.arts);
        map2.put("name", "Arts");
        list.add(map2);

        HashMap<String, Object> map3 = new HashMap<String, Object>();
        map3.put("img", R.drawable.quick);
        map3.put("name", "Quick");
        list.add(map3);

        return list;
    }

    //获取指令卡+宝具卡
    public static List<Map<String, Object>> getCommandNPCards(String npColor) {

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        //每个Map结构为一条数据，key与Adapter中定义的String数组中定义的一一对应。

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("img", R.drawable.buster);
        map.put("name", "Buster");
        list.add(map);

        HashMap<String, Object> map2 = new HashMap<String, Object>();
        map2.put("img", R.drawable.arts);
        map2.put("name", "Arts");
        list.add(map2);

        HashMap<String, Object> map3 = new HashMap<String, Object>();
        map3.put("img", R.drawable.quick);
        map3.put("name", "Quick");
        list.add(map3);

        int color = 0;
        switch (npColor) {
            case "b":
                color = R.drawable.np_b;
                break;
            case "a":
                color = R.drawable.np_a;
                break;
            case "q":
                color = R.drawable.np_q;
                break;
        }

        HashMap<String, Object> map4 = new HashMap<String, Object>();
        map4.put("img", color);
        map4.put("name", "NP");
        list.add(map4);

        return list;
    }

    //app版本大小比较
    public static boolean compareAppVersion(String netVersion,String localVersion){
        System.out.println("preVersion is " + netVersion + " curVersion is " + localVersion);
        //判断版本号大小
        String localVer[] = localVersion.split("\\.");
        String netVer[] = netVersion.split("\\.");
        //1号位
        int loc1 = Integer.valueOf(localVer[0]);
        int loc2 = Integer.valueOf(localVer[1]);
        int loc3 = Integer.valueOf(localVer[2]);
        int net1 = Integer.valueOf(netVer[0]);
        int net2 = Integer.valueOf(netVer[1]);
        int net3 = Integer.valueOf(netVer[2]);
        //比较1号
        if (net1 < loc1) {
            return false;
            //比较2号位
        }else if (net2 < loc2) {
            return false;
            //比较3号
        }else if (net3 < loc3) {
            return false;
            //比较4号位（英文版本号）
        }else{
            return true;
        }
    }

    //英文版本号的大小
    public static int get4thVersion(String v){
        switch (v) {
            case "base":
                return 0;
            case "alpha":
                return 1;
            case "beta":
                return 2;
            case "explorer":
                return 3;
            case "rc":
                return 4;
            case "release":
                return 5;
        }
        return 0;
    }

    //获取阵营String
    public static String getAttributeString(int attr){
        //阵营 天地人星兽(0:天 1:地 2:人 3:星 4:兽)
        String value = "";
        switch (attr){
            case 0:
                value = "天";
                break;
            case 1:
                value = "地";
                break;
            case 2:
                value = "人";
                break;
            case 3:
                value = "星";
                break;
            case 4:
                value = "兽";
                break;
        }
        return value;
    }

    //图片转为二进制数据
    public static byte[] bitmap2bytes(Bitmap bitmap){
        int size = bitmap.getWidth() * bitmap.getHeight() * 4;
        //创建一个字节数组输出流,流的大小为size
        ByteArrayOutputStream baos= new ByteArrayOutputStream(size);
        try {
            //设置位图的压缩格式，质量为100%，并放入字节数组输出流中
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            //将字节数组输出流转化为字节数组byte[]
            byte[] imagedata = baos.toByteArray();
            return imagedata;
        }catch (Exception e){
        }finally {
            try {
                bitmap.recycle();
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new byte[0];
    }


    public static String bitmap2Base64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static Bitmap base642Bitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    //生成servant json数组，为web版创造数据源
    public static void createJson(List<ServantItem> servantList){
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
    public static int openBrowser(Context ctx,String url){
        if (!TextUtils.isEmpty(url) && ctx != null){
            if (BaseUtils.isNetworkAvailable(ctx)) {
                Uri uri = Uri.parse(url);
                Intent i = new Intent();
                i.setAction("android.intent.action.VIEW");
                i.setData(uri);
                ctx.startActivity(i);
                return 0;//ok
            }else{
                return 1;//net error
            }
        }
        return 2;//unknown error
    }

    public static void showTip(Context ctx,String jsonName) {
        if (ctx != null) {
            //获取json
            String json = loadJsonFromAsset(ctx,jsonName);

            //解析json
            Gson gson = new Gson();
            Type type = new TypeToken <TipItem>(){}.getType();
            TipItem item = gson.fromJson(json,type);

            //展示
            TipDialog d = new TipDialog(ctx,item);
            d.show();
        }
    }

    public static void showTip(Context ctx,String jsonName,String hint) {
        if (ctx != null && !TextUtils.isEmpty(hint)) {
            //获取json
            String json = loadJsonFromAsset(ctx,jsonName);

            //解析json
            Gson gson = new Gson();
            Type type = new TypeToken <TipItem>(){}.getType();
            TipItem item = gson.fromJson(json,type);

            item.setTip(hint);

            //展示
            TipDialog d = new TipDialog(ctx,item);
            d.show();
        }
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

}
