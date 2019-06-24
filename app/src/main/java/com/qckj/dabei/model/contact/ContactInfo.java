package com.qckj.dabei.model.contact;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * 联系人信息
 * <p>
 * Created by yangzhizhong on 2019/3/20.
 */
public class ContactInfo {

    String F_C_ID;
    String emergency_linkman;
    String emergency_linkman_phone;

    public String getF_C_ID() {
        return F_C_ID;
    }

    public void setF_C_ID(String f_C_ID) {
        F_C_ID = f_C_ID;
    }

    public String getEmergency_linkman() {
        return emergency_linkman;
    }

    public void setEmergency_linkman(String emergency_linkman) {
        this.emergency_linkman = emergency_linkman;
    }

    public String getEmergency_linkman_phone() {
        return emergency_linkman_phone;
    }

    public void setEmergency_linkman_phone(String emergency_linkman_phone) {
        this.emergency_linkman_phone = emergency_linkman_phone;
    }
}
