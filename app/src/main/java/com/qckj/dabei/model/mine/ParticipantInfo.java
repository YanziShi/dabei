package com.qckj.dabei.model.mine;

import com.qckj.dabei.util.json.JsonField;

/**
 * 参与人 on 2019/6/1.
 */
public class ParticipantInfo {
    @JsonField("F_TAB_USER_ID")
    String id;
    @JsonField("F_C_TXIMG")
    String img;
    @JsonField("F_C_PHONE")
    String phone;
    @JsonField("F_I_STA")
    String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
