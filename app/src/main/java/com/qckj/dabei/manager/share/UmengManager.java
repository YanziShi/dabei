package com.qckj.dabei.manager.share;

import com.qckj.dabei.app.App;
import com.qckj.dabei.app.BaseManager;
import com.qckj.dabei.util.CommonUtils;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

/**
 * 友盟管理器
 */

public class UmengManager extends BaseManager {
    // 微信


    @Override
    public void onManagerCreate(App application) {
        PlatformConfig.setWeixin(CommonUtils.WX_APP_ID, CommonUtils.WX_APP_SECRET);
        PlatformConfig.setQQZone(CommonUtils.QQ_APP_ID, CommonUtils.QQ_APP_SECRET);
        UMShareAPI.get(application);

    }

}
