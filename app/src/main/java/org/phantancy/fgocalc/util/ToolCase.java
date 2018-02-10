package org.phantancy.fgocalc.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.phantancy.fgocalc.R;
import com.spreada.utils.chinese.ZHConverter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by PY on 2017/2/7.
 */
public class ToolCase {

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
        ArrayAdapter<String> spAdapter = new ArrayAdapter<String>(context,R.layout.item_content_spinner_deep,str);
        spAdapter.setDropDownViewResource(R.layout.item_content_spinner_deep);
        sp.setAdapter(spAdapter);
    }

    public static void spInitDeep(Context context,int[] inte,Spinner sp){
        String[] str = new String[inte.length];
        for(int i = 0;i < inte.length;i ++){
            str[i] = String.valueOf(inte[i]);
        }
        ArrayAdapter<String> spAdapter = new ArrayAdapter<String>(context,R.layout.item_content_spinner_deep,str);
        spAdapter.setDropDownViewResource(R.layout.item_content_spinner_deep);
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
        ToastUtils.displayShortToast(ctx, "结果已复制剪切板");
    }

    //获取指令卡列表
    public static List<Map<String, Object>> getCommandCards() {

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        //每个Map结构为一条数据，key与Adapter中定义的String数组中定义的一一对应。

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("img", R.mipmap.buster);
        map.put("name", "Buster");
        list.add(map);

        HashMap<String, Object> map2 = new HashMap<String, Object>();
        map2.put("img", R.mipmap.arts);
        map2.put("name", "Arts");
        list.add(map2);

        HashMap<String, Object> map3 = new HashMap<String, Object>();
        map3.put("img", R.mipmap.quick);
        map3.put("name", "Quick");
        list.add(map3);

        return list;
    }

    //获取指令卡+宝具卡
    public static List<Map<String, Object>> getCommandNPCards(String npColor) {

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        //每个Map结构为一条数据，key与Adapter中定义的String数组中定义的一一对应。

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("img", R.mipmap.buster);
        map.put("name", "Buster");
        list.add(map);

        HashMap<String, Object> map2 = new HashMap<String, Object>();
        map2.put("img", R.mipmap.arts);
        map2.put("name", "Arts");
        list.add(map2);

        HashMap<String, Object> map3 = new HashMap<String, Object>();
        map3.put("img", R.mipmap.quick);
        map3.put("name", "Quick");
        list.add(map3);

        int color = 0;
        switch (npColor) {
            case "b":
                color = R.mipmap.np_b;
                break;
            case "a":
                color = R.mipmap.np_a;
                break;
            case "q":
                color = R.mipmap.np_q;
                break;
        }

        HashMap<String, Object> map4 = new HashMap<String, Object>();
        map4.put("img", color);
        map4.put("name", "NP");
        list.add(map4);

        return list;
    }

    //版本号大小
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
}
