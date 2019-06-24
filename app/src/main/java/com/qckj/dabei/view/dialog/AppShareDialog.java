package com.qckj.dabei.view.dialog;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.manager.share.UmengShareEntryActivity;
import com.qckj.dabei.model.mine.AppShareInfo;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.ViewInject;
import com.qckj.dabei.view.webview.BrowserActivity;

/**
 * Created by yangzhizhong on 2019/4/15.
 */
public class AppShareDialog extends Dialog {

    @FindViewById(R.id.dialog_article_share_touch_area)
    private View view;
    @FindViewById(R.id.dialog_article_share_touch_liner)
    private LinearLayout linearLayout;
    @FindViewById(R.id.dialog_article_share_gr)
    private GridView mGridView;
    @FindViewById(R.id.dialog_share_cancel_btn)
    private TextView mCancelBtn;

    public String title;
    public String content;
    private String contentUrl;
    private String iconUrl;
    private int iconRec;
    private boolean isCard;
    private  String shareType;

    private Activity activity;

    private AppShareAdapter adapter;

    /**
     * 对item进行监听
     */
    private AdapterView.OnItemClickListener onItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            dismiss();
            if (position == 5 && isCard) {
                if (activity instanceof BrowserActivity) {
                    ((BrowserActivity) activity).loadH5Method("javascript:businessCard()");
                }
            } else {
                sendShare(adapter.getItem(position));
            }
        }
    };

    private View.OnClickListener onCancelClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };

    private String[] titles = new String[]
            {"QQ", "QQ空间", "微信", "朋友圈", "微博"};

    private int[] images = new int[]{
            R.mipmap.ic_app_share_qq, R.mipmap.ic_app_share_qzone,
            R.mipmap.ic_app_share_wx, R.mipmap.ic_app_share_wx_friends,
            R.mipmap.ic_app_share_weibo
    };

    private int[] platformTypes = new int[]{
            AppShareInfo.PLATFORM_QQ, AppShareInfo.PLATFORM_QZONE,
            AppShareInfo.PLATFORM_WX, AppShareInfo.PLATFORM_WX_CIRCLE,
            AppShareInfo.PLATFORM_SINA
    };

    public AppShareDialog(Activity context) {
        super(context, R.style.Translucent);
        activity = context;
    }

    public void includeCard(boolean isCard) {
        this.isCard = isCard;
        if (isCard) {
            titles = new String[]{"QQ", "QQ空间", "微信", "朋友圈", "微博", "生成名片"};
            images = new int[]{
                    R.mipmap.ic_app_share_qq, R.mipmap.ic_app_share_qzone,
                    R.mipmap.ic_app_share_wx, R.mipmap.ic_app_share_wx_friends,
                    R.mipmap.ic_app_share_weibo, R.mipmap.ic_app_share_card
            };
            platformTypes = new int[]{
                    AppShareInfo.PLATFORM_QQ, AppShareInfo.PLATFORM_QZONE,
                    AppShareInfo.PLATFORM_WX, AppShareInfo.PLATFORM_WX_CIRCLE,
                    AppShareInfo.PLATFORM_SINA, AppShareInfo.PLATFORM_MYSELF
            };
        }
    }

    /**
     * 获取图片（来自本地）
     *
     * @return
     */
    public int getIconRec() {
        return iconRec;
    }

    /**
     * 设置图片（来自本地）
     *
     * @param iconRec
     */
    public void setIconRec(int iconRec) {
        this.iconRec = iconRec;
    }

    /**
     * 获取标题
     *
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取内容
     *
     * @return
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置内容
     *
     * @param content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 获取内容的Url
     *
     * @return
     */
    public String getContentUrl() {
        return contentUrl;
    }

    /**
     * 设置内容的url
     *
     * @param contentUrl
     */
    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    /**
     * 获得图片的uRl
     *
     * @return
     */
    public String getIconUrl() {
        return iconUrl;
    }

    /**
     * 设置图片的URl
     *
     * @param iconUrl
     */
    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getShareType() {
        return shareType;
    }

    public void setShareType(String shareType) {
        this.shareType = shareType;
    }

    /**
     * 发送分享
     *
     * @param info 分享实体
     */
    public void sendShare(AppShareInfo info) {
        @AppShareInfo.PlatformType int platform = info.getType();
        if (iconRec != -1) {
            UmengShareEntryActivity.startActivity(activity, platform, title, content, contentUrl, iconRec,shareType);
        } else {
            if (iconUrl != null) {
                UmengShareEntryActivity.startActivity(activity, platform, title, content, contentUrl, iconUrl, shareType);
            } else {
                Toast.makeText(getContext(), "分享失败，请稍后再试！", Toast.LENGTH_SHORT).show();
            }
        }

        dismiss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_app_share);
        ViewInject.inject(this);

        setListener();
        adapter = new AppShareAdapter(images, titles, platformTypes, getContext());
        mGridView.setAdapter(adapter);
        mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mGridView.setOnItemClickListener(onItemClick);
        mCancelBtn.setOnClickListener(onCancelClickListener);
    }


    private void setListener() {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void show() {
        super.show();
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(linearLayout, "translationY", BaseActivity.dipToPx(getContext(), 135), 0);
        objectAnimator.setDuration(300);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.start();

        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(view, "alpha", 0.0f, 0.8f);
        objectAnimator1.setDuration(300);
        objectAnimator1.start();
    }

}
