package com.qckj.dabei.ui.mine.order.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qckj.dabei.R;
import com.qckj.dabei.app.SimpleBaseAdapter;
import com.qckj.dabei.manager.mine.order.FinishXuqiuRequester;
import com.qckj.dabei.model.mine.MineTripInfo;
import com.qckj.dabei.model.mine.ParticipantInfo;
import com.qckj.dabei.ui.mine.order.ReleaseDetailActivity;
import com.qckj.dabei.util.inject.FindViewById;

/**
 * Created by yangzhizhong on 2019/6/1.
 */
public class ParticipantAdapter extends SimpleBaseAdapter<ParticipantInfo, ParticipantAdapter.ViewHolder> {

    protected ParticipantListener participantListener;
    public interface ParticipantListener {
        void isFinish(String userOrderId);
        void isEvaluate(String userOrderId);
    }
    public void setParticipantListener(ParticipantListener listener){
        participantListener = listener;
    }

    public ParticipantAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_participant_view;
    }

    @Override
    protected void bindView(ViewHolder viewHolder, ParticipantInfo participantInfo, int position) {
        Glide.with(getContext()).load(participantInfo.getImg()).into(viewHolder.imagePhoto);
        viewHolder.textPhone.setText(participantInfo.getPhone());

        if(participantInfo.getStatus().equals("0")) viewHolder.btnCompleteEvaluate.setText("确认完成");
        else viewHolder.btnCompleteEvaluate.setText("评价");

        viewHolder.btnCompleteEvaluate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(participantInfo.getStatus().equals("0")){
                    participantListener.isFinish(participantInfo.getId());
                }else {
                    participantListener.isEvaluate(participantInfo.getId());
                }
            }
        });
    }

    @NonNull
    @Override
    protected ViewHolder onNewViewHolder() {
        return new ViewHolder();
    }

    static class ViewHolder {

        @FindViewById(R.id.image_photo)
        private ImageView imagePhoto;

        @FindViewById(R.id.text_phone)
        private TextView textPhone;

        @FindViewById(R.id.btn_complete_evaluate)
        private Button btnCompleteEvaluate;
    }
}
