package com.zx.zhihu.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.zx.zhihu.R;
import com.zx.zhihu.network.OkHttpManager;
import com.zx.zhihu.network.ResultCallback;
import com.zx.zhihu.utils.Constant;
import com.zx.zhihu.utils.GsonUtils;
import com.zx.zhihu.utils.StrUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by zhangxun on 2015/9/23.
 */
public class SplashActivity extends Activity {


    private ImageView iv_start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash);
        iv_start = (ImageView) findViewById(R.id.iv_start);
        initImage();
    }

    private void initImage() {
        File dir = getFilesDir();
        File imgFile = new File(dir, "start.jpg");
        if (imgFile.exists()) {
            iv_start.setImageBitmap(BitmapFactory.decodeFile(imgFile.getAbsolutePath()));
        } else {
            iv_start.setImageResource(R.mipmap.start);
        }

        ScaleAnimation animation = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        animation.setFillAfter(true);
        animation.setDuration(3000);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (OkHttpManager.isNetworkConnected(SplashActivity.this)) {
                    OkHttpManager.getInstance().get(Constant.START, new ResultCallback() {
                        @Override
                        public void onSuccess(String data) {
                            if (!StrUtils.isEmpty(data)) {
                                try {
                                    String url = new JSONObject(data).optString("img");
                                    OkHttpManager.getInstance().getImage(url, new ResultCallback() {
                                        @Override
                                        public void onSuccess(String data) {
                                            startActivity();
                                        }

                                        @Override
                                        public void onError(Request request, Exception e) {
                                            startActivity();
                                        }
                                    });
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }

                        @Override
                        public void onError(Request request, Exception e) {

                        }
                    });
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        iv_start.setAnimation(animation);

    }

    private void startActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fab_in, R.anim.fab_out);
        finish();
    }
}
