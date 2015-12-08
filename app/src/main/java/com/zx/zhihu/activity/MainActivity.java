package com.zx.zhihu.activity;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.zx.zhihu.R;
import com.zx.zhihu.adapter.MenuListViewAdapter;
import com.zx.zhihu.bean.NewsListItem;
import com.zx.zhihu.fragment.MainFragment;
import com.zx.zhihu.fragment.NewsFragment;
import com.zx.zhihu.network.OkHttpManager;
import com.zx.zhihu.network.ResultCallback;
import com.zx.zhihu.utils.Constant;
import com.zx.zhihu.utils.GsonUtils;
import com.zx.zhihu.utils.PreUtils;
import com.zx.zhihu.utils.StrUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private DrawerLayout drawerlayout;
    private ListView menu_listview;
    private Toolbar toolBar;
    private SwipeRefreshLayout refreshLayout;
    private View header;
    private TextView tv_main;
    private LinearLayout ll_menu;


    private String curId;
    private MenuListViewAdapter adapter;
    private OkHttpManager httpManager = OkHttpManager.getInstance();
    private List<NewsListItem> menuItems;

    private boolean isLight;
    private SharedPreferences sp;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        findviews();
        initSet();
        reqNet4MenuList();
        loadLastest();
        replaceFragment();
    }

    private void loadLastest() {
        getSupportFragmentManager().beginTransaction().
                replace(R.id.fl_content, new MainFragment(), "latest").
                commit();
        curId = "latest";
    }

    public void replaceFragment() {
        if ("latest".equals(curId)){
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, new MainFragment(), "latest").commit();
        }else {

        }

    }

    /**
     * 请求网络获取导航列表
     */
    private void reqNet4MenuList() {
        if (httpManager.isNetworkConnected(MainActivity.this)){
            httpManager.get(Constant.THEMES, new ResultCallback() {
                @Override
                public void onSuccess(String data) {
                    if (!StrUtils.isEmpty(data)) {
                        PreUtils.putStringToDefault(MainActivity.this, Constant.THEMES, data);
                        parseJson(data);
                    }
                }

                @Override
                public void onError(Request request, Exception e) {

                }
            });
        }else {
            String data = PreUtils.getStringFromDefault(MainActivity.this, Constant.THEMES, "");
            parseJson(data);
        }
    }

    /**
     * 解析网络请求返回的导航列表
     * @param data
     */
    private void parseJson(String data) {
        try {
            String others = new JSONObject(data).optString("others");
            List<NewsListItem> items= GsonUtils.parse2List(others, NewsListItem[].class);
            if (null !=menuItems && menuItems.size() > 0){
                menuItems.clear();
            }
            menuItems.addAll(items);
            adapter = new MenuListViewAdapter(MainActivity.this, menuItems);
            menu_listview.setAdapter(adapter);
            updateLeftMenuTheme();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void findviews() {
        drawerlayout = findView(R.id.drawerlayout);
        menu_listview = findView(R.id.menu_listview);
        toolBar = findView(R.id.toolBar);
        refreshLayout = findView(R.id.refresh);
        header = LayoutInflater.from(MainActivity.this).inflate(R.layout.header, menu_listview, false);
        tv_main = (TextView) header.findViewById(R.id.tv_main);
        ll_menu = (LinearLayout) header.findViewById(R.id.ll_menu);

    }

    private void closeDrawerLayout(){
        if (drawerlayout.isDrawerOpen(Gravity.LEFT)) {
            drawerlayout.closeDrawers();
        }
    }

    private void initSet() {
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        isLight = sp.getBoolean("isLight", true);

        menuItems = new ArrayList<NewsListItem>();
        tv_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curId = "latest";
                replaceFragment();
                closeDrawerLayout();
            }
        });

        menu_listview.addHeaderView(header);

        menu_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_content,
                        new NewsFragment(menuItems.get(position - 1).getId(), menuItems.get(position - 1).getName()), "news").commit();
                closeDrawerLayout();
                curId = menuItems.get(position - 1).getId();
            }
        });
        toolBar.setBackgroundColor(getResources().getColor(isLight ? R.color.light_toolbar : R.color.dark_toolbar));
        setSupportActionBar(toolBar);
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
        setToolbarTitle("首页");
        setStatusBarColor(isLight ? R.color.light_toolbar : R.color.dark_toolbar);


        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                replaceFragment();
                refreshLayout.setRefreshing(false);
            }
        });
    }
    @Override
    public void widgetClick(View v) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.getItem(0).setTitle(sp.getBoolean("isLight", true) ? "夜间模式" : "日间模式");
        return true;
    }

    @TargetApi(21)
    private void setStatusBarColor(int statusBarColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // If both system bars are black, we can remove these from our layout,
            // removing or shrinking the SurfaceFlinger overlay required for our views.
           /* Window window = this.getWindow();
            if (statusBarColor == Color.BLACK && window.getNavigationBarColor() == Color.BLACK) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            } else {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            }*/
            MainActivity.this.getWindow().setStatusBarColor(getResources().getColor(statusBarColor));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            drawerlayout.openDrawer(GravityCompat.START);
            return true;
        }
        if (item.getItemId() == R.id.action_mode){
            isLight = !isLight;
            item.setTitle(isLight ? "夜间模式" : "日间模式");
            toolBar.setBackgroundColor(getResources().getColor(isLight ? R.color.light_toolbar : R.color.dark_toolbar));
            setStatusBarColor(isLight ? R.color.light_toolbar : R.color.dark_toolbar);
            if (curId.equals("latest")){
                ((MainFragment)getSupportFragmentManager().findFragmentByTag("latest")).updateTheme();
            }else {
                ((NewsFragment) getSupportFragmentManager().findFragmentByTag("news")).updateTheme();
            }
            updateLeftMenuTheme();
            sp.edit().putBoolean("isLight", isLight).commit();
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean isLight() {
        return isLight;
    }

    public void setToolbarTitle(String text) {
        toolBar.setTitle(text);
    }

    public void setSwipeRefreshEnable(boolean enable) {
        refreshLayout.setEnabled(enable);
    }

    public void updateLeftMenuTheme(){
//        ((NewsFragment)getSupportFragmentManager().findFragmentByTag("news")).updateTheme();
        refreshLayout.setColorSchemeResources(isLight ? android.R.color.holo_blue_dark : android.R.color.black);
        adapter.updateTheme();
        ll_menu.setBackgroundColor(getResources().getColor(isLight ? R.color.light_menu_header : R.color.dark_menu_header));
        tv_main.setBackgroundColor(getResources().getColor(isLight ? R.color.light_menu_index_background : R.color.dark_menu_index_background));
    }

}
