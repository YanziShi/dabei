package com.qckj.dabei.model;

import java.io.Serializable;
import java.util.List;

/**
 * @name: 梁永胜
 * @date ：2018/12/3 14:44
 * E-Mail Address：875450820@qq.com
 */
public class CityBean implements Serializable {

    /**
     * name : 北京市
     * cityList : [{"name":"北京市","cityList":[{"name":"东城区","id":"110101"},{"name":"西城区","id":"110102"},{"name":"朝阳区","id":"110105"},{"name":"丰台区","id":"110106"},{"name":"石景山区","id":"110107"},{"name":"海淀区","id":"110108"},{"name":"门头沟区","id":"110109"},{"name":"房山区","id":"110111"},{"name":"通州区","id":"110112"},{"name":"顺义区","id":"110113"},{"name":"昌平区","id":"110114"},{"name":"大兴区","id":"110115"},{"name":"怀柔区","id":"110116"},{"name":"平谷区","id":"110117"},{"name":"密云区","id":"110118"},{"name":"延庆区","id":"110119"}],"id":"110100"}]
     * id : 110000
     */

    private String name;
    private String id;
    private List<CityListBeanX> cityList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<CityListBeanX> getCityList() {
        return cityList;
    }

    public void setCityList(List<CityListBeanX> cityList) {
        this.cityList = cityList;
    }

    public static class CityListBeanX implements Serializable {
        /**
         * name : 北京市
         * cityList : [{"name":"东城区","id":"110101"},{"name":"西城区","id":"110102"},{"name":"朝阳区","id":"110105"},{"name":"丰台区","id":"110106"},{"name":"石景山区","id":"110107"},{"name":"海淀区","id":"110108"},{"name":"门头沟区","id":"110109"},{"name":"房山区","id":"110111"},{"name":"通州区","id":"110112"},{"name":"顺义区","id":"110113"},{"name":"昌平区","id":"110114"},{"name":"大兴区","id":"110115"},{"name":"怀柔区","id":"110116"},{"name":"平谷区","id":"110117"},{"name":"密云区","id":"110118"},{"name":"延庆区","id":"110119"}]
         * id : 110100
         */

        private String name;
        private String id;
        private List<CityListBean> cityList;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List<CityListBean> getCityList() {
            return cityList;
        }

        public void setCityList(List<CityListBean> cityList) {
            this.cityList = cityList;
        }

        public static class CityListBean implements Serializable {
            /**
             * name : 东城区
             * id : 110101
             */

            private String name;
            private String id;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }
        }
    }
}
