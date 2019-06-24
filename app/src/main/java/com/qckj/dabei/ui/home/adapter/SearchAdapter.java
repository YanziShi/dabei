package com.qckj.dabei.ui.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qckj.dabei.R;
import com.qckj.dabei.app.App;
import com.qckj.dabei.app.SimpleBaseAdapter;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.view.dialog.MsgDialog;
import com.qckj.dabei.manager.home.GoodsServiceRequester;
import com.qckj.dabei.manager.mine.user.UserManager;
import com.qckj.dabei.model.home.SearchAllInfo;
import com.qckj.dabei.ui.mine.user.LoginActivity;
import com.qckj.dabei.util.GlideUtil;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yangzhizhong on 2019/5/6.
 */
public class SearchAdapter extends SimpleBaseAdapter<SearchAllInfo, SearchAdapter.ViewHolder> {
    UserManager userManager;
    public SearchAdapter(Context context) {
        super(context);
        userManager = App.getInstance().getManager(UserManager.class);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.search_item_view;
    }

    @Override
    protected void bindView(ViewHolder viewHolder, SearchAllInfo searchAllInfo, int position) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (searchAllInfo.getType().equals("1")){
            viewHolder.contactBt.setVisibility(View.VISIBLE);
            viewHolder.mTvDistance.setVisibility(View.VISIBLE);
            viewHolder.mTvTime.setVisibility(View.VISIBLE);
            viewHolder.mTvSellerName.setText(searchAllInfo.getShopname());
            viewHolder.mTvContent.setText(searchAllInfo.getMessage());
            viewHolder.contactBt.setText("联系商家");
            viewHolder.mTvContent.setTextColor(getContext().getResources().getColor(R.color.content));
            viewHolder.mTvTime.setText(searchAllInfo.getDistance()+"km");
            viewHolder.mTvDistance.setVisibility(View.GONE);
            viewHolder.contactBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    call(searchAllInfo);
                }
            });
        }else  if (searchAllInfo.getType().equals("2")){
            viewHolder.contactBt.setVisibility(View.VISIBLE);
            viewHolder.mTvDistance.setVisibility(View.VISIBLE);
            viewHolder.mTvTime.setVisibility(View.VISIBLE);
            viewHolder.mTvSellerName.setText(searchAllInfo.getNeedname());
            viewHolder.mTvContent.setText(searchAllInfo.getMes());
            viewHolder.mTvContent.setTextColor(getContext().getResources().getColor(R.color.content));
            viewHolder.contactBt.setText("提供服务");
            viewHolder.mTvDistance.setText(searchAllInfo.getDistance()+"km");
            viewHolder.mTvTime.setText(format.format(new Date(searchAllInfo.getTime())));
            viewHolder.contactBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getService(searchAllInfo);
                }
            });
        }else {
            viewHolder.mTvSellerName.setText(searchAllInfo.getGoodsname());
            viewHolder.mTvContent.setText("¥"+searchAllInfo.getMoney());
            viewHolder.mTvContent.setTextColor(getContext().getResources().getColor(R.color.table_color));
            viewHolder.contactBt.setVisibility(View.GONE);
            viewHolder.mTvDistance.setVisibility(View.GONE);
            viewHolder.mTvTime.setVisibility(View.GONE);
        }
        GlideUtil.displayImage(getContext(), searchAllInfo.getImg(), viewHolder.imageView, R.mipmap.ic_launcher);

        if(searchAllInfo.getIsjpsj()==1) viewHolder.imageIsGold.setVisibility(View.VISIBLE);
        else viewHolder.imageIsGold.setVisibility(View.GONE);

        if(searchAllInfo.getIsdpzd()==1) viewHolder.imageIsTop.setVisibility(View.VISIBLE);
        else viewHolder.imageIsTop.setVisibility(View.GONE);

        if(searchAllInfo.getBusiness_state()==0) {
            viewHolder.tvIsRest.setVisibility(View.VISIBLE);
            viewHolder.contactBt.setEnabled(false);
        }
        else {
            viewHolder.tvIsRest.setVisibility(View.GONE);
            viewHolder.contactBt.setEnabled(true);
        }

    }

    @NonNull
    @Override
    protected ViewHolder onNewViewHolder() {
        return new ViewHolder();
    }

    static class ViewHolder {
        @FindViewById(R.id.image_icon)
        ImageView imageView;
        @FindViewById(R.id.tv_name)
        TextView mTvSellerName;
        @FindViewById(R.id.tv_content)
        TextView mTvContent;
        @FindViewById(R.id.tv_distance)
        TextView mTvDistance;
        @FindViewById(R.id.tv_time)
        TextView mTvTime;
        @FindViewById(R.id.btn_contact)
        Button contactBt;

        @FindViewById(R.id.image_is_gold)
        private ImageView imageIsGold;

        @FindViewById(R.id.image_is_top)
        private ImageView imageIsTop;

        @FindViewById(R.id.tv_is_rest)
        private TextView tvIsRest;
    }

    private void getService(final SearchAllInfo searchAllInfo) {
        MsgDialog dialog = new MsgDialog(getContext());
        if (userManager!=null && userManager.isLogin()) {
            dialog.show("确认可以提供帮助", "", "确定", false);
            dialog.setOnNegativeListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.setOnPositiveListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //提供服务...
                    new GoodsServiceRequester(userManager.getCurId(),searchAllInfo.getSid(),
                            new OnHttpResponseCodeListener<JSONObject>(){
                                @Override
                                public void onHttpResponse(boolean isSuccess, JSONObject jsonObject, String message) {
                                    super.onHttpResponse(isSuccess, jsonObject, message);
                                    dialog.dismiss();
                                    Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
                                    if(isSuccess){

                                    }
                                }

                                @Override
                                public void onLocalErrorResponse(int code) {
                                    super.onLocalErrorResponse(code);
                                    dialog.dismiss();
                                }
                            }).doPost();

                }
            });
        } else {
            goLogin();
        }

    }
    private void call(final SearchAllInfo searchAllInfo){
        MsgDialog dialog = new MsgDialog(getContext());
        if (userManager!=null&&userManager.isLogin()){
            // 设置返回键无效
            dialog.setCancelable(false);
            dialog.setOnNegativeListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.setOnPositiveListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    Uri data = Uri.parse("tel:" + searchAllInfo.getPhone());
                    intent.setData(data);
                    getContext().startActivity(intent);
                    dialog.dismiss();
                }
            });
            dialog.show(searchAllInfo.getPhone(),"","拨打",false);
        }else {
            goLogin();
        }

    }

    private void goLogin(){
        MsgDialog dialog = new MsgDialog(getContext());
        // 设置返回键无效
        dialog.setCancelable(false);
        dialog.setOnNegativeListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setOnPositiveListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                getContext().startActivity(intent);
                dialog.dismiss();
            }
        });
        dialog.show("需要登录账号","","去登录",false);
    }
}
