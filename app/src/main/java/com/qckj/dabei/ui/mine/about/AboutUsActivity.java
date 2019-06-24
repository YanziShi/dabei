package com.qckj.dabei.ui.mine.about;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.qckj.dabei.BuildConfig;
import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.view.dialog.MsgDialog;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.OnClick;
import com.qckj.dabei.util.inject.ViewInject;
import com.qckj.dabei.view.webview.BrowserActivity;
import com.umeng.analytics.MobclickAgent;

/**
 * 关于我们
 * <p>
 * Created by yangzhizhong on 2019/4/8.
 */
public class AboutUsActivity extends BaseActivity {

    @FindViewById(R.id.version_name)
    private TextView versionName;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, AboutUsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ViewInject.inject(this);
        versionName.setText("版本"+BuildConfig.VERSION_NAME);
        versionName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast(String.valueOf(BuildConfig.VERSION_CODE));
            }
        });
    }

    @OnClick({R.id.app_website, R.id.app_service_phone})
    private void onClickView(View view) {
        switch (view.getId()) {
            case R.id.app_website:
                BrowserActivity.startActivity(getActivity(), "http://www.dabei.com/", "大贝信息技术有限公司");
                break;
            case R.id.app_service_phone:
                showPhoneDialog("0851-85555809");
                break;
        }
    }

    private void showPhoneDialog(String phone) {
        MsgDialog dialog = new MsgDialog(getActivity());
        dialog.show(phone,"","拨打",false);
        dialog.setOnPositiveListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                callPhone(phone);
            }
        });
    }

    private void callPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        getActivity().startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
