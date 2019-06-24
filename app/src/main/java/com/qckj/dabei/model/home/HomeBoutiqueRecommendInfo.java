package com.qckj.dabei.model.home;

import android.support.annotation.NonNull;

import com.qckj.dabei.util.json.JsonField;

/**
 * "F_C_ID": "dbs_2018050810520997",
 * "F_C_IMG": "http://dabei.oss-cn-shenzhen.aliyuncs.com/upload/system/20180508110659wakY9fZXgRRreUbEEP.png",
 * "category": "汽车"
 * <p>
 * 首页精品推荐信息
 * <p>
 * Created by yangzhizhong on 2019/3/25.
 */
public class HomeBoutiqueRecommendInfo {

    @JsonField("F_C_ID")
    private String id;

    @JsonField("activity_introduce")
    private String introduce;

    @JsonField("activity_photo")
    private String photo;

    @JsonField("activity_title")
    private String title;

    @JsonField("activity_start_time")
    private String startTime;

    @JsonField("activity_end_time")
    private String endTime;

    @JsonField("activity_url")
    private String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
