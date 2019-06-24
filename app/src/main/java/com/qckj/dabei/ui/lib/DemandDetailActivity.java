package com.qckj.dabei.ui.lib;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.ui.lib.adapter.ImagesAdapter;
import com.qckj.dabei.view.dialog.MsgDialog;
import com.qckj.dabei.manager.lib.DemandDetailRequester;
import com.qckj.dabei.manager.lib.ProvideXuqiuServiceRequester;
import com.qckj.dabei.manager.mine.user.UserManager;
import com.qckj.dabei.model.lib.DemandDetailInfo;
import com.qckj.dabei.ui.mine.order.MineOrderActivity;
import com.qckj.dabei.ui.mine.user.LoginActivity;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.OnClick;
import com.qckj.dabei.util.inject.ViewInject;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @name: 梁永胜
 * @date ：2018/11/2 14:14
 * E-Mail Address：875450820@qq.com
 */
public class DemandDetailActivity extends BaseActivity{

    @FindViewById(R.id.text_title)
    private TextView textTitle;

    @FindViewById(R.id.text_price)
    private TextView textPrice;

    @FindViewById(R.id.text_time)
    private TextView textTime;

    @FindViewById(R.id.text_content)
    private TextView textContent;

    @FindViewById(R.id.grid_view)
    private GridView gridView;

    @FindViewById(R.id.btn_service)
    private Button btnService;

    @FindViewById(R.id.text_hint)
    private TextView textHint;

    @FindViewById(R.id.image_big)
    private ImageView imageBig;

    ImagesAdapter adapter;
    List<String> imageList = new ArrayList<>();

    @Manager
    UserManager userManager;
    DemandDetailInfo demandDetailBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demand_detail);
        ViewInject.inject(this);
        adapter = new ImagesAdapter(this);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                imageBig.setVisibility(View.VISIBLE);
                Glide.with(getBaseContext()).load(imageList.get(position)).into(imageBig);
            }
        });
        if(getIntent().getBooleanExtra("hideServer",false)) {
            btnService.setVisibility(View.GONE);
            textHint.setVisibility(View.GONE);
        } else {
            btnService.setVisibility(View.VISIBLE);
            textHint.setVisibility(View.VISIBLE);
        }
        loadData();
    }

    private void loadData(){
        showLoadingProgressDialog();
        new DemandDetailRequester(getIntent().getStringExtra("id"),
                new OnHttpResponseCodeListener<List<DemandDetailInfo>>(){
                    @Override
                    public void onHttpResponse(boolean isSuccess, List<DemandDetailInfo> demandDetailBeans, String message) {
                        super.onHttpResponse(isSuccess, demandDetailBeans, message);
                        dismissLoadingProgressDialog();
                        if(isSuccess && demandDetailBeans!=null && demandDetailBeans.size()>0){
                            demandDetailBean  = demandDetailBeans.get(0);
                            initView();
                        }else{
                            showToast(message);
                        }
                    }

                    @Override
                    public void onLocalErrorResponse(int code) {
                        super.onLocalErrorResponse(code);
                    }

                }).doPost();
    }

    private void initView(){
        textTitle.setText(demandDetailBean.getF_C_NAME());
        textContent.setText(demandDetailBean.getF_C_MES());
        textPrice.setText("赏金:￥"+String.valueOf(demandDetailBean.getF_I_MONEY()));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        textTime.setText(format.format(new Date(demandDetailBean.getF_D_TIME())));
        String[] iamges = demandDetailBean.getF_C_IMGS().split(",");
        for(String s: iamges){
            imageList.add(s);
        }
        adapter.addData(imageList);
    }

    @OnClick({R.id.btn_contact,R.id.btn_service,R.id.image_big})
    private void onViewClick(View v){
        switch (v.getId()){
            case R.id.btn_contact:
                MsgDialog dlg = new MsgDialog(getActivity());
                dlg.show(demandDetailBean.getF_C_PHONE(),"","拨打",false);
                dlg.setOnPositiveListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dlg.dismiss();
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        Uri data = Uri.parse("tel:" +demandDetailBean.getF_C_PHONE() );
                        intent.setData(data);
                        startActivity(intent);
                    }
                });

                break;
            case R.id.btn_service:
                if (userManager.isLogin()) {
                    MsgDialog dialog = new MsgDialog(getActivity());
                    dialog.show("确定可以提供服务！","","确定",false);
                    dialog.setOnPositiveListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            new ProvideXuqiuServiceRequester(userManager.getCurId(),demandDetailBean.getF_C_ID(),new OnHttpResponseCodeListener<JSONObject>(){
                                @Override
                                public void onHttpResponse(boolean isSuccess, JSONObject jsonObject, String message) {
                                    super.onHttpResponse(isSuccess, jsonObject, message);
                                    if(isSuccess) MineOrderActivity.startActivity(getActivity(),1);
                                    else showToast(message);
                                }

                                @Override
                                public void onLocalErrorResponse(int code) {
                                    super.onLocalErrorResponse(code);
                                }
                            }).doPost();
                        }
                    });
                } else LoginActivity.startActivity(getActivity());
                break;
            case R.id.image_big:
                imageBig.setVisibility(View.GONE);
                break;
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
