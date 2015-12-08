package com.zx.zhihu.network;

import android.os.Handler;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zhangxun on 2015/9/18.
 */
public class CommonGet {

    private OkHttpClient mOkHttpClient;
    private Handler mHandler;

    public CommonGet(OkHttpClient mOkHttpClient, Handler mHandler) {
        this.mOkHttpClient = mOkHttpClient;
        this.mHandler = mHandler;
    }

    public CommonGet() {
    }

    /**
     * 获取 Request
     * @param url 请求的；路径
     * @param tag 用于判断是否可以取消该请求
     * @return
     */
    public Request buildGetRequest(String url, Object tag){
        Request.Builder builder = new Request.Builder().url(url);
        if (tag != null){
            builder.tag(tag);
        }
        return  builder.build();
    }

    /**
     * 通用获取 Response 的方法,同步
     * @param request
     * @return
     * @throws IOException
     */
    public Response get(Request request) throws IOException {
        Call call = mOkHttpClient.newCall(request);
        return call.execute();
    }

    /**
     * 通过 url 获取Response,同步
     * @param url 请求的地址
     * @return
     * @throws IOException
     */
    public Response get(String url) throws IOException {
        return get(url, null);
    }

    /**
     * 通过 url和tag 获取Response,同步
     * @param url 请求的地址
     * @param tag 用于判断是否可以取消该请求
     * @return
     * @throws IOException
     */
    public Response get(String url, Object tag) throws IOException {
        Request request = buildGetRequest(url, tag);
        return get(request);
    }


    /**
     * 同步Get方法，返回字符串结果
     * @param url 请求路径
     * @return
     * @throws IOException
     */
    public String getAsString(String url) throws IOException {
        return getAsString(url, null);
    }

    /**
     * 同步Get方法，返回字符串结果
     * @param url 请求路径
     * @param tag 用于判断是否可以取消该请求
     * @return
     * @throws IOException
     */
    public String getAsString(String url, Object tag) throws IOException {
        Response response = get(url, tag);
        return response.body().string();
    }


    /**
     * 通用异步请求方法
     * @param request
     * @param callback
     */
    public void getAsyn(Request request, ResultCallback callback){
        deliveryResult(request, callback);
    }

    /**
     * 异步get请求方法
     * @param url 请求地址
     * @param callback 回调函数
     */
    public void getAsyn(String url, ResultCallback callback){
        getAsyn(url, callback, null);
    }

    /**
     * 异步get请求方法
     * @param url 请求地址
     * @param callback 回调函数
     * @param tag 用于判断是否可以取消该请求
     */
    public void getAsyn(String url, ResultCallback callback, Object tag){
        Request request = buildGetRequest(url, tag);
        getAsyn(request, callback);
    }

    /**
     * 处理异步get请求
     * @param request
     * @param callback
     */
    private void deliveryResult(Request request, final ResultCallback callback){

        callback.onBefore(request);

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                sendFailedStringCallback(request, e, callback);
            }

            @Override
            public void onResponse(Response response) {
                try {
                    String result = response.body().string();
//                    String data = new JSONObject(result).optString("data");
                    sendSuccessResultCallback(result, callback);
                } catch (Exception e) {
                    sendFailedStringCallback(response.request(), e, callback);
                }
            }
        });
    }

    /**
     * 处理错误的返回结果
     * @param request
     * @param e
     * @param callback
     */
    private void sendFailedStringCallback(final Request request, final Exception e, final ResultCallback callback) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(request, e);
                callback.onAfter();
            }
        });
    }

    /**
     * 处理正确的返回结果
     * @param object
     * @param callback
     */
    private void sendSuccessResultCallback(final Object object, final ResultCallback callback) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess((String) object);
                callback.onAfter();
            }
        });
    }
    /**
     * 异步下载文件
     *
     * @param url
     * @param destFileDir 本地文件存储的文件夹
     * @param callback
     */
    public void downloadAsyn(final String url, final String destFileDir, final ResultCallback callback, Object tag) {
        final Request request = new Request.Builder()
                .url(url)
                .tag(tag)
                .build();
        final Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(final Request request, final IOException e) {
                sendFailedStringCallback(request, e, callback);
            }

            @Override
            public void onResponse(Response response) {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    is = response.body().byteStream();
                    File dir = new File(destFileDir);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    File file = new File(dir, getFileName(url));
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    fos.flush();
                    //如果下载文件成功，第一个参数为文件的绝对路径
                    sendSuccessResultCallback(file.getAbsolutePath(), callback);
                } catch (IOException e) {
                    sendFailedStringCallback(response.request(), e, callback);
                } finally {
                    try {
                        if (is != null) is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null) fos.close();
                    } catch (IOException e) {
                    }
                }

            }
        });
    }


    public void downloadAsyn(final String url, final String destFileDir, final ResultCallback callback) {
        downloadAsyn(url, destFileDir, callback, null);
    }

    private String getFileName(String path) {
        int separatorIndex = path.lastIndexOf("/");
        return (separatorIndex < 0) ? path : path.substring(separatorIndex + 1, path.length());
    }
}
