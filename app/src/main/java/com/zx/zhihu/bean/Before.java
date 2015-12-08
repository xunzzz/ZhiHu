package com.zx.zhihu.bean;

import java.util.List;

/**
 * Created by zhangxun on 2015/9/24.
 */
public class Before {
    private String date;
    private List<StoriesEntity> stories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<StoriesEntity> getStories() {
        return stories;
    }

    public void setStories(List<StoriesEntity> stories) {
        this.stories = stories;
    }
}
