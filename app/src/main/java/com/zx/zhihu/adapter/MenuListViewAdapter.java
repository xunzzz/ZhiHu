package com.zx.zhihu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zx.zhihu.R;
import com.zx.zhihu.activity.MainActivity;
import com.zx.zhihu.bean.NewsListItem;

import java.util.List;

/**
 * Created by zhangxun on 2015/9/23.
 */
public class MenuListViewAdapter extends BaseAdapter{

    private Context mContext;
    private List<NewsListItem> list;
    private boolean isLight = true;

    public MenuListViewAdapter(Context mContext, List<NewsListItem> list) {
        this.mContext = mContext;
        this.list = list;
        isLight = ((MainActivity) mContext).isLight();
    }

    @Override
    public int getCount() {
        if (null != list && list.size()>0){
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.menu_item, parent, false);
        }
        TextView tv_item = (TextView) convertView.findViewById(R.id.tv_item);
        ((RelativeLayout)tv_item.getParent()).setBackgroundColor(mContext.getResources().getColor(isLight ? R.color.light_menu_listview_background : R.color.dark_menu_listview_background));
        tv_item.setTextColor(mContext.getResources().getColor(isLight ? R.color.light_menu_listview_textcolor : R.color.dark_menu_listview_textcolor));
        tv_item.setText(list.get(position).getName());
        return convertView;
    }
    public void updateTheme() {
        isLight = ((MainActivity)mContext).isLight();
        notifyDataSetChanged();
    }
}
