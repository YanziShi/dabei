package com.qckj.dabei.model.home;

import android.support.annotation.NonNull;

import com.qckj.dabei.util.json.JsonField;


/**
 * 首页banner信息
 * <p>
 * Created by yangzhizhong on 2019/3/23.
 */
public class HomeBannerInfo {

    @JsonField("IMG")
    private String imgUrl;

    @JsonField("URL")
    private String url;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @NonNull
    @Override
    public String toString() {
        return "HomeBannerInfo{" +
                "imgUrl='" + imgUrl + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
