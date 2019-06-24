package com.qckj.dabei.model.mine;

import com.qckj.dabei.util.json.JsonField;

/**
 * {
 * "F_C_ID":"dbs_2018080614451522",
 * "F_C_IMG":"http://dabei.oss-cn-shenzhen.aliyuncs.com/upload/system/20180806144503c0HtC5StLPlB4ncTn7.png",
 * "F_C_NAME":"通知",
 * "F_C_TIME":1541387518000,
 * "F_C_USID":"20180806095026HqSkT8gK9kv96Fw7YQ",
 * "F_C_XICONTENT":"国庆"
 * }
 * <p>
 * 消息实体类
 * <p>
 * Created by yangzhizhong on 2019/4/8.
 */
public class MessageInfo {

    @JsonField("F_C_ID")
    private String id;

    @JsonField("F_C_IMG")
    private String imageUrl;

    @JsonField("F_C_NAME")
    private String name;

    @JsonField("F_C_TIME")
    private long time;

    @JsonField("F_C_USID")
    private String userId;

    @JsonField("F_C_XICONTENT")
    private String content;
    @JsonField("message_count")
    int count;

    int type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
