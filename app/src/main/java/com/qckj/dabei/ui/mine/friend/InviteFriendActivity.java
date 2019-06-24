package com.qckj.dabei.ui.mine.friend;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.manager.mine.friend.InviteFriendRequester;
import com.qckj.dabei.manager.mine.user.UserManager;
import com.qckj.dabei.model.SharedAppInfo;
import com.qckj.dabei.model.mine.InviteFriendInfo;
import com.qckj.dabei.util.ImgUtils;
import com.qckj.dabei.util.ZXingUtils;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.OnClick;
import com.qckj.dabei.util.inject.ViewInject;
import com.qckj.dabei.view.ActionBar;
import com.qckj.dabei.view.dialog.AppShareDialog;
import com.qckj.dabei.view.listview.OnLoadMoreListener;
import com.qckj.dabei.view.listview.OnPullRefreshListener;
import com.qckj.dabei.view.listview.PullRefreshView;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by yangzhizhong on 2019/5/30.
 */
public class InviteFriendActivity extends BaseActivity {
    @FindViewById(R.id.action_bar)
    ActionBar actionBar;

    @FindViewById(R.id.image_code)
    ImageView imageCode;

    @FindViewById(R.id.text_num)
    TextView textNum;

    @FindViewById(R.id.list_view)
    PullRefreshView pullRefreshView;

    @Manager
    UserManager userManager;
    FriendAdapter adapter;
    private int curPage = 1;
    public static final int PAGE_SIZE = 10;
    String url;
    String content = "";
    Bitmap bitmap;

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            bitmap = (Bitmap) msg.obj;
            imageCode.setImageBitmap(bitmap);
        }
    };

    static public void startActivity(Context context) {
        Intent intent = new Intent(context, InviteFriendActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friend);
        ViewInject.inject(this);
        adapter = new FriendAdapter(this);
        pullRefreshView.setAdapter(adapter);
        pullRefreshView.setPullRefreshEnable(true);
        pullRefreshView.setLoadMoreEnable(false);
        pullRefreshView.setOnPullRefreshListener(new OnPullRefreshListener() {
            @Override
            public void onPullDownRefresh(PullRefreshView pullRefreshView) {
                curPage = 1;
                adapter.setDataNull();
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

        content = "大贝网车，为您提供最优质的出行服务，一个可以赚钱的出行APP ";
        url = "http://invitation.dabeiinfo.com/dbH5-share/#/invite/" + userManager.getCurId()+"?type=user";
        actionBar.setOnActionBarClickListener(new ActionBar.OnActionBarClickListener() {
            @Override
            public boolean onActionBarClick(int function) {
                if (function == ActionBar.FUNCTION_TEXT_RIGHT) {
                    if (bitmap != null) goShare();
                }
                return false;
            }
        });
        new Thread() {
            @Override
            public void run() {
                // 在新的进程中实现图片的加载
                super.run();
                //从url中获得bitmap，将bitmap发送给主线程
                //Bitmap bmp= BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.ic_wechat);
                Bitmap mBitmap = ZXingUtils.createQRImage(url, 400, 400);
                Message message = Message.obtain();
                message.obj = mBitmap;
                mHandler.sendMessage(message);
            }
        }.start();
    }

    private void loadData(boolean isRefresh) {

        new InviteFriendRequester(userManager.getCurId(),curPage, PAGE_SIZE,  new OnHttpResponseCodeListener<InviteFriendInfo>() {
            @Override
            public void onHttpResponse(boolean isSuccess, InviteFriendInfo inviteFriendInfo, String message) {
                super.onHttpResponse(isSuccess, inviteFriendInfo, message);

                if (isRefresh) pullRefreshView.stopPullRefresh();
                else pullRefreshView.stopLoadMore();
                if (!isSuccess) {
                    pullRefreshView.setLoadMoreEnable(false);
                    showToast(message);
                    return;
                }
                if(inviteFriendInfo.getList()==null) return;
                textNum.setText("邀请好友列表("+inviteFriendInfo.getCount()+")");
                adapter.addData(inviteFriendInfo.getList());
                if (inviteFriendInfo.getList().size() == 10) {
                    pullRefreshView.setLoadMoreEnable(true);
                    curPage++;
                } else if (inviteFriendInfo.getList().size() == 0) {
                    pullRefreshView.setLoadMoreEnable(false);
                } else {
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

    @OnClick(R.id.btn_save)
    void onViewClick(View view) {
        //保存图片
        boolean isSaveSuccess = ImgUtils.saveImageToGallery(InviteFriendActivity.this, bitmap);
        if (isSaveSuccess) {
            showToast( "保存图片成功");
        } else {
            showToast( "保存图片失败，请稍后重试");
        }
    }

    private void goShare() {
        SharedAppInfo sharedAppInfo = new SharedAppInfo();
        sharedAppInfo.setCard(false);
        sharedAppInfo.setTitle("大贝邀约");
        sharedAppInfo.setDescribe("下载就有钱,躺着都有收益,一次发展客户,终身享受收益,赶快分享给身边的人一起赚钱吧!");
        sharedAppInfo.setLink(url);
        AppShareDialog dialog = new AppShareDialog(this);
        dialog.setContent(sharedAppInfo.getDescribe());
        dialog.setTitle(sharedAppInfo.getTitle());
        dialog.setContentUrl(sharedAppInfo.getLink());
        dialog.setIconRec(R.mipmap.ic_launcher);
        dialog.setIconUrl(sharedAppInfo.getIconUrl());
        dialog.includeCard(sharedAppInfo.isCard());
        dialog.show();
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
