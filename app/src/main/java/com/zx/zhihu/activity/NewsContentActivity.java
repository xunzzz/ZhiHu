package com.zx.zhihu.activity;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.okhttp.Request;
import com.zx.zhihu.R;
import com.zx.zhihu.bean.Content;
import com.zx.zhihu.bean.StoriesEntity;
import com.zx.zhihu.network.OkHttpManager;
import com.zx.zhihu.network.ResultCallback;
import com.zx.zhihu.utils.Constant;
import com.zx.zhihu.utils.GsonUtils;

/**
 * Created by zhangxun on 2015/9/24.
 */
public class NewsContentActivity extends BaseActivity{
    private ImageLoader mImageLoader;
    private WebView mWebView;
    private ImageView imageView;
    private Toolbar toolbar;
    private OkHttpManager httpManager;
    private StoriesEntity entity;
    private Content content;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;


    @Override
    protected int getContentViewLayoutId() {
        return R.layout.news_content_layout;
    }

    @Override
    public void initView() {
        findviews();
        initSet();
    }

    private void initSet() {
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.back);
        ab.setDisplayHomeAsUpEnabled(true);


        mImageLoader = ImageLoader.getInstance();
        entity = (StoriesEntity) getIntent().getSerializableExtra("entity");
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // 开启DOM storage API 功能
        mWebView.getSettings().setDomStorageEnabled(true);
        // 开启database storage API功能
        mWebView.getSettings().setDatabaseEnabled(true);
        // 开启Application Cache功能
        mWebView.getSettings().setAppCacheEnabled(true);
        if (httpManager.isNetworkConnected(NewsContentActivity.this)){
            httpManager.get(Constant.CONTENT + entity.getId(), new ResultCallback() {
                @Override
                public void onSuccess(String data) {
                    parseJson(data);
                }

                @Override
                public void onError(Request request, Exception e) {

                }
            });
        }
    }

    private void parseJson(String data) {
        content = GsonUtils.parse2Bean(data, Content.class);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        mImageLoader.displayImage(content.getImage(), imageView, options);
        mCollapsingToolbarLayout.setTitle(content.getTitle());
        String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/css/news.css\" type=\"text/css\">";
        String html = "<html><head>" + css + "</head><body>" + content.getBody() + "</body></html>";
        html = html.replace("<div class=\"img-place-holder\">", "");
        mWebView.loadDataWithBaseURL("x-data://base", html, "text/html", "UTF-8", null);
    }

    private void findviews() {
        mCollapsingToolbarLayout = findView(R.id.collapsing_toolbar);
        httpManager = OkHttpManager.getInstance();
        mWebView = findView(R.id.webview);
        imageView = findView(R.id.iv_title);
        toolbar = findView(R.id.toolbar);
    }

    @Override
    public void widgetClick(View v) {

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId()){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
