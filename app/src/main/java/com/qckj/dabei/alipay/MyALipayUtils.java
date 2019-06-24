package com.qckj.dabei.alipay;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;
import com.alipay.sdk.app.PayTask;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.manager.mine.merchant.AlyPayRequester;
import com.qckj.dabei.util.CommonUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Map;

/**
 * 支付宝支付
 */

public class MyALipayUtils {
    private static final int SDK_PAY_FLAG = 1;
    private Activity context;
    private MyALipayUtils(Activity context) {
        this.context = context;
    }
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            AliPayResult payResult = new AliPayResult((Map<String, String>) msg.obj);
            String status = payResult.getResultStatus();
            switch (status) {
                case "9000":
                    Toast.makeText(context,"支付成功",Toast.LENGTH_SHORT).show();
                    context.finish();
                    EventBus.getDefault().post(CommonUtils.CHANGE_USERINFO_TAG);
                    break;
                case "8000":
                    Toast.makeText(context,"正在处理中",Toast.LENGTH_SHORT).show();
                    break;
                case "4000":
                    Toast.makeText(context,"订单支付失败",Toast.LENGTH_SHORT).show();
                    break;
                case "5000":
                    Toast.makeText(context,"重复请求",Toast.LENGTH_SHORT).show();
                    break;
                case "6001":
                    Toast.makeText(context,"已取消支付",Toast.LENGTH_SHORT).show();
                    break;
                case "6002":
                    Toast.makeText(context,"网络连接出错",Toast.LENGTH_SHORT).show();
                    break;
                case "6004":
                    Toast.makeText(context,"正在处理中",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(context,"支付失败",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    //支付宝支付接口
   static public void alyPay(final Activity act,int type,String orderCode,double orderMoney){
        new AlyPayRequester(type,orderCode,orderMoney,new OnHttpResponseCodeListener<JSONObject>(){
            @Override
            public void onHttpResponse(boolean isSuccess, JSONObject jsonObject, String message) {
                super.onHttpResponse(isSuccess, jsonObject, message);
                if(isSuccess){
                    try {
                        String orderInfo = jsonObject.getJSONObject("data").getString("orderInfo");
                        new MyALipayUtils(act).toALiPay(orderInfo);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else Toast.makeText(act,message,Toast.LENGTH_SHORT).show();
            }
        }).doPost();
    }

    public void toALiPay(final String orderInfo) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(context);
                Map<String, String> result = alipay.payV2
                        (orderInfo, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }
}
