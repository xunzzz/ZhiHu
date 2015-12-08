package com.zx.zhihu.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;

import com.squareup.okhttp.OkHttpClient;
import com.zx.zhihu.utils.Constant;

import java.util.Map;

/**
 * Created by zhangxun on 2015/9/18.
 */
public class OkHttpManager {
    private static OkHttpManager mInstance;
    private OkHttpClient mOkHttpClient;
    private CommonGet mCommonGet;
    private Handler mHandler;


    private OkHttpManager() {
        mHandler = new Handler(Looper.getMainLooper());
        //创建okHttpClient对象
        mOkHttpClient = new OkHttpClient();
        mCommonGet = new CommonGet(mOkHttpClient, mHandler);


        //管理cookie（待定）



    }

    /**
     * 获取 OkHttpManager
     * @return
     */
    public static OkHttpManager getInstance(){
        if (mInstance == null){
            synchronized (OkHttpManager.class){
                if (mInstance == null){
                    mInstance = new OkHttpManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * get请求方式
     */
    public void get(String url, ResultCallback callback){
        url = Constant.BASEURL + url;
        mCommonGet.getAsyn(url, callback);
    }

    /**
     * 下载文件
     * @param url
     * @param callback
     */
    public void getImage(String url, ResultCallback callback){
        url = Constant.BASEURL + url;
        mCommonGet.downloadAsyn(url, Constant.BOOT_DIR, callback);
    }

    /**
     * 判断网络是否连接
     * @param context
     * @return
     */
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
