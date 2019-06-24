package com.qckj.dabei.model.home;

import com.qckj.dabei.util.json.JsonField;

/**
 * {
 * "F_C_DPNAME":"紫薇汽车用品商行",
 * "F_C_FMIMG":"http://dabei.oss-cn-shenzhen.aliyuncs.com/WX/dianpu/20180721114921774-23F495B2-9115-4A4F-A1EF-A1A5709C59C9.jpeg",
 * "F_C_ID":"20180718141159YtQFOEDXAlXzdqZ1Yk",
 * "URL":"http://dabei.oss-cn-shenzhen.aliyuncs.com/"
 * }
 * <p>
 * 推荐服务信息
 * <p>
 * Created by yangzhizhong on 2019/4/10.
 */
public class RecommendServiceInfo {

    @JsonField("F_C_DPNAME")
    private String name;

    @JsonField("F_C_FMIMG")
    private String imgUrl;

    @JsonField("F_C_ID")
    private String id;

    @JsonField("URL")
    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
