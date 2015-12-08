package com.zx.zhihu.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.ResponseHandlerInterface;

/**
 * Created by zhangxun on 2015/9/23.
 */
public class HttpUtils {
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, ResponseHandlerInterface responseHandler){
        client.get(Constant.BASEURL + url, responseHandler);
    }

    public static void getImage(String url, ResponseHandlerInterface responseHandler){
        client.get(url, responseHandler);
    }

    public static boolean isNetworkConnected(Context context){
        if (null != context){
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (null != networkInfo){
                return networkInfo.isAvailable();
            }
        }
        return false;
    }


}
