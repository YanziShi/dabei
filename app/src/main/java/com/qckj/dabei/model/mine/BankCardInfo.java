package com.qckj.dabei.model.mine;

import com.qckj.dabei.util.json.JsonField;

import java.io.Serializable;

/**
 * Created by yangzhizhong on 2019/5/27.
 */
public class BankCardInfo implements Serializable {

    /*"F_C_BLANK": "贵阳银行股份有限公司",
            "F_C_ID": "20190523135422DS3GrwWtktHRFrXGpP",
            "F_C_NUMBER": "6217359901022925879",
            "abbreviation": "GYYXGFYXGS",
            "bankgroundimage": "https:\/\/www.dabeiinfo.com\/bankimage\/background\/red.png",
            "blankimage": "https:\/\/www.dabeiinfo.com\/bankimage\/logo\/GYYXGFYXGS.png",
            "count": 3,
            "f_c_name": "李小甲"*/
    @JsonField("F_C_BLANK")
    String bankName;

    @JsonField("F_C_ID")
    String bankId;

    @JsonField("bankgroundimage")
    String bankBg;

    @JsonField("F_C_NUMBER")
    String bankNumber;

    @JsonField("blankimage")
    String bankImge;

    @JsonField("f_c_name")
    String name;

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getBankBg() {
        return bankBg;
    }

    public void setBankBg(String bankBg) {
        this.bankBg = bankBg;
    }

    public String getBankNumber() {
        return bankNumber;
    }

    public void setBankNumber(String bankNumber) {
        this.bankNumber = bankNumber;
    }

    public String getBankImge() {
        return bankImge;
    }

    public void setBankImge(String bankImge) {
        this.bankImge = bankImge;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
