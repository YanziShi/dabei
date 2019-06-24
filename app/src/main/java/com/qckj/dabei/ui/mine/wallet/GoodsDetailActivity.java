package com.qckj.dabei.ui.mine.wallet;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.model.mine.GoodsInfo;
import com.qckj.dabei.ui.main.fragment.MineFragment;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.OnClick;
import com.qckj.dabei.util.inject.ViewInject;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by yangzhizhong on 2019/5/29.
 */
public class GoodsDetailActivity extends BaseActivity {
    @FindViewById(R.id.goods_iv)
    ImageView goodsIv;
    @FindViewById(R.id.text_origin_price)
    TextView textOriginPrice;
    @FindViewById(R.id.text_name)
    TextView textName;
    @FindViewById(R.id.text_sale_price)
    TextView textSalePrice;
    @FindViewById(R.id.text_message)
    TextView textMessage;

    GoodsInfo goodsInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
        ViewInject.inject(this);
        goodsInfo = (GoodsInfo) getIntent().getSerializableExtra("goodsInfo");
        Glide.with(this).load(goodsInfo.getImageUrl()).into(goodsIv);
        textName.setText(goodsInfo.getName());
        textOriginPrice.setText("原价：￥"+goodsInfo.getRmbPrice());
        textSalePrice.setText("商城价：￥"+goodsInfo.getSaleRmbPrice()+"元+"+goodsInfo.getSaleBeizhuPrice()+"贝珠");
        textMessage.setText(goodsInfo.getIntroduce());
    }

    @OnClick({R.id.btn_change})
    void onViewClick(View v) {
        switch (v.getId()){
            case R.id.btn_change:
                OrderActivity.startActivity(GoodsDetailActivity.this,goodsInfo);
                break;
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