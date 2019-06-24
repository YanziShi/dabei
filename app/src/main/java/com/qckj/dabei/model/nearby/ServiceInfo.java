package com.qckj.dabei.model.nearby;

import com.qckj.dabei.util.json.JsonField;

import java.io.Serializable;

/**
 * 行业分类信息
 * <p>
 * Created by yangzhizhong on 2019/3/26.
 */
public class ServiceInfo implements Serializable {
        @JsonField("shop_id")
        private String id;

        @JsonField("dpName")
        private String dpName;

        @JsonField("flname")
        private String name;

        @JsonField("dpphone")
        private String phone;

        @JsonField("juli")
        private String distance;

        @JsonField("dpfmImg")
        private String imgUrl;

        @JsonField("isjpsj")
        private int isGold;

        @JsonField("isdpzd")
        private int isTop;

        @JsonField("business_state")
        private int isRest;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDpName() {
            return dpName;
        }

        public void setDpName(String dpName) {
            this.dpName = dpName;
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

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public int getIsGold() {
            return isGold;
        }

        public void setIsGold(int isGold) {
            this.isGold = isGold;
        }

        public int getIsTop() {
            return isTop;
        }

        public void setIsTop(int isTop) {
            this.isTop = isTop;
        }

        public int getIsRest() {
            return isRest;
        }

        public void setIsRest(int isRest) {
            this.isRest = isRest;
        }
}
