package com.qckj.dabei.model.mine;

import android.support.annotation.IntDef;

/**
 * 等接口：还需要加上注解
 * <p>
 * Created by yangzhizhong on 2019/4/16.
 */
public class AuthInfo {

    public static final int AUTH_STATE_UN = 0;
    public static final int AUTH_STATE = 1;
    public static final int AUTH_STATE_ING = 2;

    private String imgUrl;
    private String name;
    private String content;
    private int authType;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @AuthState
    public int getAuthType() {
        return authType;
    }

    public void setAuthType(int authType) {
        this.authType = authType;
    }

    @IntDef({AUTH_STATE_UN, AUTH_STATE, AUTH_STATE_ING})
    public @interface AuthState {
    }
}
