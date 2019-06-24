package com.qckj.dabei.manager.share;

import com.qckj.dabei.app.App;
import com.qckj.dabei.app.BaseManager;

import java.util.ArrayList;
import java.util.List;


/**
 * 分享管理类
 * <p>
 * Created by yangzhizhong
 */

public class ShareManager extends BaseManager {

    private List<OnShareListener> onShareListeners = new ArrayList<>();

    @Override
    public void onManagerCreate(App application) {

    }

    public void share() {
        for (OnShareListener onShareListener : onShareListeners) {
            onShareListener.onShareSuccess();
        }
    }

    public void addOnShareListener(OnShareListener onShareListener) {
        onShareListeners.add(onShareListener);
    }

    public void removeOnShareListener(OnShareListener onShareListener) {
        onShareListeners.remove(onShareListener);
    }

    public interface OnShareListener {
        void onShareSuccess();
    }
}
