package com.qckj.dabei.ui.mine.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.app.SimpleBaseAdapter;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.manager.mine.user.UserManager;
import com.qckj.dabei.manager.mine.wallet.DeliveryAddressRequester;
import com.qckj.dabei.model.mine.DeliveryAddressInfo;
import com.qckj.dabei.ui.mine.wallet.adapter.AddressAdapter;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.ViewInject;
import com.qckj.dabei.view.ActionBar;
import com.qckj.dabei.view.listview.OnPullRefreshListener;
import com.qckj.dabei.view.listview.PullRefreshView;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

/**
 * Created by yangzhizhong on 2019/5/29.
 */
public class DeliveryAddressActivity extends BaseActivity {

    @FindViewById(R.id.action_bar)
    private ActionBar actionBar;

    @FindViewById(R.id.list_view)
    private PullRefreshView pullRefreshView;

    @FindViewById(R.id.no_record)
    private TextView noRecord;

    @Manager
    private UserManager userManager;
    static public String userId ;

    private AddressAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_address);
        ViewInject.inject(this);
        userId = userManager.getCurId();
        initView();
    }

    private void initView() {
        adapter = new AddressAdapter(this);
        pullRefreshView.setAdapter(adapter);
        pullRefreshView.setPullRefreshEnable(true);
        pullRefreshView.setLoadMoreEnable(false);
        actionBar.setOnActionBarClickListener(new ActionBar.OnActionBarClickListener() {
            @Override
            public boolean onActionBarClick(int function) {
                if(function == ActionBar.FUNCTION_TEXT_RIGHT){
                    Intent intent = new Intent(DeliveryAddressActivity.this, EditAddressActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });

        adapter.setOnAdapterItemClickListener(new SimpleBaseAdapter.OnAdapterItemClickListener<DeliveryAddressInfo>() {
            @Override
            public void onAdapterItemClick(int position, DeliveryAddressInfo deliveryAddressInfo) {
                Intent intent = new Intent();
                intent.putExtra("deliveryAddressInfo",deliveryAddressInfo);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        pullRefreshView.setOnPullRefreshListener(new OnPullRefreshListener() {
            @Override
            public void onPullDownRefresh(PullRefreshView pullRefreshView) {
                loadData();
            }
        });
        adapter.setListener(new SimpleBaseAdapter.Listener() {
            @Override
            public void isChange() {
                loadData();
            }
        });
    }

    void loadData(){
        pullRefreshView.startPullRefresh();
        new DeliveryAddressRequester(userManager.getCurId(),new OnHttpResponseCodeListener<List<DeliveryAddressInfo>>(){
            @Override
            public void onHttpResponse(boolean isSuccess, List<DeliveryAddressInfo> deliveryAddressInfos, String message) {
                super.onHttpResponse(isSuccess, deliveryAddressInfos, message);
                pullRefreshView.stopPullRefresh();
                if(isSuccess && deliveryAddressInfos.size()>0){
                    noRecord.setVisibility(View.GONE);
                    pullRefreshView.setVisibility(View.VISIBLE);
                    adapter.setData(deliveryAddressInfos);
                }else {
                    noRecord.setVisibility(View.VISIBLE);
                    pullRefreshView.setVisibility(View.GONE);
                    showToast(message);
                }
            }
        }).doPost();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        loadData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}

