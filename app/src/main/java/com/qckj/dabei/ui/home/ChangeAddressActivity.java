package com.qckj.dabei.ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amap.api.services.district.DistrictItem;
import com.amap.api.services.district.DistrictResult;
import com.amap.api.services.district.DistrictSearch;
import com.amap.api.services.district.DistrictSearchQuery;
import com.qckj.dabei.R;
import com.qckj.dabei.ui.home.adapter.CityAdapter;
import com.qckj.dabei.ui.home.adapter.DistrictAdapter;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.manager.location.GaoDeLocationManager;
import com.qckj.dabei.ui.main.fragment.HomeFragment;
import com.qckj.dabei.util.CommonUtils;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.ViewInject;
import com.qckj.dabei.view.ActionBar;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @name: 梁永胜
 * @date ：2018/10/12 09:32
 * E-Mail Address：875450820@qq.com
 */
public class ChangeAddressActivity extends BaseActivity implements DistrictSearch.OnDistrictSearchListener {
    @FindViewById(R.id.action_bar)
    ActionBar actionBar;

    @FindViewById(R.id.adress_gridView)
    GridView adressGridView;

    @FindViewById(R.id.now_adress_tv)
    TextView nowAdressTv;

    @FindViewById(R.id.now_district_tv)
    TextView nowDistrictTv;

    @FindViewById(R.id.district_gridView)
    GridView districtGridView;

    @FindViewById(R.id.group_content)
    ViewGroup groupContent;

    private List<DistrictItem> cityItems = new ArrayList<>();
    private List<DistrictItem> districtItems = new ArrayList<>();

    private CityAdapter locationAdapter;
    private DistrictAdapter districtAdapter;


    private String province;
    private String city;
    private String district;
    private double latitude;
    private double longitude;
    private DistrictSearch search;
    private DistrictSearchQuery query;
    protected static final int RERULT_CODE_ADDRESS = 1;

    public static void startActivity(Activity context, String province, String city, String district){
        Intent intent = new Intent(context, ChangeAddressActivity.class);
        intent.putExtra("province",province);
        intent.putExtra("city",city);
        intent.putExtra("district",district);
        context.startActivityForResult(intent,1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_adress);
        ViewInject.inject(this);
        initParms();
    }

    private void getCity(String cityName) {
        showLoadingProgressDialog();
        query.setKeywords(cityName);//传入关键字
        query.setShowBoundary(true);//是否返回边界值
        search.setQuery(query);
        search.setOnDistrictSearchListener(this);//绑定监听器
        search.searchDistrictAnsy();//开始搜索
    }

    public void initParms() {
        province =  getIntent().getStringExtra("province");
        city =  getIntent().getStringExtra("city");
        district =  getIntent().getStringExtra("district");

        search = new DistrictSearch(this);
        query = new DistrictSearchQuery();

        nowDistrictTv.setText(district);
        nowAdressTv.setText(city);
        locationAdapter = new CityAdapter(this, cityItems);
        adressGridView.setAdapter(locationAdapter);
        adressGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                nowAdressTv.setText(cityItems.get(i).getName());
                getCity(cityItems.get(i).getName());
                city = cityItems.get(i).getName();

            }
        });
        districtAdapter = new DistrictAdapter(this, districtItems);
        districtGridView.setAdapter(districtAdapter);
        districtGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                nowDistrictTv.setText(districtItems.get(i).getName());
                district = districtItems.get(i).getName();
                latitude = districtItems.get(i).getCenter().getLatitude();
                longitude = districtItems.get(i).getCenter().getLongitude();
            }
        });
        actionBar.setOnActionBarClickListener(new ActionBar.OnActionBarClickListener() {
            @Override
            public boolean onActionBarClick(int function) {
                if(function==ActionBar.FUNCTION_TEXT_RIGHT){
                    backResult();
                }
                return false;
            }
        });

        getCity(province);
    }

    private  void backResult(){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("city",city);
            jsonObject.put("district",district);
            jsonObject.put("latitude",latitude);
            jsonObject.put("longitude",longitude);
            EventBus.getDefault().post(jsonObject);
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDistrictSearched(DistrictResult districtResult) {
        dismissLoadingProgressDialog();
        int code = districtResult.getAMapException().getErrorCode();
        if (code == 1000) {
            groupContent.setVisibility(View.VISIBLE);
            List<DistrictItem> allDistrictItems = districtResult.getDistrict();
            String level = allDistrictItems.get(0).getLevel();

            if (level.equals("province")) {
                cityItems.clear();
                getCity(city);
                for (int i = 0;i <allDistrictItems.size();i++){
                    cityItems = allDistrictItems.get(i).getSubDistrict();
                }
                locationAdapter = new CityAdapter(this, cityItems);
                adressGridView.setAdapter(locationAdapter);
            } else {
                districtItems.clear();

                for (int i = 0;i <allDistrictItems.size();i++){
                    districtItems = allDistrictItems.get(i).getSubDistrict();
                    nowDistrictTv.setText(districtItems.get(0).getName());
                    district = districtItems.get(0).getName();
                    latitude = districtItems.get(0).getCenter().getLatitude();
                    longitude = districtItems.get(0).getCenter().getLongitude();
                }
                districtAdapter = new DistrictAdapter(this, districtItems);
                districtGridView.setAdapter(districtAdapter);
            }
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
