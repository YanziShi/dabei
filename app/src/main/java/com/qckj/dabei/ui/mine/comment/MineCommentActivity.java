package com.qckj.dabei.ui.mine.comment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.manager.mine.comment.GetCommentRequester;
import com.qckj.dabei.manager.mine.user.UserManager;
import com.qckj.dabei.model.comment.CommentInfo;
import com.qckj.dabei.ui.mine.comment.adapter.CommentAdapter;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.ViewInject;
import com.qckj.dabei.view.listview.OnLoadMoreListener;
import com.qckj.dabei.view.listview.OnPullRefreshListener;
import com.qckj.dabei.view.listview.PullRefreshView;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

/**
 * 我的评论
 * <p>
 * Created by yangzhizhong on 2019/4/10.
 */
public class MineCommentActivity extends BaseActivity {

    public static final int PAGE_SIZE = 10;

    @FindViewById(R.id.pull_list_view)
    private PullRefreshView pullRefreshView;

    @Manager
    private UserManager userManager;

    private int curPage = 1;
    private CommentAdapter commentAdapter;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MineCommentActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_comment);
        ViewInject.inject(this);
        init();
        initListener();
    }

    private void init() {
        commentAdapter = new CommentAdapter(getActivity());
        pullRefreshView.setAdapter(commentAdapter);
        pullRefreshView.setPullRefreshEnable(true);
        pullRefreshView.setLoadMoreEnable(false);
    }

    private void initListener() {
        pullRefreshView.setOnPullRefreshListener(new OnPullRefreshListener() {
            @Override
            public void onPullDownRefresh(PullRefreshView pullRefreshView) {
                curPage = 1;
                commentAdapter.setDataNull();
                loadData(true);
            }
        });

        pullRefreshView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(PullRefreshView pullRefreshView) {
                loadData(false);
            }
        });
        pullRefreshView.startPullRefresh();
    }


    private void loadData(boolean isRefresh) {
        new GetCommentRequester(curPage, PAGE_SIZE, userManager.getCurId(), new OnHttpResponseCodeListener<List<CommentInfo>>() {
            @Override
            public void onHttpResponse(boolean isSuccess, List<CommentInfo> commentInfos, String message) {
                super.onHttpResponse(isSuccess, commentInfos, message);
                if (isRefresh) pullRefreshView.stopPullRefresh();
                else pullRefreshView.stopLoadMore();
                if (isSuccess) {
                    commentAdapter.addData(commentInfos);
                    if (commentInfos.size() == 10) {
                        pullRefreshView.setLoadMoreEnable(true);
                        curPage++;
                    } else {
                        pullRefreshView.setLoadMoreEnable(false);
                    }
                } else {
                    showToast(message);
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
