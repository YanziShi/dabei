package com.qckj.dabei.ui.main.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.qckj.dabei.app.App;
import com.qckj.dabei.manager.mine.user.UserManager;
import com.qckj.dabei.model.home.HomeBannerInfo;
import com.qckj.dabei.ui.main.MainActivity;
import com.qckj.dabei.ui.mine.user.LoginActivity;
import com.qckj.dabei.view.banner.AutoBannerAdapter;
import com.qckj.dabei.view.webview.BrowserActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangzhizhong on 2019/3/23.
 */
public class HomeBannerAdapter extends AutoBannerAdapter {

    private Context context;
    private List<HomeBannerInfo> homeBannerInfoList;

    public HomeBannerAdapter(Context context) {
        this.context = context;
        homeBannerInfoList = new ArrayList<>();
    }

    public void changeItems(List<HomeBannerInfo> bannerInfoList) {
        this.homeBannerInfoList.clear();
        this.homeBannerInfoList.addAll(bannerInfoList);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return homeBannerInfoList.size();
    }

    @Override
    protected HomeBannerInfo getItem(int position) {
        return homeBannerInfoList.get(position);
    }

    @Override
    public View getView(View convertView, final int position) {
        ImageView imageView;
        if (convertView != null) {
            imageView = (ImageView) convertView;
        } else {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setBackgroundColor(Color.parseColor("#f7f7f7"));
        }
        Glide.with(context).load(homeBannerInfoList.get(position).getImgUrl()).into(imageView);
        imageView.setOnClickListener(view -> {
            if(position==0 ){
                UserManager userManager = App.getInstance().getManager(UserManager.class);
                if (!userManager.isLogin()) {
                    LoginActivity.startActivity((Activity) context);
                    return;
                }
                BrowserActivity.startActivity(context,homeBannerInfoList.get(position).getUrl()+"?userId="+userManager.getCurId(),false);
            } else BrowserActivity.startActivity(context,homeBannerInfoList.get(position).getUrl(),false);
        });
        return imageView;
    }

}