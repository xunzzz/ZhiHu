package com.zx.zhihu.bean;

import java.util.List;

/**
 * Created by zhangxun on 2015/9/24.
 */
public class News {
    private List<StoriesEntity> stories;
    private int color;
    private String description;
    private String name;
    private String background;
    private String image;
    private List<EditorsEntity> editors;
    private String image_source;

    public List<StoriesEntity> getStories() {
        return stories;
    }

    public void setStories(List<StoriesEntity> stories) {
        this.stories = stories;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<EditorsEntity> getEditors() {
        return editors;
    }

    public void setEditors(List<EditorsEntity> editors) {
        this.editors = editors;
    }

    public String getImage_source() {
        return image_source;
    }

    public void setImage_source(String image_source) {
        this.image_source = image_source;
    }
}
