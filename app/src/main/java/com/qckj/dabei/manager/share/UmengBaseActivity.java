package com.qckj.dabei.manager.share;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.app.http.AppHandlerProxy;
import com.umeng.socialize.UMShareAPI;

/**
 * 社会化分享界面
 * Created by zdxing on 2015/10/14.
 */
public abstract class UmengBaseActivity extends BaseActivity {

    protected View rootView;
    protected ProgressDialog progressDialog;

    private boolean isCanClickGoBack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = new FrameLayout(this);
        setContentView(rootView);
        isCanClickGoBack = false;

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("请稍候...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                finish();
            }
        });
        AppHandlerProxy.postDelayed(new Runnable() {
            @Override
            public void run() {
                isCanClickGoBack = true;
            }
        }, 200);
        rootView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                rootView.getViewTreeObserver().removeOnPreDrawListener(this);
                init();
                return false;
            }
        });
    }

    @Override
    public void finish() {
        dismissDialog();
        super.finish();
    }

    private void init() {
        onInit();
    }

    protected abstract void onInit();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void dismissDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            try {
                progressDialog.dismiss();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        UMShareAPI.get(this).onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if (!isCanClickGoBack) {
            return;
        }
        super.onBackPressed();
    }
}
