package org.phantancy.fgocalc.util;

/**
 * Created by PY on 2017/3/10.
 */

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OKhttpManager {
    private OkHttpClient client;
    private static OKhttpManager oKhttpManager;
    private Handler mHandler;

    /**
     * 单例获取 OKhttpManager实例
     */
    private static OKhttpManager getInstance() {
        if (oKhttpManager == null) {
            oKhttpManager = new OKhttpManager();
        }
        return oKhttpManager;
    }

    private OKhttpManager() {
        client = new OkHttpClient();
        mHandler = new Handler(Looper.getMainLooper());
    }


    /**************
     * 内部逻辑处理
     ****************/
    private Response p_getSync(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        return response;
    }

    private String p_getSyncAsString(String url) throws IOException {
        return p_getSync(url).body().string();
    }

    private void p_getAsync(String url, final DataCallBack callBack) {
        final Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                deliverDataFailure(call, e, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    String result = response.body().string();
                    deliverDataSuccess(result, callBack);
                } catch (IOException e) {
                    deliverDataFailure(call, e, callBack);
                }
            }
        });
    }

    private void p_postAsync(String url, Map<String, String> params, final DataCallBack callBack) {
        RequestBody requestBody = null;

        if (params == null) {
            params = new HashMap<String, String>();
        }
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey().toString();
            String value = null;
            if (entry.getValue() == null) {
                value = "";
            } else {
                value = entry.getValue().toString();
            }
            builder.add(key, value);
        }
        requestBody = builder.build();
        final Request request = new Request.Builder().url(url).post(requestBody).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                deliverDataFailure(call, e, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String result = response.body().string();
                    deliverDataSuccess(result, callBack);
                } catch (IOException e) {
                    deliverDataFailure(call, e, callBack);
                }
            }
        });
    }

    /**
     * 数据分发的方法
     */
    private void deliverDataFailure(final Call call, final IOException e, final DataCallBack callBack) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.requestFailure(call, e);
                }
            }
        });
    }

    private void deliverDataSuccess(final String result, final DataCallBack callBack) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.requestSuccess(result);
                }
            }
        });
    }


    /******************
     * 对外公布的方法
     *****************/
    public static Response getSync(String url) throws IOException {
        Log.d("getSync->",url);
        return getInstance().p_getSync(url);//同步GET，返回Response类型数据
    }


    public static String getSyncAsString(String url) throws IOException {
        return getInstance().p_getSyncAsString(url);//同步GET，返回String类型数据
    }

    public static void getAsync(String url, DataCallBack callBack) {
        Log.d("getAsync->",url);
        getInstance().p_getAsync(url, callBack);//异步GET请求
    }

    public static void postAsync(String url, DataCallBack callBack,Map<String, String> params) {
//        for (Map.Entry<String, String> entry : params.entrySet()) {
//            System.out.println(entry.getKey() + ":" + entry.getValue());
//        }
        getInstance().p_postAsync(url, params, callBack);//异步POST请求
    }

    /**
     * 数据回调接口
     */
    public interface DataCallBack {
        void requestFailure(Call call, IOException e);

        void requestSuccess(String result);
    }

}
