package com.qckj.dabei.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.app.CommonConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.manager.mine.merchant.WeixinPayRequester;
import com.qckj.dabei.ui.mine.merchant.GoldMerchantActivity;
import com.qckj.dabei.util.CommonUtils;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 微信入口界面
 * <p>
 * Created by yangzhizhong on 2019/3/28.
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "WXPayEntryActivity";

    private IWXAPI api;

    //微信支付接口
    static public void weixinPay(final Activity act,int type, String orderCode, double orderMoney){
        new WeixinPayRequester(type,orderCode,orderMoney,new OnHttpResponseCodeListener<JSONObject>(){
            @Override
            public void onHttpResponse(boolean isSuccess, JSONObject jsonObject, String message) {
                super.onHttpResponse(isSuccess, jsonObject, message);
                if(isSuccess){
                    try {
                        IWXAPI api = WXAPIFactory.createWXAPI(act, CommonUtils.WX_APP_ID);
                        api.registerApp(CommonUtils.WX_APP_ID);
                        PayReq req = new PayReq();
                        JSONObject data = jsonObject.getJSONObject("data");
                        req.appId           = data.getString("appid");//你的微信appid
                        req.partnerId       = data.getString("partnerid");//商户号
                        req.prepayId        = data.getString("prepayid");//预支付交易会话ID
                        req.nonceStr        = data.getString("noncestr");//随机字符串
                        req.timeStamp       = data.getString("timestamp");//时间戳
                        req.packageValue    = data.getString("package");   //扩展字段,这里固定填写Sign=WXPay
                        req.sign            = data.getString("sign");//签名
                        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                        api.sendReq(req);
                    } catch (JSONException e) { e.printStackTrace(); }
                }else Toast.makeText(act,message,Toast.LENGTH_SHORT).show();
            }
        }).doPost();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this,CommonUtils.WX_APP_ID);
        api.handleIntent(getIntent(), this);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }


    @Override
    public void onReq(BaseReq req) {
        Gson gson = new Gson();
        Log.e(TAG, gson.toJson(req));
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (resp.errCode == 0) {
                Toast.makeText(this,"支付成功",Toast.LENGTH_SHORT).show();
                EventBus.getDefault().post(CommonUtils.CHANGE_USERINFO_TAG);
            } else {
                Toast.makeText(this,"支付失败",Toast.LENGTH_SHORT).show();
            }
            finish();
        }

    }

}