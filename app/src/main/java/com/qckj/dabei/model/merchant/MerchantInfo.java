package com.qckj.dabei.model.merchant;

/**
 * Created by yangzhizhong on 2019/5/15.
 */
public class MerchantInfo {
    String F_C_ID;          //用户商家ID
    String F_C_FRNAME;      //法人姓名
    String F_C_FRIDNUMBER;  //身份证号码
    String F_I_STATE;       //状态：0=审核中；1=已通过；2=未通过
    String F_D_STATETIME;   //审核时间
    String F_C_SFZZIMG;     //身份证正面照
    String F_C_SFZFIMG;     //身份证反面照
    String F_C_SFZSCIMG;    //手持身份证照
    String F_C_YYZZIMG;     //营业执照照片
    String F_C_DPNAME;      //店铺名称/企业名称
    String F_C_NAME;        //类别名称/企业类型
    String F_I_MONEY;       //人均消费
    String state;           //企业状态：0=未上线售卖商品；1=已上线售卖商品
    String F_C_JJPHONE;     //店铺电话/企业电话
    String ADDRESS;         //注册地址
    String F_C_ADDRESS;     //店铺地址/详细地址
    String F_C_X;           //坐标X轴
    String F_C_Y;           //坐标Y轴
    String F_C_MESSAGE;     //店铺介绍/企业介绍
    String URL;             //商家官网/官网链接
    String F_C_FMIMG;       //店铺封面图/企业封面
    String realName;        //用户认证的姓名
    String F_C_PHONE;       //用户的手机号码
    int business_state;  //营业状态（1代表营业中，0代表休息中）
    int isjpsj;          //否是金牌商家(0代表不是，1代表是)
    int isdpzd;          //店铺是否置顶(0代表未置顶，1代表置顶)
    String member_name;  //会员等级名称

    public String getF_C_ID() {
        return F_C_ID;
    }

    public void setF_C_ID(String f_C_ID) {
        F_C_ID = f_C_ID;
    }

    public String getF_C_FRNAME() {
        return F_C_FRNAME;
    }

    public void setF_C_FRNAME(String f_C_FRNAME) {
        F_C_FRNAME = f_C_FRNAME;
    }

    public String getF_C_FRIDNUMBER() {
        return F_C_FRIDNUMBER;
    }

    public void setF_C_FRIDNUMBER(String f_C_FRIDNUMBER) {
        F_C_FRIDNUMBER = f_C_FRIDNUMBER;
    }

    public String getF_I_STATE() {
        return F_I_STATE;
    }

    public void setF_I_STATE(String f_I_STATE) {
        F_I_STATE = f_I_STATE;
    }

    public String getF_D_STATETIME() {
        return F_D_STATETIME;
    }

    public void setF_D_STATETIME(String f_D_STATETIME) {
        F_D_STATETIME = f_D_STATETIME;
    }

    public String getF_C_SFZZIMG() {
        return F_C_SFZZIMG;
    }

    public void setF_C_SFZZIMG(String f_C_SFZZIMG) {
        F_C_SFZZIMG = f_C_SFZZIMG;
    }

    public String getF_C_SFZFIMG() {
        return F_C_SFZFIMG;
    }

    public void setF_C_SFZFIMG(String f_C_SFZFIMG) {
        F_C_SFZFIMG = f_C_SFZFIMG;
    }

    public String getF_C_SFZSCIMG() {
        return F_C_SFZSCIMG;
    }

    public void setF_C_SFZSCIMG(String f_C_SFZSCIMG) {
        F_C_SFZSCIMG = f_C_SFZSCIMG;
    }

    public String getF_C_YYZZIMG() {
        return F_C_YYZZIMG;
    }

    public void setF_C_YYZZIMG(String f_C_YYZZIMG) {
        F_C_YYZZIMG = f_C_YYZZIMG;
    }

    public String getF_C_DPNAME() {
        return F_C_DPNAME;
    }

    public void setF_C_DPNAME(String f_C_DPNAME) {
        F_C_DPNAME = f_C_DPNAME;
    }

    public String getF_C_NAME() {
        return F_C_NAME;
    }

    public void setF_C_NAME(String f_C_NAME) {
        F_C_NAME = f_C_NAME;
    }

    public String getF_I_MONEY() {
        return F_I_MONEY;
    }

    public void setF_I_MONEY(String f_I_MONEY) {
        F_I_MONEY = f_I_MONEY;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getF_C_JJPHONE() {
        return F_C_JJPHONE;
    }

    public void setF_C_JJPHONE(String f_C_JJPHONE) {
        F_C_JJPHONE = f_C_JJPHONE;
    }

    public String getADDRESS() {
        return ADDRESS;
    }

    public void setADDRESS(String ADDRESS) {
        this.ADDRESS = ADDRESS;
    }

    public String getF_C_ADDRESS() {
        return F_C_ADDRESS;
    }

    public void setF_C_ADDRESS(String f_C_ADDRESS) {
        F_C_ADDRESS = f_C_ADDRESS;
    }

    public String getF_C_X() {
        return F_C_X;
    }

    public void setF_C_X(String f_C_X) {
        F_C_X = f_C_X;
    }

    public String getF_C_Y() {
        return F_C_Y;
    }

    public void setF_C_Y(String f_C_Y) {
        F_C_Y = f_C_Y;
    }

    public String getF_C_MESSAGE() {
        return F_C_MESSAGE;
    }

    public void setF_C_MESSAGE(String f_C_MESSAGE) {
        F_C_MESSAGE = f_C_MESSAGE;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getF_C_FMIMG() {
        return F_C_FMIMG;
    }

    public void setF_C_FMIMG(String f_C_FMIMG) {
        F_C_FMIMG = f_C_FMIMG;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getF_C_PHONE() {
        return F_C_PHONE;
    }

    public void setF_C_PHONE(String f_C_PHONE) {
        F_C_PHONE = f_C_PHONE;
    }

    public int getBusiness_state() {
        return business_state;
    }

    public void setBusiness_state(int business_state) {
        this.business_state = business_state;
    }

    public int getIsjpsj() {
        return isjpsj;
    }

    public void setIsjpsj(int isjpsj) {
        this.isjpsj = isjpsj;
    }

    public int getIsdpzd() {
        return isdpzd;
    }

    public void setIsdpzd(int isdpzd) {
        this.isdpzd = isdpzd;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }
}
