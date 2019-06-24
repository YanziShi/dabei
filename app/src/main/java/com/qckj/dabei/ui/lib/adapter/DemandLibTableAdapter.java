package com.qckj.dabei.ui.lib.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qckj.dabei.R;
import com.qckj.dabei.app.SimpleBaseAdapter;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.view.dialog.MsgDialog;
import com.qckj.dabei.manager.lib.ProvideXuqiuServiceRequester;
import com.qckj.dabei.manager.mine.user.UserManager;
import com.qckj.dabei.model.lib.DemandLibInfo;
import com.qckj.dabei.ui.mine.order.MineOrderActivity;
import com.qckj.dabei.ui.mine.user.LoginActivity;
import com.qckj.dabei.util.DateUtils;
import com.qckj.dabei.util.GlideUtil;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.view.image.CircleImageView;

import org.json.JSONObject;

/**
 * 需求库适配器
 * <p>
 * Created by yangzhizhong on 2019/4/9.
 */
public class DemandLibTableAdapter extends SimpleBaseAdapter<DemandLibInfo, DemandLibTableAdapter.ViewHolder> {

    UserManager userManager;

    public DemandLibTableAdapter(Context context) {
        super(context);
    }

    public void setUserManager(UserManager userManager){
        this.userManager = userManager;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.demand_lib_item_view;
    }

    @Override
    protected void bindView(ViewHolder viewHolder, DemandLibInfo demandLibInfo, int position) {
        GlideUtil.displayImage(getContext(), demandLibInfo.getTxImg(), viewHolder.icon, R.mipmap.ic_launcher);
        viewHolder.name.setText(demandLibInfo.getNicheng());
        String content = "【"+demandLibInfo.getName()+"】"+demandLibInfo.getMes();
        viewHolder.content.setText(content);
        viewHolder.distance.setText(demandLibInfo.getDistance() + "km");
        viewHolder.time.setText(DateUtils.getTimeStringByMillisecondsWithFormatString(demandLibInfo.getTime(), "yyyy-MM-dd"));
        viewHolder.groupImages.removeAllViews();
        if (demandLibInfo.getImgUrl() != null && !demandLibInfo.getImgUrl().equals("")) {
            viewHolder.groupImages.setVisibility(View.VISIBLE);
            String[] urls = demandLibInfo.getImgUrl().split(",");
            for (String url : urls) {
                ImageView imageView = new ImageView(getContext());
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                param.width = 200;
                param.height = 150;
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                param.setMargins(0, 0, 20, 0); // Margin
                GlideUtil.displayImage(getContext(), url, imageView, R.mipmap.ic_launcher);
                viewHolder.groupImages.addView(imageView, param);
            }
        } else viewHolder.groupImages.setVisibility(View.GONE);
        viewHolder.btnService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userManager.isLogin()) {
                    MsgDialog dialog = new MsgDialog(getContext());
                    dialog.show("确定可以提供服务！","","确定",false);
                    dialog.setOnPositiveListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            new ProvideXuqiuServiceRequester(userManager.getCurId(),demandLibInfo.getId(),new OnHttpResponseCodeListener<JSONObject>(){
                                @Override
                                public void onHttpResponse(boolean isSuccess, JSONObject jsonObject, String message) {
                                    super.onHttpResponse(isSuccess, jsonObject, message);
                                    if(isSuccess)MineOrderActivity.startActivity(getContext(),1);
                                    else Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onLocalErrorResponse(int code) {
                                    super.onLocalErrorResponse(code);
                                }
                            }).doPost();
                        }
                    });
                } else LoginActivity.startActivity((Activity) getContext());
            }
        });
    }

    @NonNull
    @Override
    protected ViewHolder onNewViewHolder() {
        return new ViewHolder();
    }

    static class ViewHolder {
        @FindViewById(R.id.icon)
        private CircleImageView icon;
        @FindViewById(R.id.name)
        private TextView name;
        @FindViewById(R.id.content)
        private TextView content;
        @FindViewById(R.id.distance)
        private TextView distance;
        @FindViewById(R.id.btn_service)
        private Button btnService;
        @FindViewById(R.id.time)
        private TextView time;
        @FindViewById(R.id.group_images)
        private LinearLayout groupImages;
    }
}
