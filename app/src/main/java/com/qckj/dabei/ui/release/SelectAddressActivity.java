package com.qckj.dabei.ui.release;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.app.SimpleBaseAdapter;
import com.qckj.dabei.ui.release.adapter.SelectAddressAdapter;
import com.qckj.dabei.util.DataConversionUtils;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.OnClick;
import com.qckj.dabei.util.inject.ViewInject;
import com.qckj.dabei.view.listview.PullRefreshView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * @name: 梁永胜
 * @date ：2019/2/25 00:31
 * E-Mail Address：875450820@qq.com
 */
public class SelectAddressActivity extends BaseActivity {
    @FindViewById(R.id.mapview)
    MapView mMapView;
    @FindViewById(R.id.location_img)
    ImageView mIvLocation;
    @FindViewById(R.id.iv_center_location)
    ImageView mIvCenterLocation;
    @FindViewById(R.id.lv_content)
    PullRefreshView listView;
    @FindViewById(R.id.pb_loading)
    ProgressBar mProgressBar;
    private SelectAddressAdapter mAddressAdapter;
    private List<PoiItem> mList;
    private PoiItem userSelectPoiItem;

    private AMap mAMap;
    private Marker mLocationGpsMarker, mSelectByListMarker;
    private UiSettings mUiSettings;
    private PoiSearch mPoiSearch;
    private PoiSearch.Query mQuery;
    private boolean isSearchData = false;//是否搜索地址数据
    private int searchAllPageNum;//Poi搜索最大页数，可应用于上拉加载更多
    private int searchNowPageNum;//当前poi搜索页数
    private float zoom = 20;//地图缩放级别

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = new AMapLocationClientOption();
    private AMapLocation location;
    private AMapLocationListener mAMapLocationListener;

    private onPoiSearchLintener mOnPoiSearchListener;
    private View.OnClickListener mOnClickListener;
    private GeocodeSearch.OnGeocodeSearchListener mOnGeocodeSearchListener;

    private ObjectAnimator mTransAnimator;//地图中心标志动态

    private static final int SEARCHREQUESTCODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address);
        ViewInject.inject(this);
        initDatas(savedInstanceState);
        initListener();
    }

    private void initDatas(Bundle savedInstanceState) {
        mMapView.onCreate(savedInstanceState);// 此方法必须重写
        if (mAMap == null) {
            mAMap = mMapView.getMap();
        }
        mUiSettings = mAMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(false);//是否显示地图中放大缩小按钮
        mUiSettings.setMyLocationButtonEnabled(false); // 是否显示默认的定位按钮
        mUiSettings.setScaleControlsEnabled(true);//是否显示缩放级别

        mAMap.moveCamera(CameraUpdateFactory.zoomTo(20));
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.
                fromResource(R.mipmap.location_blue));// 自定义定位蓝点图标*/
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 自定义精度范围的圆形边框颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));//圆圈的颜色,设为透明的时候就可以去掉园区区域了
        mAMap.setMyLocationStyle(myLocationStyle);
        // 初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000); // 设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        mAMap.setMyLocationStyle(myLocationStyle);// 设置定位蓝点的Style
        mAMap.setMyLocationEnabled(false);// 是否可触发定位并显示定位层

        mList = new ArrayList<>();
        mAddressAdapter = new SelectAddressAdapter(this);
        listView.setAdapter(mAddressAdapter);

        mTransAnimator = ObjectAnimator.ofFloat(mIvCenterLocation, "translationY", 0f, -80f, 0f);
        mTransAnimator.setDuration(800);
    }

    private void initListener() {
        //监测地图画面的移动
        mAMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                if (null != location && null != cameraPosition && isSearchData) {
                    mIvLocation.setImageResource(R.mipmap.location_gps_black);
                    zoom = cameraPosition.zoom;
                    if (null != mSelectByListMarker) {
                        mSelectByListMarker.setVisible(false);
                    }
                    getAddressInfoByLatLong(cameraPosition.target.latitude, cameraPosition.target.longitude);
                    startTransAnimator();
                }
                if (!isSearchData) {
                    isSearchData = true;
                }
            }

            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

            }
        });

        //设置触摸地图监听器
        mAMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                isSearchData = true;
            }
        });

        //Poi搜索监听器
        mOnPoiSearchListener = new onPoiSearchLintener();

        //逆地址搜索监听器
        getAddressData();

        //gps定位监听器
        mAMapLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation loc) {
                try {
                    if (null != loc) {

                        if (loc.getErrorCode() == 0) {//可在其中解析amapLocation获取相应内容。
                            location = loc;
                            stopLocation();
                            doWhenLocationSucess();
                            //getAddressData();
                        } else {
                            //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                            Log.e("AmapError", "location Error, ErrCode:"
                                    + loc.getErrorCode() + ", errInfo:"
                                    + loc.getErrorInfo());

                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };

        mAddressAdapter.setOnAdapterItemClickListener(new SimpleBaseAdapter.OnAdapterItemClickListener<PoiItem>() {
            @Override
            public void onAdapterItemClick(int position, PoiItem poiItem) {
                Intent intent = new Intent();
                intent.putExtra("poiItem",poiItem);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        startLocation();
    }

    @OnClick({R.id.search_ll,R.id.location_img})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_ll:
                startActivityForResult(new Intent(SelectAddressActivity.this, SearchAddressActivity.class), SEARCHREQUESTCODE);
                break;
            case R.id.location_img:
                mIvLocation.setImageResource(R.mipmap.location_gps_black);
                if (null != mSelectByListMarker) {
                    mSelectByListMarker.setVisible(false);
                }
                if (null == location) {
                    startLocation();
                } else {
                    doWhenLocationSucess();
                }
                break;

        }
    }

    private void getAddressData(){
        //逆地址搜索监听器
        mOnGeocodeSearchListener = new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                if (i == 1000) {
                    if (regeocodeResult != null) {
                        mProgressBar.setVisibility(View.GONE);
                        userSelectPoiItem = DataConversionUtils.changeToPoiItem(regeocodeResult);
                        if (null != mList) {
                            mList.clear();
                        }
                        mList.addAll(regeocodeResult.getRegeocodeAddress().getPois());
                        if (null != userSelectPoiItem) {
                            mList.add(0, userSelectPoiItem);
                        }
                        mAddressAdapter.setData(mList);
                        listView.smoothScrollToPosition(0);
                    }
                }

            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

            }
        };

    }
    /**
     * 通过经纬度获取当前地址详细信息，逆地址编码
     *
     * @param latitude
     * @param longitude
     */
    private void getAddressInfoByLatLong(double latitude, double longitude) {
        GeocodeSearch geocodeSearch = new GeocodeSearch(this);
        /*
        point - 要进行逆地理编码的地理坐标点。
        radius - 查找范围。默认值为1000，取值范围1-3000，单位米。
        latLonType - 输入参数坐标类型。包含GPS坐标和高德坐标。 可以参考RegeocodeQuery.setLatLonType(String)
        */
        RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(latitude, longitude), 3000, GeocodeSearch.AMAP);
        geocodeSearch.getFromLocationAsyn(query);
        geocodeSearch.setOnGeocodeSearchListener(mOnGeocodeSearchListener);
    }
    /**
     * 移动动画
     */
    private void startTransAnimator() {
        if (null != mTransAnimator && !mTransAnimator.isRunning()) {
            mTransAnimator.start();
        }
    }
    /**
     * 开始定位
     */
    public void startLocation() {
        initLocation();
        // 设置定位参数
        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();

    }
    /**
     * 初始化定位
     */
    private void initLocation() {
        if (null == locationClient) {
            //初始化client
            locationClient = new AMapLocationClient(this.getApplicationContext());
            //设置定位参数
            locationClient.setLocationOption(getDefaultOption());
            // 设置定位监听
            locationClient.setLocationListener(mAMapLocationListener);
        }
    }

    /**
     * 默认的定位参数
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTPS);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setMockEnable(true);//如果您希望位置被模拟，请通过setMockEnable(true);方法开启允许位置模拟
        return mOption;
    }
    /**
     * 停止定位
     */
    private void stopLocation() {
        if (null != locationClient) {
            locationClient.stopLocation();
        }
    }
    /**
     * 当定位成功需要做的事情
     */
    private void doWhenLocationSucess() {
        isSearchData = false;
        userSelectPoiItem = DataConversionUtils.changeToPoiItem(location);
        doSearchQuery(true, "", location.getCity(), new LatLonPoint(location.getLatitude(), location.getLongitude()));
        moveMapCamera(location.getLatitude(), location.getLongitude());
        refleshLocationMark(location.getLatitude(), location.getLongitude());
    }
    /**
     * 开始进行poi搜索
     *
     * @param isReflsh 是否为刷新数据
     * @param keyWord
     * @param city
     * @param lpTemp
     */
    protected void doSearchQuery(boolean isReflsh, String keyWord, String city, LatLonPoint lpTemp) {
        mQuery = new PoiSearch.Query(keyWord, "", city);//第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        mQuery.setPageSize(30);// 设置每页最多返回多少条poiitem
        if (isReflsh) {
            searchNowPageNum = 0;
        } else {
            searchNowPageNum++;
        }
        if (searchNowPageNum > searchAllPageNum) {
            return;
        }
        mQuery.setPageNum(searchNowPageNum);// 设置查第一页


        mPoiSearch = new PoiSearch(this, mQuery);
        mOnPoiSearchListener.IsReflsh(isReflsh);
        mPoiSearch.setOnPoiSearchListener(mOnPoiSearchListener);
        if (lpTemp != null) {
            mPoiSearch.setBound(new PoiSearch.SearchBound(lpTemp, 10000, true));//该范围的中心点-----半径，单位：米-----是否按照距离排序
        }
        mPoiSearch.searchPOIAsyn();// 异步搜索
    }
    /**
     * 把地图画面移动到定位地点(使用moveCamera方法没有动画效果)
     *
     * @param latitude
     * @param longitude
     */
    private void moveMapCamera(double latitude, double longitude) {
        if (null != mAMap) {
            mAMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), zoom));
        }
    }

    /**
     * 刷新地图标志物gps定位位置
     *
     * @param latitude
     * @param longitude
     */
    private void refleshLocationMark(double latitude, double longitude) {
        if (mLocationGpsMarker == null) {
            mLocationGpsMarker = mAMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                            .decodeResource(getResources(), R.mipmap.location_blue)))
                    .draggable(true));
        }
        mLocationGpsMarker.setPosition(new LatLng(latitude, longitude));
        mAMap.invalidate();

    }
    //重写Poi搜索监听器，可扩展上拉加载数据，下拉刷新
    class onPoiSearchLintener implements PoiSearch.OnPoiSearchListener {
        private boolean isReflsh;//是为下拉刷新，否为上拉加载更多

        public void IsReflsh(boolean isReflsh) {
            this.isReflsh = isReflsh;
        }

        @Override
        public void onPoiSearched(PoiResult result, int i) {
            if (i == 1000) {
                mProgressBar.setVisibility(View.GONE);
                if (result != null && result.getQuery() != null) {// 搜索poi的结果
                    searchAllPageNum = result.getPageCount();
                    if (result.getQuery().equals(mQuery)) {// 是否是同一条
                        if (isReflsh && null != mList) {
                            mList.clear();
                            if (null != userSelectPoiItem) {
                                mList.add(0, userSelectPoiItem);
                            }
                        }
                        mList.addAll(result.getPois());// 取得第一页的poiitem数据，页数从数字0开始
                        if (null != mAddressAdapter) {
                            mAddressAdapter.setData(mList);
                            listView.smoothScrollToPosition(0);
                        }
                    }
                }
            }
        }

        @Override
        public void onPoiItemSearched(PoiItem poiItem, int i) {

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null != data && SEARCHREQUESTCODE == requestCode) {
            try {
                userSelectPoiItem = (PoiItem) data.getParcelableExtra("poiItem");
                if (null != userSelectPoiItem) {
                    isSearchData = false;
                    doSearchQuery(true, "", location.getCity(), userSelectPoiItem.getLatLonPoint());
                    moveMapCamera(userSelectPoiItem.getLatLonPoint().getLatitude(), userSelectPoiItem.getLatLonPoint().getLongitude());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
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
