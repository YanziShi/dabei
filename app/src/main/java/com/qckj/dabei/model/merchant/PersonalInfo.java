package com.qckj.dabei.model.merchant;

import com.qckj.dabei.util.json.JsonField;

/**
 * Created by yangzhizhong on 2019/5/16.
 */
public class PersonalInfo {

    int ishhr;//是否合伙人（0=不是；1=是；）

    @JsonField("islxr")
    int isAddContact;   //是否已添加联系人（0=未添加；1=已添加）

    int zycount;       //资源库数量
    int messagecount;  //消息条数
    int isjpsj;        //1代表是，0代表不是金牌商家

    public int getIshhr() {
        return ishhr;
    }

    public void setIshhr(int ishhr) {
        this.ishhr = ishhr;
    }

    public int getIsAddContact() {
        return isAddContact;
    }

    public void setIsAddContact(int isAddContact) {
        this.isAddContact = isAddContact;
    }

    public int getZycount() {
        return zycount;
    }

    public void setZycount(int zycount) {
        this.zycount = zycount;
    }

    public int getMessagecount() {
        return messagecount;
    }

    public void setMessagecount(int messagecount) {
        this.messagecount = messagecount;
    }

    public int getIsjpsj() {
        return isjpsj;
    }

    public void setIsjpsj(int isjpsj) {
        this.isjpsj = isjpsj;
    }
}
