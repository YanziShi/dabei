package com.qckj.dabei.model.home;

import java.io.Serializable;
import java.util.List;

/**
 * @name: 梁永胜
 * @date ：2018/11/10 15:48
 * E-Mail Address：875450820@qq.com
 */
public class ServiceDetailInfo implements Serializable {

    /**
     * F_C_ADDRESS : 玉厂路284号家园内
     * F_C_ID : 20180723093810CsA1jlOJl5qFGr5qMr02a5Ds7mwozfkqHFonT3IJFg7slgsZmx
     * F_C_SPFMIMG : http://dabei.oss-cn-shenzhen.aliyuncs.com/WX/spgl/2018072393741763-8093D948-1376-41FF-AEAE-5E8C4F07D50A.jpeg
     * F_C_SPNAME : 啦啦啦
     * F_I_MONEY : 2558.0
     * F_TAB_SJ_MES_ID : 20180718141159YtQFOEDXAlXzdqZ1Yk
     * goods : [{"F_C_ID":"20180721114409LXRTypV18zALG1eobak96BwpanayfvSISa6HbKYnvVZgOPg4MA","F_C_SPFMIMG":"http://dabei.oss-cn-shenzhen.aliyuncs.com/WX/spgl/20180721114316520-1AD726B3-1FCA-4397-9005-E9F3601CC25E.jpeg","F_C_SPNAME":"5","F_I_MONEY":20}]
     */

    private String F_C_ADDRESS;
    private String F_C_ID;
    private String F_C_SPFMIMG;
    private String F_C_SPNAME;
    private double F_I_MONEY;
    private String F_TAB_SJ_MES_ID;
    private String message;
    private List<GoodsBean> goods;

    public String getF_C_ADDRESS() {
        return F_C_ADDRESS;
    }

    public void setF_C_ADDRESS(String F_C_ADDRESS) {
        this.F_C_ADDRESS = F_C_ADDRESS;
    }

    public String getF_C_ID() {
        return F_C_ID;
    }

    public void setF_C_ID(String F_C_ID) {
        this.F_C_ID = F_C_ID;
    }

    public String getF_C_SPFMIMG() {
        return F_C_SPFMIMG;
    }

    public void setF_C_SPFMIMG(String F_C_SPFMIMG) {
        this.F_C_SPFMIMG = F_C_SPFMIMG;
    }

    public String getF_C_SPNAME() {
        return F_C_SPNAME;
    }

    public void setF_C_SPNAME(String F_C_SPNAME) {
        this.F_C_SPNAME = F_C_SPNAME;
    }

    public double getF_I_MONEY() {
        return F_I_MONEY;
    }

    public void setF_I_MONEY(double F_I_MONEY) {
        this.F_I_MONEY = F_I_MONEY;
    }

    public String getF_TAB_SJ_MES_ID() {
        return F_TAB_SJ_MES_ID;
    }

    public void setF_TAB_SJ_MES_ID(String F_TAB_SJ_MES_ID) {
        this.F_TAB_SJ_MES_ID = F_TAB_SJ_MES_ID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<GoodsBean> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsBean> goods) {
        this.goods = goods;
    }

    public static class GoodsBean implements Serializable {
        /**
         * F_C_ID : 20180721114409LXRTypV18zALG1eobak96BwpanayfvSISa6HbKYnvVZgOPg4MA
         * F_C_SPFMIMG : http://dabei.oss-cn-shenzhen.aliyuncs.com/WX/spgl/20180721114316520-1AD726B3-1FCA-4397-9005-E9F3601CC25E.jpeg
         * F_C_SPNAME : 5
         * F_I_MONEY : 20.0
         */

        private String F_C_ID;
        private String F_C_SPFMIMG;
        private String F_C_SPNAME;
        private double F_I_MONEY;

        public String getF_C_ID() {
            return F_C_ID;
        }

        public void setF_C_ID(String F_C_ID) {
            this.F_C_ID = F_C_ID;
        }

        public String getF_C_SPFMIMG() {
            return F_C_SPFMIMG;
        }

        public void setF_C_SPFMIMG(String F_C_SPFMIMG) {
            this.F_C_SPFMIMG = F_C_SPFMIMG;
        }

        public String getF_C_SPNAME() {
            return F_C_SPNAME;
        }

        public void setF_C_SPNAME(String F_C_SPNAME) {
            this.F_C_SPNAME = F_C_SPNAME;
        }

        public double getF_I_MONEY() {
            return F_I_MONEY;
        }

        public void setF_I_MONEY(double F_I_MONEY) {
            this.F_I_MONEY = F_I_MONEY;
        }
    }
}
