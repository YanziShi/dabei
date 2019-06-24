package com.qckj.dabei.model.mine;


import android.support.annotation.IntDef;

public class AppShareInfo {

    /** 第三方平台 自己定义 */
    public static final int PLATFORM_MYSELF = 0;
    /** 第三方平台 新浪微博 */
    public static final int PLATFORM_SINA = 1;
    /** 第三方平台 微信 */
    public static final int PLATFORM_WX = 2;
    /** 第三方平台 QQ */
    public static final int PLATFORM_QQ = 3;

    /** 第三方平台 微信朋友圈 */
    public static final int PLATFORM_WX_CIRCLE = 4;

    /** 第三方平台 QQ */
    public static final int PLATFORM_QZONE = 5;

    private int imageId;

    private String title;

    @PlatformType
    private int type;

    public AppShareInfo() {
    }

    public AppShareInfo(int imageId, String title, @PlatformType int type) {
        this.imageId = imageId;
        this.title = title;
        this.type = type;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "AppShareInfo{" +
                "imageId=" + imageId +
                ", title='" + title + '\'' +
                ", type=" + type +
                '}';
    }

    @IntDef({PLATFORM_MYSELF,PLATFORM_SINA, PLATFORM_WX, PLATFORM_QQ, PLATFORM_WX_CIRCLE, PLATFORM_QZONE})
    public @interface PlatformType {
    }
}
