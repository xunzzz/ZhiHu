package com.zx.zhihu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zx.zhihu.MyApplication;
import com.zx.zhihu.manager.AppManager;
import com.zx.zhihu.utils.StrUtils;

/**
 * Created by zhangxun on 2015/9/23.
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int layoutId = getContentViewLayoutId();
        if (layoutId > 0){
            setContentView(layoutId);

            AppManager.getAppManager().addActivity(this);
        }
        //初始化页面信息
        initView();

    }

    @Override
    public void onClick(View v) {
        widgetClick(v);
    }

    /**
     * 初始化布局
     * @return
     */
    protected int getContentViewLayoutId(){
        return -1;
    }
    /**
     * 初始化页面信息
     */
    public abstract void initView();

    /**
     * 初始化点击事件
     * @param v
     */
    public abstract void widgetClick(View v);

    public <T> T findView(int resId) {
        return (T) this.findViewById(resId);
    }


    /**
     * 通过Class跳转界面
     */
    public void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    /**
     * 含有Bundle通过Class跳转界面
     */
    public void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * 显示snackbar
     * @param msg
     */
    protected void showSnackBar(String msg){
        if (!StrUtils.isEmpty(msg)){
            Snackbar.make(getWindow().getDecorView(), msg, Snackbar.LENGTH_LONG).show();
        }
    }

    /**
     *
     * @return
     */
    protected MyApplication getMyApplication(){
        return (MyApplication) getApplication();
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void finish() {
        super.finish();
    }
}
