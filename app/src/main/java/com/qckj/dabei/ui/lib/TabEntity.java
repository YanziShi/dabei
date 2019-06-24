package com.qckj.dabei.ui.lib;

import com.flyco.tablayout.listener.CustomTabEntity;

/**
 * Created by yangzhizhong on 2019/4/9.
 */
public class TabEntity implements CustomTabEntity {

    private int id;
    private String type;


    public TabEntity(int id, String type) {
        this.id = id;
        this.type = type;
    }

    @Override
    public String getTabTitle() {
        return type;
    }

    @Override
    public int getTabSelectedIcon() {
        return 0;
    }

    @Override
    public int getTabUnselectedIcon() {
        return 0;
    }
}
