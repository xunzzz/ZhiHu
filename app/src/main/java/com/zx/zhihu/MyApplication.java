package com.zx.zhihu;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by zhangxun on 2015/9/23.
 */
public class MyApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        initImageLoader(getApplicationContext());//初始化ImageLoader
    }

    private void initImageLoader(Context context) {
        ImageLoaderConfiguration config = ImageLoaderConfiguration.createDefault(context);
        ImageLoader.getInstance().init(config);
    }
}
