package com.qckj.dabei.manager.balance;

import android.app.Activity;
import android.widget.Toast;

import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.manager.mine.merchant.BalancePayRequester;
import com.qckj.dabei.util.CommonUtils;
import com.umeng.commonsdk.debug.E;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

/**
 * Created by yangzhizhong on 2019/5/21.
 */
public class BalanceUtil {
    static final public int PAY_VIP = 1;
    static final public int PAY_GOODS = 2;
    static final public int PAY_TOP = 3;
    static final public int PAY_GOLD = 4;
    //余额支付接口
   static public void balancePay(Activity act,int type,String orderCode){
        new BalancePayRequester(type,orderCode,new OnHttpResponseCodeListener<JSONObject>(){
            @Override
            public void onHttpResponse(boolean isSuccess, JSONObject jsonObject, String message) {
                super.onHttpResponse(isSuccess, jsonObject, message);
                Toast.makeText(act,message,Toast.LENGTH_SHORT).show();
                if(isSuccess){
                    EventBus.getDefault().post(CommonUtils.CHANGE_USERINFO_TAG);
                    act.finish();
                }
            }
        }).doPost();
    }
}
