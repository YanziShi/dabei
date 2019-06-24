package com.qckj.dabei.model.comment;

import com.qckj.dabei.util.json.JsonField;

/**
 * F_C_ID : 2018111016273527zhphQWDYbfHqFeU5
 * F_C_MESSAGE : 好好aaaa
 * F_C_PHONE : 15761644014
 * F_C_TXIMG :
 * F_D_TIME : 1.541838343E12
 * <p>
 * 评论信息
 * <p>
 * Created by yangzhizhong on 2019/4/10.
 */
public class CommentInfo {
    @JsonField("F_C_ID")
    private String id;

    @JsonField("F_C_MESSAGE")
    private String message;

    @JsonField("F_C_PHONE")
    private String phone;

    @JsonField("F_C_TXIMG")
    private String imageUrl;

    @JsonField("F_D_TIME")
    private long time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
