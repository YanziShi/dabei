package com.qckj.dabei.ui.mine.wallet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.manager.mine.user.UserManager;
import com.qckj.dabei.manager.mine.wallet.EditAddressRequester;
import com.qckj.dabei.model.mine.DeliveryAddressInfo;
import com.qckj.dabei.view.AddressPicker;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.ViewInject;
import com.qckj.dabei.view.ActionBar;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

/**
 * Created by yangzhizhong on 2019/5/30.
 */
public class EditAddressActivity extends BaseActivity {
    @FindViewById(R.id.action_bar)
    ActionBar actionBar;
    @FindViewById(R.id.edit_name)
    EditText editName;
    @FindViewById(R.id.edit_phone)
    EditText editPhone;
    @FindViewById(R.id.btn_city)
    TextView textCity;
    @FindViewById(R.id.edit_address)
    EditText editAddress;

    @Manager
    UserManager userManager;
    String addressId = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);
        ViewInject.inject(this);
        DeliveryAddressInfo deliveryAddressInfo = (DeliveryAddressInfo)getIntent().getSerializableExtra("deliveryAddressInfo");
        if(deliveryAddressInfo!=null){
            actionBar.setTitleText("编辑地址");
            editName.setText(deliveryAddressInfo.getName());
            editPhone.setText(deliveryAddressInfo.getPhone());
            textCity.setText(deliveryAddressInfo.getCity());
            editAddress.setText(deliveryAddressInfo.getAddress());
            addressId = deliveryAddressInfo.getId();
        }
        textCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddressPicker.showAddrPicker( EditAddressActivity.this,new AddressPicker.ClickListener() {
                    @Override
                    public void onClick(String address) {
                        textCity.setText(address);
                    }
                });
            }
        });

        actionBar.setOnActionBarClickListener(new ActionBar.OnActionBarClickListener() {
            @Override
            public boolean onActionBarClick(int function) {
                if(function == ActionBar.FUNCTION_TEXT_RIGHT){
                    showLoadingProgressDialog();
                    new EditAddressRequester(userManager.getCurId(),addressId,editName.getText().toString(),editPhone.getText().toString(),
                            textCity.getText().toString(),editAddress.getText().toString(),new OnHttpResponseCodeListener<JSONObject>(){
                        @Override
                        public void onHttpResponse(boolean isSuccess, JSONObject jsonObject, String message) {
                            super.onHttpResponse(isSuccess, jsonObject, message);
                            dismissLoadingProgressDialog();
                            showToast(message);
                            if(isSuccess){
                                finish();
                            }
                        }
                    }).doPost();
                }
                return false;
            }
        });
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
