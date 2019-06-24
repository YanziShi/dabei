package com.qckj.dabei.model.mine;

/**
 * Created by yangzhizhong on 2019/6/10.
 */
public class UserByIdInfo {
    String F_C_ID;
    //角色：1=普通用户；2=商家；3=代理商
    int F_I_ROLE;
    //状态：1=正常；2=异常；3=封号
    int F_I_STATE;
    String F_C_PWD;
    String F_C_UC;
    String F_C_TXIMG;
    String F_C_NICHENG;
    //性别:0=未知；1=男；2=女
    int F_I_SEX;
    //实名认证：0=未认证；1=已认证
    int F_I_RZSM;
    //企业商家认证状态：0=未认证；1=企业商家已认证；2=审核中
    int F_I_RZSJ_QY;
    //代理商认证状态：0=未认证；1=已认证
    int F_I_RZDLS;
    double F_C_BALANCE;
    int POINT;
    //绑定手机状态：0=未绑定；1=已绑定
    int F_I_RZPHONE;
    //密码状态：0=未设置密码；1=已设置密码
    int passwordState;
    int count;
    String F_C_BLANK;
    String F_C_NUMBER;
    String F_C_NAME;
    //个人商家认证状态：0=未认证；1=个人商家已认证；2=审核中
    int F_I_RZSJ_GR;
    //0普通会员,1代表白银,2代表金牌, 3代表砖石
    int member_grade;
    //0,1
    int isjpsj;

    public String getF_C_ID() {
        return F_C_ID;
    }

    public void setF_C_ID(String f_C_ID) {
        F_C_ID = f_C_ID;
    }

    public int getF_I_ROLE() {
        return F_I_ROLE;
    }

    public void setF_I_ROLE(int f_I_ROLE) {
        F_I_ROLE = f_I_ROLE;
    }

    public int getF_I_STATE() {
        return F_I_STATE;
    }

    public void setF_I_STATE(int f_I_STATE) {
        F_I_STATE = f_I_STATE;
    }

    public String getF_C_PWD() {
        return F_C_PWD;
    }

    public void setF_C_PWD(String f_C_PWD) {
        F_C_PWD = f_C_PWD;
    }

    public String getF_C_PHONE() {
        return F_C_UC;
    }

    public void setF_C_PHONE(String f_C_PHONE) {
        F_C_UC = f_C_PHONE;
    }

    public String getF_C_TXIMG() {
        return F_C_TXIMG;
    }

    public void setF_C_TXIMG(String f_C_TXIMG) {
        F_C_TXIMG = f_C_TXIMG;
    }

    public String getF_C_NICHENG() {
        return F_C_NICHENG;
    }

    public void setF_C_NICHENG(String f_C_NICHENG) {
        F_C_NICHENG = f_C_NICHENG;
    }

    public int getF_I_SEX() {
        return F_I_SEX;
    }

    public void setF_I_SEX(int f_I_SEX) {
        F_I_SEX = f_I_SEX;
    }

    public int getF_I_RZSM() {
        return F_I_RZSM;
    }

    public void setF_I_RZSM(int f_I_RZSM) {
        F_I_RZSM = f_I_RZSM;
    }

    public int getF_I_RZSJ_QY() {
        return F_I_RZSJ_QY;
    }

    public void setF_I_RZSJ_QY(int f_I_RZSJ_QY) {
        F_I_RZSJ_QY = f_I_RZSJ_QY;
    }

    public int getF_I_RZDLS() {
        return F_I_RZDLS;
    }

    public void setF_I_RZDLS(int f_I_RZDLS) {
        F_I_RZDLS = f_I_RZDLS;
    }

    public double getF_C_BALANCE() {
        return F_C_BALANCE;
    }

    public void setF_C_BALANCE(double f_C_BALANCE) {
        F_C_BALANCE = f_C_BALANCE;
    }

    public int getPOINT() {
        return POINT;
    }

    public void setPOINT(int POINT) {
        this.POINT = POINT;
    }

    public int getF_I_RZPHONE() {
        return F_I_RZPHONE;
    }

    public void setF_I_RZPHONE(int f_I_RZPHONE) {
        F_I_RZPHONE = f_I_RZPHONE;
    }

    public int getPasswordState() {
        return passwordState;
    }

    public void setPasswordState(int passwordState) {
        this.passwordState = passwordState;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getF_C_BLANK() {
        return F_C_BLANK;
    }

    public void setF_C_BLANK(String f_C_BLANK) {
        F_C_BLANK = f_C_BLANK;
    }

    public String getF_C_NUMBER() {
        return F_C_NUMBER;
    }

    public void setF_C_NUMBER(String f_C_NUMBER) {
        F_C_NUMBER = f_C_NUMBER;
    }

    public String getF_C_NAME() {
        return F_C_NAME;
    }

    public void setF_C_NAME(String F_C_NAME) {
        this.F_C_NAME = F_C_NAME;
    }

    public int getF_I_RZSJ_GR() {
        return F_I_RZSJ_GR;
    }

    public void setF_I_RZSJ_GR(int f_I_RZSJ_GR) {
        F_I_RZSJ_GR = f_I_RZSJ_GR;
    }

    public int getMember_grade() {
        return member_grade;
    }

    public void setMember_grade(int member_grade) {
        this.member_grade = member_grade;
    }

    public int getIsjpsj() {
        return isjpsj;
    }

    public void setIsjpsj(int isjpsj) {
        this.isjpsj = isjpsj;
    }
}
