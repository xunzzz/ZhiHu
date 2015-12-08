package com.zx.zhihu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zx.zhihu.R;
import com.zx.zhihu.activity.MainActivity;
import com.zx.zhihu.bean.StoriesEntity;

import java.util.List;

/**
 * Created by zhangxun on 2015/9/24.
 */
public class NewsFragmentAdapter extends BaseAdapter{
    private List<StoriesEntity> list;
    private Context context;
    private boolean isLight;
    private ImageLoader mImageLoader;
    private DisplayImageOptions options;

    public NewsFragmentAdapter(Context context, List<StoriesEntity> list) {
        this.context = context;
        this.list = list;
        mImageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        isLight = ((MainActivity) context).isLight();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public StoriesEntity getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (null == convertView){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.news_item, parent, false);
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.iv_title = (ImageView) convertView.findViewById(R.id.iv_title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        StoriesEntity storiesEntity = list.get(position);
        ((RelativeLayout)viewHolder.tv_title.getParent().getParent()).setBackgroundResource(isLight ? R.drawable.item_background_selector_light : R.drawable.item_background_selector_dark);
        ((RelativeLayout)viewHolder.tv_title.getParent().getParent().getParent()).setBackgroundColor(context.getResources().getColor(isLight ? R.color.light_news_item : R.color.dark_news_item));
        viewHolder.tv_title.setTextColor(context.getResources().getColor(isLight ? android.R.color.black : android.R.color.white));
        viewHolder.tv_title.setText(storiesEntity.getTitle());
        if (null != storiesEntity.getImages()){
            viewHolder.iv_title.setVisibility(View.VISIBLE);
            mImageLoader.displayImage(storiesEntity.getImages().get(0), viewHolder.iv_title, options);
        }else {
            viewHolder.iv_title.setVisibility(View.GONE);
        }
        return convertView;
    }

    public void updateTheme() {
        isLight = ((MainActivity)context).isLight();
        notifyDataSetChanged();
    }


    public static class ViewHolder {
        TextView tv_title;
        ImageView iv_title;
    }
}
