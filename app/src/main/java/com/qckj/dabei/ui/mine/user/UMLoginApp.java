package com.qckj.dabei.ui.mine.user;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.app.http.OnResult;
import com.qckj.dabei.util.log.Logger;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.utils.SocializeUtils;

import java.util.Map;

/**
 * 第三方平台登录app
 * <p>
 * Created by yangzhizhong on 2019/3/28.
 */
public class UMLoginApp {

    public static void loginApp(Activity activity, SHARE_MEDIA shareMedia, OnResult<Map<String, String>> onResult) {
        UMShareAPI.get(activity).getPlatformInfo(activity, shareMedia, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                onResult.onResult(true, map);
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                String errorMsg = throwable.getLocalizedMessage();
                String[] msgArray = errorMsg.split(" ");
                String[] error = null;
                for (String s : msgArray) {
                    String[] e = s.split("：");
                    if (e.length != 0) {
                        if (e[0].equals("错误码")) {
                            error = e;
                            break;
                        }
                    }
                }
                if (error == null) {
                    Toast.makeText(activity,"分享失败，请稍后再试！",Toast.LENGTH_SHORT).show();
                    onResult.onResult(false, null);
                    return;
                }
                String errorCode = error[1];
                switch (errorCode) {
                    case "2008":
                        if (share_media == SHARE_MEDIA.WEIXIN
                                || share_media == SHARE_MEDIA.WEIXIN_CIRCLE
                                || share_media == SHARE_MEDIA.WEIXIN_FAVORITE) {
                            Toast.makeText(activity,"微信未安装!",Toast.LENGTH_SHORT).show();
                        } else if (share_media == SHARE_MEDIA.QQ
                                || share_media == SHARE_MEDIA.QZONE) {
                            Toast.makeText(activity,"QQ未安装!",Toast.LENGTH_SHORT).show();
                        }
                        break;
                    default:
                        Toast.makeText(activity,"登录失败，请稍后再试！",Toast.LENGTH_SHORT).show();
                        break;
                }
                onResult.onResult(false, null);
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                onResult.onResult(false, null);
            }

        });


    }

}
