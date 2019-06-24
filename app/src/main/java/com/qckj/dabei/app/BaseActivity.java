package com.qckj.dabei.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.qckj.dabei.app.http.AppHandler;
import com.qckj.dabei.util.log.Logger;
import com.qckj.dabei.view.dialog.LoadingProgressDialog;
import com.umeng.analytics.MobclickAgent;

/**
 * 所有activity的基类
 * <p>
 * Created by yangzhizhong on 2019/3/21.
 */
@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity implements AppHandler.HandlerMessageListener {

    /**
     * 日志打印， logger.d("a = %d", 3);
     */
    protected Logger logger = new Logger(getClass().getSimpleName());

    protected App app;
    private LoadingProgressDialog mCurrentProgressDialog;
    private boolean mIsFinish = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getInstance().injectManager(this);
        mIsFinish = false;
    }

    @Override
    public void finish() {
        super.finish();
        mIsFinish = true;
    }

    public BaseActivity getActivity() {
        return this;
    }

    public void showToast(@StringRes int resId) {
        showToast(getResources().getString(resId));
    }

    public void showToast(String message) {
        if (TextUtils.isEmpty(message))
            return;
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void showLoadingProgressDialog() {
        showLoadingProgressDialog(false);
    }

    public boolean isActivityFinished() {
        return mIsFinish;
    }

    public void showLoadingProgressDialog(boolean isCancelable) {
        if (isDestroyed()) {
            return;
        }
        if (mCurrentProgressDialog == null) {
            mCurrentProgressDialog = new LoadingProgressDialog(this);
        }
        mCurrentProgressDialog.setCancelable(isCancelable);
        if (!mCurrentProgressDialog.isShowing()) {
            try {
                mCurrentProgressDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void dismissLoadingProgressDialog() {
        if (mCurrentProgressDialog != null) {
            try {
                if (mCurrentProgressDialog.isShowing()) {
                    mCurrentProgressDialog.dismiss();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 返回
     *
     * @param view view
     */
    public void onBackClick(View view) {
        onBackPressed();
    }

    public static int dipToPx(Context context, float dip) {
        return (int) (context.getResources().getDisplayMetrics().density * dip + 0.5f);
    }

    @Override
    public void handleMessage(Message msg) {
        if (!isDestroyed())
            getActivity().handleMessage(msg);
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
