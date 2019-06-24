package com.qckj.dabei.model.mine;

import com.qckj.dabei.util.json.JsonField;

import java.io.Serializable;

/**
 * Created by yangzhizhong on 2019/5/29.
 */
public class DeliveryAddressInfo implements Serializable {
    @JsonField("F_C_ADDRESS")
    String address;
    @JsonField("F_C_ID")
    String id;
    @JsonField("city")
    String city;
    @JsonField("F_C_NAME")
    String name;
    @JsonField("F_C_PHONE")
    String phone;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
