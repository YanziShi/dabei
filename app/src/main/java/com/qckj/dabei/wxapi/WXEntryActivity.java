package com.qckj.dabei.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.qckj.dabei.R;
import com.qckj.dabei.app.http.AsyncHttpClient;
import com.qckj.dabei.app.http.HttpRequester;
import com.qckj.dabei.app.http.IResponseHandler;
import com.qckj.dabei.app.http.OnResultMessageListener;
import com.qckj.dabei.manager.mine.user.UserManager;
import com.qckj.dabei.ui.mine.user.BindPhoneActivity;
import com.qckj.dabei.util.CommonUtils;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.ViewInject;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yangzhizhong on 2019/6/3.
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
        private IWXAPI mWeixinAPI;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWeixinAPI = WXAPIFactory.createWXAPI(this, CommonUtils.WX_APP_ID, true);
        mWeixinAPI.handleIntent(this.getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        mWeixinAPI.handleIntent(intent, this);//必须调用此句话
    }

    //微信发送的请求将回调到onReq方法
    @Override
    public void onReq(BaseReq req) {

    }

    //发送到微信请求的响应结果
    @Override
    public void onResp(BaseResp resp) {
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                //发送成功
                finish();
                SendAuth.Resp sendResp = (SendAuth.Resp) resp;
                if (sendResp != null) {
                    String code = sendResp.code;
                    getAccess_token(code);
                }
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                finish();
                //发送取消
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                finish();
                //发送被拒绝
                break;
            default:
                //发送返回
                break;
        }
    }

    private void getAccess_token(final String code) {
        String path = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
                + CommonUtils.WX_APP_ID
                + "&secret="
                + CommonUtils.WX_APP_SECRET
                + "&code="
                + code
                + "&grant_type=authorization_code";
        //网络请求，根据自己的请求方式
        AsyncHttpClient.get(path, new IResponseHandler() {
            @Override
            public void onResult(int code, byte[] data) {
                String result = new String(data);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String openid = jsonObject.getString("openid").toString().trim();
                    EventBus.getDefault().post(openid);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception exception) {
                Log.d("Exception",exception.getMessage());
            }
        });

    }


}