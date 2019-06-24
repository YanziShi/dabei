package com.qckj.dabei.ui.mine.contact;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qckj.dabei.R;
import com.qckj.dabei.app.SimpleBaseAdapter;
import com.qckj.dabei.model.contact.ContactInfo;
import com.qckj.dabei.util.inject.FindViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * 紧急联系人是适配器
 * <p>
 * Created by yangzhizhong on 2019/3/20.
 */
public class EmergencyContactAdapter extends SimpleBaseAdapter<ContactInfo, EmergencyContactAdapter.ViewHolder> {


    public EmergencyContactAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.emergency_contact_item_view;
    }

    @Override
    protected void bindView(ViewHolder viewHolder, ContactInfo contactInfo, int position) {
        viewHolder.contactName.setText(contactInfo.getEmergency_linkman());
        viewHolder.contactPhone.setText(contactInfo.getEmergency_linkman_phone());
    }

    @NonNull
    @Override
    protected EmergencyContactAdapter.ViewHolder onNewViewHolder() {
        return new ViewHolder();
    }

     class ViewHolder {
        @FindViewById(R.id.contact_name)
        TextView contactName;
         @FindViewById(R.id.contact_phone)
        TextView contactPhone;
    }
}
