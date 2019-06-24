package com.qckj.dabei.ui.mine.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.manager.mine.order.FinishXuqiuRequester;
import com.qckj.dabei.manager.mine.order.ParticipantRequester;
import com.qckj.dabei.manager.mine.order.RomoveNeedRequester;
import com.qckj.dabei.manager.mine.user.UserManager;
import com.qckj.dabei.model.mine.MineReleaseInfo;
import com.qckj.dabei.model.mine.ParticipantInfo;
import com.qckj.dabei.ui.lib.DemandDetailActivity;
import com.qckj.dabei.ui.mine.order.adapter.ParticipantAdapter;
import com.qckj.dabei.util.DateUtils;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.OnClick;
import com.qckj.dabei.util.inject.ViewInject;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by yangzhizhong on 2019/6/1.
 */
public class ReleaseDetailActivity extends BaseActivity {

    @FindViewById(R.id.text_title)
    TextView textTitle;
    @FindViewById(R.id.text_time)
    TextView textTime;
    @FindViewById(R.id.text_intro)
    TextView textIntro;
    @FindViewById(R.id.btn_cancel_delete)
    Button btnCancelDelete;
    @FindViewById(R.id.list_view)
    ListView listView;
    @FindViewById(R.id.no_record)
    TextView noRecord;

    @Manager
    UserManager userManager;
    MineReleaseInfo mineReleaseInfo;

    ParticipantAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_detail);
        ViewInject.inject(this);
        mineReleaseInfo = (MineReleaseInfo) getIntent().getSerializableExtra("data");
        initView();
        loadData();
    }

    void initView(){
        textTitle.setText(mineReleaseInfo.getName());
        textTime.setText(DateUtils.getTimeStringByMillisecondsWithFormatString(mineReleaseInfo.getTime(), "yyyy-MM-dd hh:mm:ss"));
        textIntro.setText(mineReleaseInfo.getMes());
        if(mineReleaseInfo.getState().equals("2")) btnCancelDelete.setText("删除");
        else btnCancelDelete.setText("取消");
        adapter = new ParticipantAdapter(this);
        listView.setAdapter(adapter);
        adapter.setParticipantListener(new ParticipantAdapter.ParticipantListener() {
            @Override
            public void isFinish(String userOrderId) {
                new FinishXuqiuRequester(userManager.getCurId(),mineReleaseInfo.getId(),userOrderId,
                        new OnHttpResponseCodeListener<JSONObject>(){
                            @Override
                            public void onHttpResponse(boolean isSuccess, JSONObject jsonObject, String message) {
                                super.onHttpResponse(isSuccess, jsonObject, message);
                                showToast(message);
                                if(isSuccess) {
                                    setResult(RESULT_OK);
                                    finish();
                                }
                            }
                        }).doPost();
            }

            @Override
            public void isEvaluate(String userOrderId) {
                Intent intent = new Intent(ReleaseDetailActivity.this,EvaluationActivity.class);
                intent.putExtra("userOrderId",userOrderId);
                startActivity(intent);
            }
        });
    }

    void loadData(){
        showLoadingProgressDialog();
        new ParticipantRequester(userManager.getCurId(),mineReleaseInfo.getId(),mineReleaseInfo.getIsPay(),
                new OnHttpResponseCodeListener<List<ParticipantInfo>>(){
                    @Override
                    public void onHttpResponse(boolean isSuccess, List<ParticipantInfo> participantInfos, String message) {
                        super.onHttpResponse(isSuccess, participantInfos, message);
                        dismissLoadingProgressDialog();
                        if(isSuccess && participantInfos.size()>0){
                            listView.setVisibility(View.VISIBLE);
                            noRecord.setVisibility(View.GONE);
                            adapter.setData(participantInfos);
                        }else {
                            listView.setVisibility(View.GONE);
                            noRecord.setVisibility(View.VISIBLE);
                        }
                    }
                }).doPost();
    }

    @OnClick({R.id.btn_cancel_delete,R.id.btn_need_detail})
    void onViewClick(View view){
        switch (view.getId()){
            case R.id.btn_cancel_delete:
                showLoadingProgressDialog();
                new RomoveNeedRequester(userManager.getCurId(),mineReleaseInfo.getId(),new OnHttpResponseCodeListener<JSONObject>(){
                    @Override
                    public void onHttpResponse(boolean isSuccess, JSONObject jsonObject, String message) {
                        super.onHttpResponse(isSuccess, jsonObject, message);
                        dismissLoadingProgressDialog();
                        showToast(message);
                        if(isSuccess){
                            setResult(RESULT_OK);
                            finish();
                        }
                    }
                }).doPost();
                break;
            case R.id.btn_need_detail:
                Intent intent = new Intent(ReleaseDetailActivity.this, DemandDetailActivity.class);
                intent.putExtra("id", mineReleaseInfo.getId());
                intent.putExtra("hideServer",true);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            setResult(RESULT_OK);
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
