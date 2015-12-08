package com.zx.zhihu.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zx.zhihu.R;
import com.zx.zhihu.activity.MainActivity;
import com.zx.zhihu.activity.NewsContentActivity;
import com.zx.zhihu.bean.StoriesEntity;
import com.zx.zhihu.utils.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangxun on 2015/9/24.
 */
public class MainFragmentAdapter extends BaseAdapter{
    private List<StoriesEntity> list;
    private Context context;
    private boolean isLight;
    private ImageLoader mImageLoader;
    private DisplayImageOptions options;

    public MainFragmentAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<StoriesEntity>();
        mImageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        isLight = ((MainActivity) context).isLight();
    }

    public void addList(List<StoriesEntity> items) {
        this.list.addAll(items);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        if (null != list && list.size() > 0){
            return list.size();
        }
        return 0;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.main_news_item, parent, false);
            viewHolder.tv_topic = (TextView) convertView.findViewById(R.id.tv_topic);
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.iv_title = (ImageView) convertView.findViewById(R.id.iv_title);
            viewHolder.item_RL = (RelativeLayout) convertView.findViewById(R.id.item_RL);
            viewHolder.title_RL = (RelativeLayout) convertView.findViewById(R.id.title_RL);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ((RelativeLayout)viewHolder.item_RL.getParent()).setBackgroundColor(context.getResources().getColor(isLight ? R.color.light_news_item : R.color.dark_news_item));
        viewHolder.tv_topic.setTextColor(context.getResources().getColor(isLight ? R.color.light_news_topic : R.color.dark_news_topic));
        viewHolder.tv_title.setTextColor(context.getResources().getColor(isLight ? android.R.color.black : android.R.color.white));

        final StoriesEntity entity = list.get(position);
        if (entity.getType() == Constant.TOPIC){
            ((RelativeLayout) viewHolder.tv_topic.getParent()).setBackgroundColor(Color.TRANSPARENT);
            viewHolder.title_RL.setVisibility(View.GONE);
            viewHolder.tv_topic.setVisibility(View.VISIBLE);
            viewHolder.tv_topic.setText(entity.getTitle());
        } else {
            ((RelativeLayout) viewHolder.tv_title.getParent().getParent()).setBackgroundResource(isLight ? R.drawable.item_background_selector_light : R.drawable.item_background_selector_dark);
            viewHolder.tv_topic.setVisibility(View.GONE);
            viewHolder.title_RL.setVisibility(View.VISIBLE);
            viewHolder.tv_title.setText(entity.getTitle());
            if (null != entity.getImages()){
                mImageLoader.displayImage(entity.getImages().get(0), viewHolder.iv_title, options);
            }
        }
        viewHolder.title_RL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NewsContentActivity.class);
                intent.putExtra("entity", entity);
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    public void updateTheme() {
        isLight = ((MainActivity)context).isLight();
        notifyDataSetChanged();
    }

    public static class ViewHolder {
        TextView tv_topic;
        TextView tv_title;
        ImageView iv_title;
        RelativeLayout item_RL, title_RL;
    }
}
