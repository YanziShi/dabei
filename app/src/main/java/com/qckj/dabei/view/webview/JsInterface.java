package com.qckj.dabei.view.webview;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.qckj.dabei.R;
import com.qckj.dabei.app.App;
import com.qckj.dabei.view.dialog.SelectMapDialog;
import com.qckj.dabei.manager.SettingManager;
import com.qckj.dabei.manager.location.GaoDeLocationManager;
import com.qckj.dabei.manager.location.UserLocationInfo;
import com.qckj.dabei.manager.mine.user.UserManager;
import com.qckj.dabei.model.SharedAppInfo;
import com.qckj.dabei.ui.main.MainActivity;
import com.qckj.dabei.ui.mine.user.LoginActivity;
import com.qckj.dabei.util.ImgUtils;
import com.qckj.dabei.util.json.JsonHelper;
import com.qckj.dabei.view.dialog.AppShareDialog;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 与js交互管理类
 */
public class JsInterface {
    private Context context;
    private String title = "";


    public JsInterface(Context context) {
        this.context = context;
    }

    public interface GoCarListner{
        void goCar(String data);
    }
    GoCarListner mGoCarListner;
    public void setGoCarListner(GoCarListner goCarListner){
        mGoCarListner = goCarListner;
    }

    /**
     * 获取用户id
     *
     * @return int
     */
    @JavascriptInterface
    public String getUid() {
        return App.getInstance().getManager(UserManager.class).getCurId();
    }

    /**
     * 获取客户端版本号
     *
     * @return 版本号
     */
    @JavascriptInterface
    public int getVersionCode() {
        //获取包管理器
        PackageManager pm = context.getPackageManager();
        //获取包信息
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            //返回版本号
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @JavascriptInterface
    public void finishWebView() {
        if (context instanceof Activity) {
            ((Activity) context).finish();
        }
    }

    @JavascriptInterface
    public String back() {
        return "null";
    }

    @JavascriptInterface
    public String getUserInfo() {
        String userInfo = App.getInstance().getManager(SettingManager.class).getSetting(SettingManager.KEY_USER_INFO, "");
        if(BrowserActivity.orderType!=null){
            try {
                JSONObject jsonObject = new JSONObject(userInfo);
                jsonObject.put("orderType",BrowserActivity.orderType);
                userInfo = jsonObject.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return userInfo;
    }

    @JavascriptInterface
    public void backToAndroid() {
        if (context instanceof Activity) {
            ((Activity) context).finish();
        }
    }

    @JavascriptInterface
    public String getLocation() {
        // TODO: 2019/4/9 待修改
        UserLocationInfo userLocationInfo = App.getInstance().getManager(GaoDeLocationManager.class).getUserLocationInfo();
        return "[" + userLocationInfo.getLongitude() + "," + userLocationInfo.getLatitude() + "]";
    }

    @JavascriptInterface
    public void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phone);
        intent.setData(data);
        context.startActivity(intent);
    }

    @JavascriptInterface
    public void sharedAppInfo(String jsonStr) {
        try {
            SharedAppInfo sharedAppInfo = JsonHelper.toObject(new JSONObject(jsonStr), SharedAppInfo.class);
            AppShareDialog dialog = new AppShareDialog((Activity) context);
            dialog.setContent(sharedAppInfo.getDescribe());
            dialog.setTitle(sharedAppInfo.getTitle());
            dialog.setShareType(sharedAppInfo.getShareType());
            dialog.setContentUrl(sharedAppInfo.getLink());
            dialog.setIconRec(R.mipmap.ic_launcher);
            dialog.setIconUrl(sharedAppInfo.getIconUrl());
            dialog.includeCard(sharedAppInfo.isCard());
            dialog.show();
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @JavascriptInterface
    public void mobileCopy(String text) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        //创建ClipData对象
        ClipData clipData = ClipData.newPlainText("copy", text);
        //添加ClipData对象到剪切板中
        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(context,"复制成功！",Toast.LENGTH_SHORT).show();
    }

    @JavascriptInterface
    public void getBeizhu() {
        MainActivity.startActivity(context,MainActivity.INDEX_RELEASE_DEMAND);
    }

    @JavascriptInterface
    public void appLogin() {
        LoginActivity.startActivity((Activity) context);
        ((Activity) context).finish();
    }

    @JavascriptInterface
    public void downloadImg(String urlImage) {
        ImgUtils.donwloadImg(context,urlImage);
    }

    @JavascriptInterface
    public void thirdPartyLink(String url) {
        BrowserActivity.startActivity(context,url,true);
    }

    @JavascriptInterface
    public void goDaBeiCar(String jsonData) {
        if(mGoCarListner!=null){
            mGoCarListner.goCar(jsonData);
        }
    }

    @JavascriptInterface
    public void goNavagetion(String jsonData) {
       SelectMapDialog dialog = new SelectMapDialog(context);
       dialog.setData(jsonData);
       dialog.show();
    }

}
