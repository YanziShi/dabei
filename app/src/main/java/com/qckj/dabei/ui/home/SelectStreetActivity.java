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
import com.qckj.dabei.ui.home.adapter.StreetAdapter;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.manager.location.GaoDeLocationManager;
import com.qckj.dabei.manager.location.UserLocationInfo;
import com.qckj.dabei.model.street.StreetInfo;
import com.qckj.dabei.model.street.StreetRequester;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.view.dialog.LoadingProgressDialog;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

/**
 * Created by yangzhizhong on 2019/5/7.
 */
public class SelectStreetActivity extends BaseActivity {

    EditText editSearch;
    ListView listView;
    LoadingProgressDialog dialog;
    StreetAdapter adapter;
    List<StreetInfo> datas;
    UserLocationInfo userLocationInfo;
    @Manager
    private GaoDeLocationManager gaoDeLocationManager;

    boolean notToNext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_street);
        notToNext = getIntent().getBooleanExtra("notToNext",false);
        editSearch = (EditText) findViewById(R.id.edit_search);
        listView   = (ListView) findViewById(R.id.list_view);
        adapter = new StreetAdapter(this);
        listView.setAdapter(adapter);
        dialog = new LoadingProgressDialog(this);
        userLocationInfo = gaoDeLocationManager.getUserLocationInfo();
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
                if(notToNext){
                    Intent intentBack = new Intent();
                    intentBack.putExtra("streetId",datas.get(position).getStreet_id());
                    intentBack.putExtra("streetName",datas.get(position).getName());
                    setResult(RESULT_OK,intentBack);
                    finish();
                }else{
                    Intent intent = new Intent(SelectStreetActivity.this , BusinessCircleActivity.class);
                    intent.putExtra("streetId",datas.get(position).getStreet_id());
                    startActivityForResult(intent,1);
                }

            }
        });
        loadData();
    }

    void loadData(){
        dialog.show();
        new StreetRequester("null",userLocationInfo.getAdCode(),editSearch.getText().toString(),
                new OnHttpResponseCodeListener<List<StreetInfo>>(){

                    @Override
                    public void onHttpResponse(boolean isSuccess, List<StreetInfo> streetInfos, String message) {
                        super.onHttpResponse(isSuccess, streetInfos, message);
                        dialog.dismiss();
                        if(isSuccess && streetInfos!=null){
                            datas = streetInfos;
                            adapter.setData(streetInfos);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            setResult(RESULT_OK,data);
            finish();
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
