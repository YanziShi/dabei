package com.qckj.dabei.view.webview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.manager.location.GaoDeLocationManager;
import com.qckj.dabei.model.SharedAppInfo;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.ViewInject;
import com.qckj.dabei.view.ActionBar;
import com.qckj.dabei.view.dialog.AppShareDialog;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

/**
 * 内置浏览器
 * <p>
 * Created by yangzhizhong on 2019/4/9.
 */
public class BrowserActivity extends BaseActivity {
    public static String URL_NAME = "url_name";
    public static String TITLE = "title";
    public static String IS_SHOW_ACTION_BAR = "is_show_action_bar";
    public static String IS_SHOW_SHARE_BUTTON = "is_show_share_button";

    @FindViewById(R.id.activity_browser_actionbar)
    private ActionBar actionBar;

    @FindViewById(R.id.custom_webView_progress)
    private ProgressBar progressBar;

    @FindViewById(R.id.custom_webView)
    public WebView webView;

    private JsInterface mJsInterface;

    private String title;
    private String mUrl;
    private boolean isShowActionBar;
    private boolean isShowShareButton;
    public static String orderType;

    String mData;

    public static void startActivity(Context context, String url, String title, boolean isShowShareButton,String content) {
        Intent lIntent = new Intent(context, BrowserActivity.class);
        lIntent.putExtra(URL_NAME, url);
        lIntent.putExtra(TITLE, title);
        lIntent.putExtra(IS_SHOW_SHARE_BUTTON, isShowShareButton);
        lIntent.putExtra("content",content);
        context.startActivity(lIntent);
    }

    public static void startActivity(Context context, String url, boolean isShowActionBar) {
        Intent lIntent = new Intent(context, BrowserActivity.class);
        lIntent.putExtra(URL_NAME, url);
        lIntent.putExtra(IS_SHOW_ACTION_BAR, isShowActionBar);
        context.startActivity(lIntent);
    }

    public static void startActivity(Context context, String url, String title,String orderType) {
        Intent lIntent = new Intent(context, BrowserActivity.class);
        lIntent.putExtra(URL_NAME, url);
        lIntent.putExtra("orderType", orderType);
        lIntent.putExtra(TITLE, title);
        context.startActivity(lIntent);
    }

    public static void startActivity(Context context, String url, String title) {
        Intent lIntent = new Intent(context, BrowserActivity.class);
        lIntent.putExtra(URL_NAME, url);
        lIntent.putExtra(TITLE, title);
        context.startActivity(lIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        ViewInject.inject(this);
        initView();
        initData();
        webView.loadUrl(mUrl);
    }

    private void initView() {
        mUrl = getIntent().getStringExtra(URL_NAME);
        title = getIntent().getStringExtra(TITLE);
        orderType = getIntent().getStringExtra("orderType");
        isShowActionBar = getIntent().getBooleanExtra(IS_SHOW_ACTION_BAR, true);
        isShowShareButton = getIntent().getBooleanExtra(IS_SHOW_SHARE_BUTTON, false);
        actionBar.setTitleText(title);
        actionBar.setVisibility(isShowActionBar ? View.VISIBLE : View.GONE);
        actionBar.setRightBtnShow(isShowShareButton);
        actionBar.setOnActionBarClickListener(new ActionBar.OnActionBarClickListener() {
            @Override
            public boolean onActionBarClick(int function) {
                if(function == ActionBar.FUNCTION_BUTTON_RIGHT){
                    SharedAppInfo sharedAppInfo = new SharedAppInfo();
                    sharedAppInfo.setCard(false);
                    sharedAppInfo.setTitle(title);
                    sharedAppInfo.setDescribe(getIntent().getStringExtra("content"));
                    sharedAppInfo.setLink(mUrl);
                    AppShareDialog dialog = new AppShareDialog(getActivity());
                    dialog.setContent(sharedAppInfo.getDescribe());
                    dialog.setTitle(sharedAppInfo.getTitle());
                    dialog.setContentUrl(sharedAppInfo.getLink());
                    dialog.setIconRec(R.mipmap.ic_launcher);
                    dialog.setIconUrl(sharedAppInfo.getIconUrl());
                    dialog.includeCard(sharedAppInfo.isCard());
                    dialog.show();
                }
                return false;
            }
        });
    }

    private void initData() {
        webView.getSettings().setDefaultTextEncodingName("UTF-8");
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        mJsInterface = new JsInterface(this);
        mJsInterface.setGoCarListner(new JsInterface.GoCarListner() {
            @Override
            public void goCar(String data) {
                loadH5Method(SystemConfig.carUrl);
                mData = data;
            }
        });
        webView.addJavascriptInterface(mJsInterface, "JavaScriptInterface");
        webView.addJavascriptInterface(mJsInterface, "android");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                webView.setVisibility(View.GONE);
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (progressBar == null) {
                    return;
                }
                progressBar.setProgress(newProgress > 5 ? newProgress : 5);
                if(progressBar.getProgress()==100){
                    progressBar.setVisibility(View.GONE);
                    if(mData!=null){
                        String jsUrl = "javascript:shopDestination("+mData+")";
                        loadH5Method(jsUrl);
                        mData = null;
                    }
                }

            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });

    }

    public void loadH5Method(String method) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl(method);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK  && webView.canGoBack()) {
            webView.goBack();
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }
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