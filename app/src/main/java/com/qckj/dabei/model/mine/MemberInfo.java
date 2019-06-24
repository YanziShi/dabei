package com.qckj.dabei.model.mine;

import com.qckj.dabei.util.json.JsonField;

/**
 * 加入合伙人信息
 * <p>
 * Created by yangzhizhong on 2019/4/11.
 */
public class MemberInfo {
    @JsonField("F_C_ID")
    private String id;

    @JsonField("member_name")
    private String memberName;

    @JsonField("member_price")
    private String memberPrice;

    @JsonField("member_introduce")
    private String memberIntroduce;

    @JsonField("member_logo")
    private String memberLogo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberPrice() {
        return memberPrice;
    }

    public void setMemberPrice(String memberPrice) {
        this.memberPrice = memberPrice;
    }

    public String getMemberIntroduce() {
        return memberIntroduce;
    }

    public void setMemberIntroduce(String memberIntroduce) {
        this.memberIntroduce = memberIntroduce;
    }

    public String getMemberLogo() {
        return memberLogo;
    }

    public void setMemberLogo(String memberLogo) {
        this.memberLogo = memberLogo;
    }
}
