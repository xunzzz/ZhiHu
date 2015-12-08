package com.zx.zhihu.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.okhttp.Request;
import com.zx.zhihu.R;
import com.zx.zhihu.activity.MainActivity;
import com.zx.zhihu.activity.NewsContentActivity;
import com.zx.zhihu.adapter.NewsFragmentAdapter;
import com.zx.zhihu.bean.News;
import com.zx.zhihu.bean.StoriesEntity;
import com.zx.zhihu.network.OkHttpManager;
import com.zx.zhihu.network.ResultCallback;
import com.zx.zhihu.utils.Constant;
import com.zx.zhihu.utils.GsonUtils;

import java.util.List;

/**
 * Created by zhangxun on 2015/9/24.
 */
public class NewsFragment extends BaseFragment{
    private ImageLoader mImageLoader;
    private ListView lv_news;
    private ImageView iv_title;
    private TextView tv_title;
    private OkHttpManager httpManager;
    private News news;
    private NewsFragmentAdapter mAdapter;
    private String urlId;
    private String title;

    public NewsFragment(String id, String title) {
        this.urlId = id;
        this.title = title;
    }


    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        httpManager = OkHttpManager.getInstance();
        mImageLoader = ImageLoader.getInstance();
        ((MainActivity)mActivity).setToolbarTitle(title);
        View view = inflater.inflate(R.layout.news_layout, container, false);
        lv_news = (ListView) view.findViewById(R.id.lv_news);
        View header = inflater.inflate(R.layout.news_header, lv_news, false);
        iv_title = (ImageView) header.findViewById(R.id.iv_title);
        tv_title = (TextView) header.findViewById(R.id.tv_title);

        lv_news.addHeaderView(header);

        lv_news.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StoriesEntity entity = (StoriesEntity) parent.getAdapter().getItem(position);
                Intent intent = new Intent(mActivity, NewsContentActivity.class);
                intent.putExtra("entity", entity);
                startActivity(intent);

            }
        });

        lv_news.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (lv_news != null && lv_news.getChildCount() > 0) {
                    boolean enable = (firstVisibleItem == 0) && (view.getChildAt(firstVisibleItem).getTop() == 0);
                    ((MainActivity) mActivity).setSwipeRefreshEnable(enable);
                }
            }
        });


        return view;
    }


    @Override
    protected void initData() {
        super.initData();
        if (httpManager.isNetworkConnected(mActivity)){
            httpManager.get(Constant.THEMENEWS + urlId, new ResultCallback() {
                @Override
                public void onSuccess(String data) {
                    parseJson(data);
                }

                @Override
                public void onError(Request request, Exception e) {

                }
            });
        }else {


        }

    }

    private void parseJson(String data) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        news = GsonUtils.parse2Bean(data, News.class);
        List<StoriesEntity> storiesEntities = news.getStories();
        mAdapter = new NewsFragmentAdapter(mActivity, storiesEntities);
        lv_news.setAdapter(mAdapter);
        tv_title.setText(news.getDescription());
        mImageLoader.displayImage(news.getImage(), iv_title, options);
    }

    public void updateTheme(){
        mAdapter.updateTheme();
    }
}
