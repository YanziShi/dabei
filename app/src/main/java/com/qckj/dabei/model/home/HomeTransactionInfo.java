package com.qckj.dabei.model.home;

import android.support.annotation.NonNull;

import com.qckj.dabei.util.json.JsonField;

/**
 * 交易信息
 * <p>
 * "F_C_NAME":"西餐",
 * "F_C_NICHENG":"燕子",
 * "finish":1552572894000
 * <p>
 * Created by yangzhizhong on 2019/3/23.
 */
public class HomeTransactionInfo {

    @JsonField("F_C_NAME")
    private String contentName;

    @JsonField("F_C_NICHENG")
    private String clientName;

    @JsonField("finish")
    private long finishDate;

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public long getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(long finishDate) {
        this.finishDate = finishDate;
    }

    @NonNull
    @Override
    public String toString() {
        return "HomeTransactionInfo{" +
                "contentName='" + contentName + '\'' +
                ", clientName='" + clientName + '\'' +
                ", finishDate=" + finishDate +
                '}';
    }
}
