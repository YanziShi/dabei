package com.qckj.dabei.ui.mine.msg;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.app.SimpleBaseAdapter;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.manager.mine.user.UserManager;
import com.qckj.dabei.manager.mine.msg.AgentInfoRequester;
import com.qckj.dabei.manager.mine.msg.GetMessageRequester;
import com.qckj.dabei.model.mine.MessageInfo;
import com.qckj.dabei.ui.mine.msg.adapter.MessageAdapter;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.ViewInject;
import com.qckj.dabei.view.ActionBar;
import com.qckj.dabei.view.listview.OnLoadMoreListener;
import com.qckj.dabei.view.listview.OnPullRefreshListener;
import com.qckj.dabei.view.listview.PullRefreshView;
import com.qckj.dabei.view.webview.BrowserActivity;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 我的消息界面
 * <p>
 * Created by yangzhizhong on 2019/4/8.
 */
public class MineMessageActivity extends BaseActivity {
    private int curPage = 1;
    public static final int PAGE_SIZE = 10;

    @FindViewById(R.id.action_bar)
    private ActionBar actionBar;

    @FindViewById(R.id.mine_message_list)
    private PullRefreshView pullRefreshView;

    @FindViewById(R.id.no_record)
    private TextView noRecord;

    @Manager
    private UserManager userManager;

    private MessageAdapter messageAdapter;
    String receiveId = "";
    String receiveName = "";
    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MineMessageActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_message);
        ViewInject.inject(this);
        initView();
        initListener();
    }

    private void initListener() {
        actionBar.setOnActionBarClickListener(new ActionBar.OnActionBarClickListener() {
            @Override
            public boolean onActionBarClick(int function) {
                if(function == ActionBar.FUNCTION_TEXT_RIGHT){
                    String url = SystemConfig.webUrl+"/#/chat?userId="+userManager.getCurId()+"&receiveId="+receiveId+"&receiveName="+receiveName;
                    BrowserActivity.startActivity(MineMessageActivity.this,url,false);
                }
                return false;
            }
        });

        messageAdapter.setOnAdapterItemClickListener(new SimpleBaseAdapter.OnAdapterItemClickListener<MessageInfo>() {
            @Override
            public void onAdapterItemClick(int position, MessageInfo messageInfo) {
                if(messageInfo.getType() == 0 ||messageInfo.getType() == 1 ){
                    Intent intent = new Intent(MineMessageActivity.this,SystemMsgActivity.class);
                    startActivity(intent);
                }else{
                    String url =  SystemConfig.webUrl+"/#/chat?userId="+userManager.getCurId()+"&receiveId="+messageInfo.getUserId()+"&receiveName="+messageInfo.getName();
                    BrowserActivity.startActivity(MineMessageActivity.this,url,false);
                }

            }
        });
        pullRefreshView.setOnPullRefreshListener(new OnPullRefreshListener() {
            @Override
            public void onPullDownRefresh(PullRefreshView pullRefreshView) {
                curPage = 1;
                messageAdapter.setDataNull();
                loadData(true);
            }
        });
        pullRefreshView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(PullRefreshView pullRefreshView) {
                loadData(false);
            }
        });
    }

    private void loadData(boolean isRefresh) {

        new AgentInfoRequester(userManager.getCurId(),new OnHttpResponseCodeListener<JSONObject>(){
            @Override
            public void onHttpResponse(boolean isSuccess, JSONObject jsonObject, String message) {
                super.onHttpResponse(isSuccess, jsonObject, message);
                if(isSuccess){
                    actionBar.setRightText("我的代理商");
                    try {
                        JSONObject data = jsonObject.getJSONObject("data");
                        receiveId = data.getString("F_C_USID");
                        receiveName = data.getString("F_C_NAME");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    actionBar.setRightText("");
                }
            }
        }).doPost();

        new GetMessageRequester(curPage, PAGE_SIZE, userManager.getCurId(), new OnHttpResponseCodeListener<List<MessageInfo>>() {
            @Override
            public void onHttpResponse(boolean isSuccess, List<MessageInfo> messageInfos, String message) {
                super.onHttpResponse(isSuccess, messageInfos, message);
                if (isRefresh) pullRefreshView.stopPullRefresh();
                else pullRefreshView.stopLoadMore();

                if (!isSuccess) {
                    showToast(message);
                    pullRefreshView.setLoadMoreEnable(false);
                    return;
                }
                messageAdapter.setData(messageInfos);
                if (messageInfos.size() == 10) {
                    pullRefreshView.setLoadMoreEnable(true);
                    noRecord.setVisibility(View.GONE);
                    curPage++;
                } else if (messageInfos.size() == 0) {
                    noRecord.setVisibility(View.VISIBLE);
                    pullRefreshView.setLoadMoreEnable(false);
                } else {
                    noRecord.setVisibility(View.GONE);
                    pullRefreshView.setLoadMoreEnable(false);
                }
            }

            @Override
            public void onLocalErrorResponse(int code) {
                super.onLocalErrorResponse(code);
                pullRefreshView.setLoadMoreEnable(false);
                if (isRefresh) pullRefreshView.stopPullRefresh();
                else pullRefreshView.stopLoadMore();
            }
        }).doPost();
    }

    private void initView() {
        messageAdapter = new MessageAdapter(this);
        pullRefreshView.setAdapter(messageAdapter);
        pullRefreshView.setPullRefreshEnable(true);
        pullRefreshView.setLoadMoreEnable(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        pullRefreshView.startPullRefresh();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
