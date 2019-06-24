package com.qckj.dabei.model;

import com.qckj.dabei.util.json.JsonField;

/**
 * {
 * "title": '',
 * "link": '',
 * "iconUrl": '',
 * "describe": '',
 * }
 * <p>
 * 分享app信息
 * <p>
 * Created by yangzhizhong on 2019/4/15.
 */
public class SharedAppInfo {

    private String title;

    private String link;

    private String iconUrl;

    private String describe;

    private boolean isCard;

    //img;
    String shareType;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public boolean isCard() {
        return isCard;
    }

    public void setCard(boolean card) {
        isCard = card;
    }

    public String getShareType() {
        return shareType;
    }

    public void setShareType(String shareType) {
        this.shareType = shareType;
    }
}
