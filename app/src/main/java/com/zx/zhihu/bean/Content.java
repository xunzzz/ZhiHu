package com.zx.zhihu.bean;

import java.util.List;

/**
 * Created by zhangxun on 2015/9/24.
 */
public class Content {
    private int id;
    private List<RecommendersEntity> recommenders;
    private String body;
    private String title;
    private String ga_prefix;
    private String share_url;
    private String image;
    private int type;
    private List<String> css;
    private String image_source;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<RecommendersEntity> getRecommenders() {
        return recommenders;
    }

    public void setRecommenders(List<RecommendersEntity> recommenders) {
        this.recommenders = recommenders;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
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

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
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

    public List<String> getCss() {
        return css;
    }

    public void setCss(List<String> css) {
        this.css = css;
    }

    public String getImage_source() {
        return image_source;
    }

    public void setImage_source(String image_source) {
        this.image_source = image_source;
    }
}
