package com.qckj.dabei.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.qckj.dabei.R;
import com.qckj.dabei.ui.home.adapter.BusinessCircleAdapter;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.model.street.BusinessCircleInfo;
import com.qckj.dabei.model.street.BusinessCircleRequester;
import com.qckj.dabei.view.ActionBar;
import com.qckj.dabei.view.dialog.LoadingProgressDialog;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

/**
 * Created by yangzhizhong on 2019/5/8.
 */
public class BusinessCircleActivity extends BaseActivity {

    EditText editSearch;
    ListView listView;
    LoadingProgressDialog dialog;
    BusinessCircleAdapter adapter;
    String streetId;
    ActionBar actionBar;
    List<BusinessCircleInfo> datas;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_street);
        streetId = getIntent().getStringExtra("streetId");
        actionBar = (ActionBar) findViewById(R.id.action_bar);
        actionBar.setTitleText("选择商圈");
        editSearch = (EditText) findViewById(R.id.edit_search);
        listView   = (ListView) findViewById(R.id.list_view);
        adapter = new BusinessCircleAdapter(this);
        listView.setAdapter(adapter);
        dialog = new LoadingProgressDialog(this);
        editSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                loadData();
                return false;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("F_C_ID",datas.get(position).getStreet_id());
                intent.putExtra("F_C_NAME",datas.get(position).getName());
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        loadData();
    }

    void loadData(){
        dialog.show();
        new BusinessCircleRequester("null",streetId,editSearch.getText().toString(),
                new OnHttpResponseCodeListener<List<BusinessCircleInfo>>(){

                    @Override
                    public void onHttpResponse(boolean isSuccess, List<BusinessCircleInfo> businessCircleInfos, String message) {
                        super.onHttpResponse(isSuccess, businessCircleInfos, message);
                        dialog.dismiss();
                        if(isSuccess && businessCircleInfos!=null){
                            datas = businessCircleInfos;
                            adapter.setData(businessCircleInfos);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onLocalErrorResponse(int code) {
                        super.onLocalErrorResponse(code);
                        dialog.dismiss();
                    }
                }).doPost();
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
