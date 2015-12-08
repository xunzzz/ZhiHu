package com.zx.zhihu.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import com.squareup.okhttp.Request;
import com.zx.zhihu.R;
import com.zx.zhihu.activity.MainActivity;
import com.zx.zhihu.activity.NewsContentActivity;
import com.zx.zhihu.adapter.MainFragmentAdapter;
import com.zx.zhihu.bean.Before;
import com.zx.zhihu.bean.Latest;
import com.zx.zhihu.bean.StoriesEntity;
import com.zx.zhihu.bean.TopStoriesEntity;
import com.zx.zhihu.network.OkHttpManager;
import com.zx.zhihu.network.ResultCallback;
import com.zx.zhihu.utils.Constant;
import com.zx.zhihu.utils.GsonUtils;
import com.zx.zhihu.views.Kanner;

import java.util.List;

/**
 * Created by zhangxun on 2015/9/24.
 */
public class MainFragment extends BaseFragment{
    private ListView lv_news;
    private List<Latest> items;
    private Latest latest;
    private Before before;
    private Kanner kanner;
    private Handler handler = new Handler();
    private MainFragmentAdapter mAdapter;
    private OkHttpManager httpManager;
    private boolean isLoading = false;
    private String date;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        httpManager = OkHttpManager.getInstance();
        ((MainActivity) mActivity).setToolbarTitle("今日热闻");
        View view = inflater.inflate(R.layout.main_news_layout, container, false);
        lv_news = (ListView) view.findViewById(R.id.lv_news);
        View header = inflater.inflate(R.layout.mainfragment_header, lv_news, false);
        kanner = (Kanner) header.findViewById(R.id.kanner);
        kanner.setOnItemClickListener(new Kanner.OnItemClickListener() {
            @Override
            public void click(View v, TopStoriesEntity entity) {
                StoriesEntity storiesEntity = new StoriesEntity();
                storiesEntity.setId(entity.getId());
                storiesEntity.setTitle(entity.getTitle());
                Intent intent = new Intent(mActivity, NewsContentActivity.class);
                intent.putExtra("entity", storiesEntity);
                startActivity(intent);
            }
        });
        lv_news.addHeaderView(header);
        mAdapter = new MainFragmentAdapter(mActivity);
        lv_news.setAdapter(mAdapter);
//        lv_news.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                StoriesEntity entity = (StoriesEntity) parent.getAdapter().getItem(position);
//                if (position != 0 && Constant.TOPIC != entity.getType()){
//                    Intent intent = new Intent(mActivity, NewsContentActivity.class);
//                    intent.putExtra("entity", entity);
//                    startActivity(intent);
//                }
//            }
//        });
        lv_news.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (lv_news != null && lv_news.getChildCount() > 0){
                    boolean enable = (firstVisibleItem == 0) && (view.getChildAt(firstVisibleItem).getTop() == 0);
                    ((MainActivity) mActivity).setSwipeRefreshEnable(enable);
                    if (enable){
                        ((MainActivity) mActivity).setToolbarTitle("首页");
                    }else {
                        ((MainActivity) mActivity).setToolbarTitle("今日热闻");
                    }


                    if (firstVisibleItem + visibleItemCount == totalItemCount && !isLoading){
                        loadMore(Constant.BEFORE +date);
                    }

                }
            }
        });
        return view;
    }

    private void loadMore(String url) {
        isLoading = true;
        if (httpManager.isNetworkConnected(mActivity)){
            httpManager.get(url, new ResultCallback() {
                @Override
                public void onSuccess(String data) {
                    parseBeforeJson(data);
                }

                @Override
                public void onError(Request request, Exception e) {

                }
            });
        }else {

        }


    }

    private void parseBeforeJson(String data) {
        before = GsonUtils.parse2Bean(data, Before.class);
        if (null == before){
            isLoading = false;
            return;
        }
        date = before.getDate();
        final List<StoriesEntity> storiesEntities = before.getStories();
        handler.post(new Runnable() {
            @Override
            public void run() {
                StoriesEntity topic = new StoriesEntity();
                topic.setType(Constant.TOPIC);
                topic.setTitle(convertDate(date));
                storiesEntities.add(0, topic);
                mAdapter.addList(storiesEntities);
                isLoading = false;
            }
        });

    }


    @Override
    protected void initData() {
        super.initData();
        loadData();

    }

    private void loadData() {
        isLoading = true;
        if (httpManager.isNetworkConnected(mActivity)){
            httpManager.get(Constant.LATESTNEWS, new ResultCallback() {
                @Override
                public void onSuccess(String data) {
                    parseLatestJson(data);
                }

                @Override
                public void onError(Request request, Exception e) {

                }
            });

        }else {

        }

    }

    private void parseLatestJson(String data) {
        latest = GsonUtils.parse2Bean(data, Latest.class);
        date = latest.getDate();
        final List<StoriesEntity> storiesEntities = latest.getStories();
        kanner.setTopEntities(latest.getTop_stories());
        handler.post(new Runnable() {
            @Override
            public void run() {
                StoriesEntity topic = new StoriesEntity();
                topic.setType(Constant.TOPIC);
                topic.setTitle("今日热闻");
                storiesEntities.add(0, topic);
                mAdapter.addList(storiesEntities);
                isLoading = false;
            }
        });
    }

    private String convertDate(String date) {
        String result = date.substring(0, 4);
        result += "年";
        result += date.substring(4, 6);
        result += "月";
        result += date.substring(6, 8);
        result += "日";
        return result;
    }

    public void updateTheme(){
        mAdapter.updateTheme();
    }



}
