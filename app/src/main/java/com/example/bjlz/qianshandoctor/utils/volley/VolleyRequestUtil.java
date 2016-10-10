package com.example.bjlz.qianshandoctor.utils.volley;

import android.content.Context;
import android.net.Uri;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.bjlz.qianshandoctor.application.MyApplication;

import java.util.Map;

/**
 * Volley请求工具类
 */
public class VolleyRequestUtil {

    public static StringRequest stringRequest;
    public static Context context;
    public static MyJsonObjectRequest jsonObjectRequest;

    /**
    * 获取GET请求内容
    * 参数：
    * context：当前上下文；
    * url：请求的url地址；
    * tag：当前请求的标签；
    * volleyListenerInterface：VolleyListenerInterface接口；
    * */
    public static void RequestGet(Context context, String url, String tag, VolleyListenerInterface volleyListenerInterface) {
        // 清除请求队列中的tag标记请求
        MyApplication.getHttpQueue().cancelAll(tag);
        // 创建当前的请求，获取字符串内容
        stringRequest = new StringRequest(Request.Method.GET, url, volleyListenerInterface.responseListener(), volleyListenerInterface.errorListener());
        // 为当前请求添加标记
        stringRequest.setTag(tag);
        // 将当前请求添加到请求队列中
        MyApplication.getHttpQueue().add(stringRequest);
        // 重启当前请求队列
        MyApplication.getHttpQueue().start();
    }

    /**
    * 获取POST请求内容（请求的代码为Map）
    * 参数：
    * context：当前上下文；
    * url：请求的url地址；
    * tag：当前请求的标签；
    * params：POST请求内容；map集合为参数
    * volleyListenerInterface：VolleyListenerInterface接口；
    * */
    public static void RequestPost(Context context, String url, String tag, final Map<String, String> params, VolleyListenerInterface volleyListenerInterface) {
        // 清除请求队列中的tag标记请求
        MyApplication.getHttpQueue().cancelAll(tag);
        // 创建当前的POST请求，并将请求内容写入Map中
        stringRequest = new StringRequest(Request.Method.POST, url, volleyListenerInterface.responseListener(), volleyListenerInterface.errorListener()){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        // 为当前请求添加标记
        stringRequest.setTag(tag);
        // 将当前请求添加到请求队列中
        MyApplication.getHttpQueue().add(stringRequest);
        // 重启当前请求队列
        MyApplication.getHttpQueue().start();
    }
    /**
     * 获取POST请求内容（请求的代码为Json）
     * 参数：
     * context：当前上下文；
     * url：请求的url地址；
     * tag：当前请求的标签；
     * params：POST请求内容；
     * volleyListenerInterface：VolleyListenerInterface接口；
     * */
    public static void RequestPostByJson(Context context, final String url, String tag, final Map<String, String> map, VolleyJsonObjectListenerInterface volleyListenerInterface) {
        // 清除请求队列中的tag标记请求
        MyApplication.getHttpQueue().cancelAll(tag);
        String params = appendParameter(url,map);
        // 创建当前的POST请求，并将请求内容写入Map中
        jsonObjectRequest  = new MyJsonObjectRequest(Request.Method.POST, url,params, volleyListenerInterface.responseObjListener(), volleyListenerInterface.errorObjListener()) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError{
//                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Accept", "application/json");
//                headers.put("Content-Type", "application/json; charset=UTF-8");
//                return headers;
//            }
        };

        // 为当前请求添加标记
        jsonObjectRequest.setTag(tag);
        // 将当前请求添加到请求队列中
        MyApplication.getHttpQueue().add(jsonObjectRequest);
        // 重启当前请求队列
        MyApplication.getHttpQueue().start();
    }
    private static String appendParameter(String url,Map<String,String> params){
        Uri uri = Uri.parse(url);
        Uri.Builder builder = uri.buildUpon();
        for(Map.Entry<String,String> entry:params.entrySet()){
            builder.appendQueryParameter(entry.getKey(),entry.getValue());
        }
        return builder.build().getQuery();
    }
}