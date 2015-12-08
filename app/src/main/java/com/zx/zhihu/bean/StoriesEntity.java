package com.zx.zhihu.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangxun on 2015/9/24.
 */
public class StoriesEntity implements Serializable{

    private int id;
    private String title;
    private List<String> images;
    private int type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "StoriesEntity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", images=" + images +
                ", type=" + type +
                '}';
    }
}
