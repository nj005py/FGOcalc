package org.phantancy.fgocalc.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.phantancy.fgocalc.R;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.spreada.utils.chinese.ZHConverter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

    public static void copy2Clipboard(Context ctx,String str,String hint){
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("fgocalc_txt", str);
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
        ToastUtils.displayShortToast(ctx, hint);
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

    //app版本大小比较
    public static boolean compareAppVersion(String preVersion,String curVersion){
        Log.d(TAG,"preVersion is " + preVersion + " curVersion is " + curVersion);
        //判断版本号大小
        String curVer[] = curVersion.split("\\.");
        String preVer[] = preVersion.split("\\.");
        //版本号分4段比较，第4段表示状态
        //v1.5.3.release vs 1.5.4.base
        //1号位
        int curVerHead = 0;
        int preVerHead = 0;
        if (curVer[0].contains("v")) {
            curVerHead = Integer.parseInt(curVer[0].charAt(1) + "");
        }else{
            curVerHead = Integer.parseInt(curVer[0]);
        }
        if (preVer[0].contains("v")) {
            preVerHead = Integer.parseInt(preVer[0].charAt(1) + "");
        }else{
            preVerHead = Integer.parseInt(preVer[0]);
        }
        //比较1号
        if (curVerHead < preVerHead) {
            Log.d(TAG,"preVersion pos1 is larger!");
            return false;
            //比较2号位
        }else if (Integer.parseInt(curVer[1]) < Integer.parseInt(preVer[1])) {
            Log.d(TAG,"preVersion pos2 is larger!");
            return false;
            //比较3号
        }else if (Integer.valueOf(curVer[2]) < Integer.valueOf(preVer[2])) {
            Log.d(TAG,"preVersion pos3 is larger!");
            return false;
            //比较4号位（英文版本号）
        }else if (curVer.length == 4) {
            //前3个数字编号无法看出谁更大时，比较英文版本号
            /**
             *base 基础版
             * alpha 内测版
             * beta 公测版
             * explorer 探索版，不一定有
             * rc 基本成型版
             * release 正式版
             */
            if(preVer.length < 4){
                Log.d(TAG,"preVersion has no pos4");
                return false;
            }else if (ToolCase.get4thVersion(curVer[3]) < ToolCase.get4thVersion(preVer[3])) {
                Log.d(TAG,"preVersion pos4 is larger!");
                return false;
            }else{
                Log.d(TAG,"Alright,update!");
                return true;
            }
        }else{
            Log.d(TAG,"need not update!");
            return false;
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

    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.mipmap.loading)// 设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.loading)// 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.loading) // 设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)// 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)// 设置下载的图片是否缓存在SD卡中
                .considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)// 设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
    // .delayBeforeLoading(int delayInMillis)//int
    // delayInMillis为你设置的下载前的延迟时间
    // 设置图片加入缓存前，对bitmap进行设置
    // .preProcessor(BitmapProcessor preProcessor)
                .resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
                .displayer(new RoundedBitmapDisplayer(20))// 是否设置为圆角，弧度为多少
            .displayer(new FadeInBitmapDisplayer(100))// 是否图片加载好后渐入的动画时间
            .build();// 构建完成

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
}
