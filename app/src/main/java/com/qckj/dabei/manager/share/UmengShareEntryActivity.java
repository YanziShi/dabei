package com.qckj.dabei.manager.share;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RawRes;
import android.text.TextUtils;
import com.qckj.dabei.model.mine.AppShareInfo;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.ViewInject;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.utils.SocializeUtils;

import static com.qckj.dabei.model.mine.AppShareInfo.PLATFORM_QQ;
import static com.qckj.dabei.model.mine.AppShareInfo.PLATFORM_QZONE;
import static com.qckj.dabei.model.mine.AppShareInfo.PLATFORM_SINA;
import static com.qckj.dabei.model.mine.AppShareInfo.PLATFORM_WX;
import static com.qckj.dabei.model.mine.AppShareInfo.PLATFORM_WX_CIRCLE;

/** 友盟分享 */

public class UmengShareEntryActivity extends UmengBaseActivity {
    private boolean onStart = false;

    @Manager
    private ShareManager shareManager;

    private UMShareListener shareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            if (isActivityFinished()) {
                return;
            }
            SocializeUtils.safeShowDialog(progressDialog);
        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            if (isActivityFinished()) {
                return;
            }
            SocializeUtils.safeCloseDialog(progressDialog);
            setResult(RESULT_OK);
            showToast("分享成功");
            shareManager.share();
            finish();
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            if (isActivityFinished()) {
                return;
            }
            SocializeUtils.safeCloseDialog(progressDialog);
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
                showToast("分享失败，请稍后再试！");
                return;
            }
            String errorCode = error[1];
            switch (errorCode) {
                case "2008":
                    if (share_media == SHARE_MEDIA.WEIXIN
                            || share_media == SHARE_MEDIA.WEIXIN_CIRCLE
                            || share_media == SHARE_MEDIA.WEIXIN_FAVORITE) {
                        showToast("微信未安装");
                    } else if (share_media == SHARE_MEDIA.QQ
                            || share_media == SHARE_MEDIA.QZONE) {
                        showToast("QQ未安装");
                    } else if (share_media == SHARE_MEDIA.SINA) {
                        showToast("微博未安装");
                    }
                    break;
                default:
                    showToast("分享失败，请稍后再试！");
                    break;
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            if (isActivityFinished()) {
                return;
            }
            SocializeUtils.safeCloseDialog(progressDialog);
            //showToast("取消分享");
        }
    };
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 0) {
                finish();
            }
            return false;
        }
    });
    private int platform;


    public static void startActivity(Activity activity, @AppShareInfo.PlatformType int platform, String title, String content, String contentUrl, String iconUrl, String shareType) {
        if (content == null || title == null || contentUrl == null || iconUrl == null) {
            throw new NullPointerException("content == null || title == null || contentUrl == null || iconUrl == null");
        }

        Intent intent = new Intent(activity, UmengShareEntryActivity.class);
        intent.putExtra("platform", platform);
        intent.putExtra("title", title);
        intent.putExtra("content", content);
        intent.putExtra("contentUrl", contentUrl);
        intent.putExtra("iconUrl", iconUrl);
        intent.putExtra("shareType", shareType);
        activity.startActivity(intent);
    }

    public static void startActivity(Activity activity, @AppShareInfo.PlatformType int platform, String title, String content, String contentUrl, @RawRes int iconRes, String shareType) {
        if (content == null || title == null || contentUrl == null) {
            throw new NullPointerException("content == null || title == null || contentUrl == null");
        }

        Intent intent = new Intent(activity, UmengShareEntryActivity.class);
        intent.putExtra("platform", platform);
        intent.putExtra("title", title);
        intent.putExtra("content", content);
        intent.putExtra("contentUrl", contentUrl);
        intent.putExtra("iconRes", iconRes);
        intent.putExtra("shareType", shareType);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewInject.inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 修复BUG 分享开始后  回调不产生的BUG
        if (onStart) {
            handler.sendEmptyMessageDelayed(0, 10);
        }
    }

    private SHARE_MEDIA getShareMedia(int platform) {
        SHARE_MEDIA platformEnum;
        if (platform == PLATFORM_SINA) {
            platformEnum = SHARE_MEDIA.SINA;
        } else if (platform == PLATFORM_WX) {
            platformEnum = SHARE_MEDIA.WEIXIN;
        } else if (platform == PLATFORM_QQ) {
            platformEnum = SHARE_MEDIA.QQ;
        } else if (platform == PLATFORM_WX_CIRCLE) {
            platformEnum = SHARE_MEDIA.WEIXIN_CIRCLE;
        } else if (platform == PLATFORM_QZONE) {
            platformEnum = SHARE_MEDIA.QZONE;
        } else {
            throw new IllegalArgumentException("请传入要登录的平台！");
        }
        return platformEnum;
    }

    @Override
    protected void onInit() {
        if (isActivityFinished()) {
            return;
        }

        platform = getIntent().getIntExtra("platform", -1);
        final String title = getIntent().getStringExtra("title");
        final String content = getIntent().getStringExtra("content");
        final String contentUrl = getIntent().getStringExtra("contentUrl");
        final String iconUrl = getIntent().getStringExtra("iconUrl");
        final int iconRes = getIntent().getIntExtra("iconRes", -1);
        final String shareType = getIntent().getStringExtra("shareType");

        startShare(platform, title, content, contentUrl, iconUrl, iconRes,shareType);
    }

    private void startShare(int platform, String title, String content, String contentUrl, String iconUrl, int iconRes,String shareType) {
        onStart = true;
        if(!TextUtils.isEmpty(shareType) && shareType.equals("img")){
            UMImage image = new UMImage(UmengShareEntryActivity.this, contentUrl);//网络图片
            ShareAction shareAction = new ShareAction(UmengShareEntryActivity.this)
                    .withMedia(image)
                    .withText(title)
                    .setPlatform(getShareMedia(platform))
                    .setCallback(shareListener);
            shareAction.share();
        }else{UMImage uMediaObject;
            if (!TextUtils.isEmpty(iconUrl)) {
                uMediaObject = new UMImage(getActivity(), iconUrl);
            } else if (iconRes != -1) {
                uMediaObject = new UMImage(getActivity(), iconRes);
            } else {
                throw new RuntimeException("iconUrl == null || iconRes == -1");
            }
            UMWeb mWeb = new UMWeb(contentUrl);
            mWeb.setTitle(title);
            mWeb.setDescription(content);
            mWeb.setThumb(uMediaObject);
            ShareAction shareAction = new ShareAction(UmengShareEntryActivity.this)
                    .withMedia(mWeb)
                    .setPlatform(getShareMedia(platform))
                    .setCallback(shareListener);
            shareAction.share();
        }

    }

}
