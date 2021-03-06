package com.zx.zhihu.bean;

import java.io.Serializable;

/**
 * Created by zhangxun on 2015/9/24.
 */
public class TopStoriesEntity implements Serializable{
    private int id;
    private String title;
    private String ga_prefix;
    private String image;
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

    public String getGa_prefix() {
        return ga_prefix;
    }

    public void setGa_prefix(String ga_prefix) {
        this.ga_prefix = ga_prefix;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "TopStoriesEntity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", ga_prefix='" + ga_prefix + '\'' +
                ", image='" + image + '\'' +
                ", type=" + type +
                '}';
    }
}
