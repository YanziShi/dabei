package com.qckj.dabei.manager.location;

import android.support.annotation.Nullable;

/**
 * Created by yangzhizhong on 2019/3/25.
 */
public interface OnLocationListener {

    /**
     * 定位完成的回调
     *
     * @param userLocationInfo 定位结果回调
     */
    void onLocationComplete(@Nullable UserLocationInfo userLocationInfo);

}
