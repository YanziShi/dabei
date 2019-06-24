package com.qckj.dabei.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.qckj.dabei.R;
import com.qckj.dabei.ui.home.adapter.OneCategoryAdapter;
import com.qckj.dabei.ui.home.adapter.TwoCategoryAdapter;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.manager.home.GetHomeFunctionInfoRequester;
import com.qckj.dabei.model.home.HomeFunctionInfo;
import com.qckj.dabei.view.dialog.LoadingProgressDialog;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

/**
 * Created by yangzhizhong on 2019/5/9.
 */
public class MoreCategoryActivity extends BaseActivity {

    OneCategoryAdapter oneCategoryAdapter;
    TwoCategoryAdapter twoCategoryAdapter;
    ListView listViewOne;
    ListView listViewTwo;
    LoadingProgressDialog dialog;
    List<HomeFunctionInfo> mHomeFunctionInfos;
    List<HomeFunctionInfo.Category> categories;

    int curOneSelectePos=0;
    int curTwoSelectePos=0;
    boolean notToNext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_category);
        notToNext  = getIntent().getBooleanExtra("notToNext",false);
        oneCategoryAdapter = new OneCategoryAdapter(this);
        twoCategoryAdapter = new TwoCategoryAdapter(this);
        dialog = new LoadingProgressDialog(this);

        listViewOne = (ListView) findViewById(R.id.one_classify);
        listViewTwo = (ListView) findViewById(R.id.two_classify);
        listViewOne.setAdapter(oneCategoryAdapter);
        listViewTwo.setAdapter(twoCategoryAdapter);

        listViewOne.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    twoCategoryAdapter.setData(mHomeFunctionInfos.get(position).getCategoryList());
                    categories = mHomeFunctionInfos.get(position).getCategoryList();
                    mHomeFunctionInfos.get(curOneSelectePos).setIsSelected(false);
                    curOneSelectePos = position;
                    mHomeFunctionInfos.get(curOneSelectePos).setIsSelected(true);
                    oneCategoryAdapter.notifyDataSetChanged();
            }
        });

        listViewTwo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(notToNext){
                    Intent intent = new Intent();
                    intent.putExtra("id",mHomeFunctionInfos.get(curOneSelectePos).getCategoryList().get(position).getClassId());
                    intent.putExtra("name",mHomeFunctionInfos.get(curOneSelectePos).getCategoryList().get(position).getClassName());
                    setResult(RESULT_OK,intent);
                    finish();
                }else{
                    mHomeFunctionInfos.get(curOneSelectePos).getCategoryList().get(curTwoSelectePos).setIsSelected(false);
                    curTwoSelectePos = position;
                    mHomeFunctionInfos.get(curOneSelectePos).getCategoryList().get(curTwoSelectePos).setIsSelected(true);
                    twoCategoryAdapter.notifyDataSetChanged();
                    ServiceBusinessActivity.startActivity(MoreCategoryActivity.this,mHomeFunctionInfos.get(curOneSelectePos),position);
                }

            }
        });

        loadData();
    }

    void loadData(){
        dialog.show();
        new GetHomeFunctionInfoRequester(new OnHttpResponseCodeListener<List<HomeFunctionInfo>>() {
            @Override
            public void onHttpResponse(boolean isSuccess, List<HomeFunctionInfo> homeFunctionInfos, String message) {
                super.onHttpResponse(isSuccess, homeFunctionInfos, message);
                dialog.dismiss();
                if (isSuccess) {
                    mHomeFunctionInfos = homeFunctionInfos;
                    mHomeFunctionInfos.get(0).setIsSelected(true);
                    oneCategoryAdapter.setData(mHomeFunctionInfos);

                    categories = homeFunctionInfos.get(0).getCategoryList();
                    categories.get(0).setIsSelected(true);
                    twoCategoryAdapter.setData(homeFunctionInfos.get(0).getCategoryList());
                }else showToast(message);
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
