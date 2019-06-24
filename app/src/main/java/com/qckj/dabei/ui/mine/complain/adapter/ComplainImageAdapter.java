package com.qckj.dabei.ui.mine.complain.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.qckj.dabei.R;
import com.qckj.dabei.app.SimpleBaseAdapter;
import com.qckj.dabei.util.inject.FindViewById;

/**
 * 投诉图片适配器
 * <p>
 * Created by yangzhizhong on 2019/4/12.
 */
public class ComplainImageAdapter extends SimpleBaseAdapter<Bitmap, ComplainImageAdapter.ViewHolder> {

    public ComplainImageAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.complain_image_item_view;
    }

    @Override
    protected void bindView(ViewHolder viewHolder, Bitmap bitmap, int position) {
        viewHolder.getIv.setImageBitmap(bitmap);
        viewHolder.subIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @NonNull
    @Override
    protected ViewHolder onNewViewHolder() {
        return new ViewHolder();
    }

    static class ViewHolder {

        @FindViewById(R.id.get_iv)
        private ImageView getIv;

        @FindViewById(R.id.sub_iv)
        private ImageView subIv;
    }
}
