package com.qckj.dabei.ui.mine.wallet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qckj.dabei.R;
import com.qckj.dabei.alipay.MyALipayUtils;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.view.dialog.ChoosePayTypeDialog;
import com.qckj.dabei.manager.balance.BalanceUtil;
import com.qckj.dabei.manager.mine.user.UserManager;
import com.qckj.dabei.manager.mine.wallet.CreateGoodOrderRequester;
import com.qckj.dabei.manager.mine.wallet.DeliveryAddressRequester;
import com.qckj.dabei.model.mine.DeliveryAddressInfo;
import com.qckj.dabei.model.mine.GoodsInfo;
import com.qckj.dabei.ui.main.fragment.MineFragment;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.OnClick;
import com.qckj.dabei.util.inject.ViewInject;
import com.qckj.dabei.wxapi.WXPayEntryActivity;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 提交订单、立即兑换
 */
public class OrderActivity extends BaseActivity {

    @FindViewById(R.id.text_name)
    TextView textName;
    @FindViewById(R.id.text_phone)
    TextView textPhone;
    @FindViewById(R.id.text_address)
    TextView textAddress;
    @FindViewById(R.id.goods_iv)
    ImageView goodsIv;
    @FindViewById(R.id.text_title)
    TextView textTitle;
    @FindViewById(R.id.text_origin_price)
    TextView textOriginPrice;
    @FindViewById(R.id.text_sale_price)
    TextView textSalePrice;
    @FindViewById(R.id.text_num)
    TextView textNum;
    @FindViewById(R.id.text_select_num)
    TextView textSelectNum;
    @FindViewById(R.id.text_beizhu_num)
    TextView textBeizhuNum;
    @FindViewById(R.id.text_rmb_num)
    TextView textRmbNum;

    @Manager
    UserManager userManager;

    GoodsInfo goodsInfo;

    int saleNum;    //商品所需贝珠数
    int curNum = 1;    //当前的数量
    String addressId;

    static public void startActivity(Activity context, GoodsInfo goodsInfo){
        int beizhuNum = MineFragment.userInfo.getPOINT();
        int saleNum   = Integer.valueOf(goodsInfo.getSaleBeizhuPrice());
        if(saleNum>beizhuNum){
            new AlertDialog.Builder(context).setTitle("等赚够了贝珠再来兑换哟！").setNegativeButton("确定",null).show();
            return;
        }else{
            Intent intent = new Intent(context,OrderActivity.class);
            intent.putExtra("goodsInfo",goodsInfo);
            intent.putExtra("saleNum",saleNum);
            context.startActivityForResult(intent,0);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ViewInject.inject(this);
        EventBus.getDefault().register(this);
        goodsInfo = (GoodsInfo) getIntent().getSerializableExtra("goodsInfo");
        saleNum   = getIntent().getIntExtra("saleNum",0);
        Glide.with(this).load(goodsInfo.getImageUrl()).into(goodsIv);
        textTitle.setText(goodsInfo.getName());
        textOriginPrice.setText("原价:"+goodsInfo.getRmbPrice()+"元");
        textSalePrice.setText("商城价:"+goodsInfo.getSaleRmbPrice()+"元+"+goodsInfo.getSaleBeizhuPrice()+"贝珠");
        textBeizhuNum.setText("使用"+goodsInfo.getSaleBeizhuPrice()+"贝珠抵扣");
        textRmbNum.setText("待支付：￥"+goodsInfo.getSaleRmbPrice());

        loadData();
    }

    void loadData(){
        showLoadingProgressDialog();
        new DeliveryAddressRequester(userManager.getCurId(),new OnHttpResponseCodeListener<List<DeliveryAddressInfo>>(){
            @Override
            public void onHttpResponse(boolean isSuccess, List<DeliveryAddressInfo> deliveryAddressInfos, String message) {
                super.onHttpResponse(isSuccess, deliveryAddressInfos, message);
                dismissLoadingProgressDialog();
                if(isSuccess && deliveryAddressInfos.size()>0){
                    initAddrView(deliveryAddressInfos.get(0));
                }else showToast(message);
            }
        }).doPost();
    }

    void initAddrView(DeliveryAddressInfo deliveryAddressInfo){
        addressId = deliveryAddressInfo.getId();
        textName.setText(deliveryAddressInfo.getName()+"(收)");
        textPhone.setText(deliveryAddressInfo.getPhone());
        textAddress.setText(deliveryAddressInfo.getCity()+deliveryAddressInfo.getAddress());
    }

    @OnClick({R.id.btn_select_address,R.id.btn_add,R.id.btn_delete,R.id.btn_submit})
    void onClickView(View v){
        switch (v.getId()){
            case R.id.btn_add:
                curNum++;
                if(curNum*saleNum>MineFragment.userInfo.getPOINT()){
                    curNum--;
                    new AlertDialog.Builder(OrderActivity.this).setTitle("贝珠不够了，不能再添加了哦！").setNegativeButton("确定",null).show();
                    return;
                }
                textNum.setText("数量："+String.valueOf(curNum));
                textSelectNum.setText(String.valueOf(curNum));
                textBeizhuNum.setText("使用"+String.valueOf(curNum*saleNum)+"贝珠抵扣");
                textRmbNum.setText("待支付：￥"+String.valueOf(Double.valueOf(goodsInfo.getSaleRmbPrice())*curNum));
                break;
            case R.id.btn_delete:
                curNum--;
                if(curNum==0){
                    curNum++;
                    new AlertDialog.Builder(OrderActivity.this).setTitle("不能再少了亲！").setNegativeButton("确定",null).show();
                    return;
                }
                textNum.setText("数量："+String.valueOf(curNum));
                textSelectNum.setText(String.valueOf(curNum));
                textBeizhuNum.setText("使用"+String.valueOf(curNum*saleNum)+"贝珠抵扣");
                textRmbNum.setText("待支付：￥"+String.valueOf(Double.valueOf(goodsInfo.getSaleRmbPrice())*curNum));
                break;
            case R.id.btn_select_address:
                Intent intent = new Intent(OrderActivity.this,DeliveryAddressActivity.class);
                startActivityForResult(intent,0);
                break;
            case R.id.btn_submit:
                ChoosePayTypeDialog dlg = new ChoosePayTypeDialog(this);
                dlg.show();
                dlg.setData(Double.valueOf(goodsInfo.getSaleRmbPrice())*curNum,null, Double.valueOf(MineFragment.userInfo.getF_C_BALANCE()));
                dlg.setListener(new ChoosePayTypeDialog.OnListener() {
                    @Override
                    public void select(int type,double cost) {
                        new CreateGoodOrderRequester(userManager.getCurId(),goodsInfo.getSaleRmbPrice(),goodsInfo.getSaleRmbPrice(),curNum,
                                goodsInfo.getId(),addressId,new OnHttpResponseCodeListener<JSONObject>(){
                            @Override
                            public void onHttpResponse(boolean isSuccess, JSONObject jsonObject, String message) {
                                super.onHttpResponse(isSuccess, jsonObject, message);
                                if(isSuccess){
                                    try {
                                        String orderCode = jsonObject.getString("order_code");
                                        if(type ==0) BalanceUtil.balancePay(getActivity(),BalanceUtil.PAY_GOODS,orderCode);
                                        else if(type == 1) WXPayEntryActivity.weixinPay(getActivity(),BalanceUtil.PAY_GOODS,orderCode,cost);
                                        else MyALipayUtils.alyPay(getActivity(),BalanceUtil.PAY_GOODS,orderCode,cost);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }else showToast(message);
                            }
                        }).doPost();
                    }
                });
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            initAddrView((DeliveryAddressInfo) data.getSerializableExtra("deliveryAddressInfo"));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(String bundle) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
