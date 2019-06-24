package com.qckj.dabei.ui.home;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.manager.home.ServiceDetailRequester;
import com.qckj.dabei.model.home.ServiceDetailInfo;
import com.qckj.dabei.ui.home.adapter.GoodsAdapter;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.OnClick;
import com.qckj.dabei.util.inject.ViewInject;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

/**
 * @name: 梁永胜
 * @date ：2018/11/10 14:33
 * E-Mail Address：875450820@qq.com
 */
public class ServiceDetailActivity extends BaseActivity{
    @FindViewById(R.id.price_tv)
    TextView priceTv;
    @FindViewById(R.id.name_tv)
    TextView nameTv;
    @FindViewById(R.id.adress_tv)
    TextView adressTv;
    @FindViewById(R.id.goods_iv)
    ImageView goodsIv;
    @FindViewById(R.id.store_service_tv)
    TextView storeSerViceTv;
    @FindViewById(R.id.store_detail_tv)
    TextView storeDetailTv;
    @FindViewById(R.id.text_message)
    TextView textMessage;
    @FindViewById(R.id.list_view)
    ListView listView;
    private String id;

    GoodsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detail);
        ViewInject.inject(this);
        adapter = new GoodsAdapter(this);
        listView.setAdapter(adapter);
        id = getIntent().getStringExtra("id");
        loadData();
    }

    private void setViewData(List<ServiceDetailInfo> beans){
        Glide.with(this).load(beans.get(0).getF_C_SPFMIMG()).into(goodsIv);
        priceTv.setText(beans.get(0).getF_I_MONEY()+"/元");
        nameTv.setText(beans.get(0).getF_C_SPNAME());
        adressTv.setText(beans.get(0).getF_C_ADDRESS());
        textMessage.setText(beans.get(0).getMessage());
        adapter.setData(beans.get(0).getGoods());
    }

    private void loadData() {
        showLoadingProgressDialog();
        new ServiceDetailRequester(id,new OnHttpResponseCodeListener<List<ServiceDetailInfo>>(){
            @Override
            public void onHttpResponse(boolean isSuccess, List<ServiceDetailInfo> serviceDetailBeans, String message) {
                super.onHttpResponse(isSuccess, serviceDetailBeans, message);
                dismissLoadingProgressDialog();
                if(isSuccess && serviceDetailBeans!=null && serviceDetailBeans.size()>0){
                    setViewData(serviceDetailBeans);
                }
            }

            @Override
            public void onLocalErrorResponse(int code) {
                super.onLocalErrorResponse(code);
                dismissLoadingProgressDialog();
            }
        }).doPost();

    }

    @OnClick({R.id.store_service_tv,R.id.store_detail_tv})
    void onViewClick(View v) {
        switch (v.getId()){
            case R.id.store_service_tv:
                storeSerViceTv.setTextColor(this.getResources().getColor(R.color.statue_color));
                storeSerViceTv.setTextSize(16);
                storeDetailTv.setTextColor(this.getResources().getColor(R.color.gray));
                storeDetailTv.setTextSize(14);
                listView.setVisibility(View.VISIBLE);
                textMessage.setVisibility(View.GONE);
                break;
            case R.id.store_detail_tv:
                storeDetailTv.setTextColor(this.getResources().getColor(R.color.statue_color));
                storeDetailTv.setTextSize(16);
                storeSerViceTv.setTextColor(this.getResources().getColor(R.color.gray));
                storeSerViceTv.setTextSize(14);
                listView.setVisibility(View.GONE);
                textMessage.setVisibility(View.VISIBLE);
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
