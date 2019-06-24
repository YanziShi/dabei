package com.qckj.dabei.ui.release;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.app.SimpleBaseAdapter;
import com.qckj.dabei.manager.location.GaoDeLocationManager;
import com.qckj.dabei.ui.release.adapter.SearchAddressAdapter;
import com.qckj.dabei.ui.release.adapter.SelectAddressAdapter;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.ViewInject;
import com.qckj.dabei.view.listview.PullRefreshView;
import com.umeng.analytics.MobclickAgent;

/**
 * @name: 梁永胜
 * @date ：2018/12/20 09:27
 * E-Mail Address：875450820@qq.com
 */
public class SearchAddressActivity extends BaseActivity {

    @FindViewById(R.id.et_search)
    EditText mEtSearch;
    @FindViewById(R.id.list_view)
    PullRefreshView listView;
    private SearchAddressAdapter mSearchAddressAdapter;


    private PoiSearch mPoiSearch;
    private PoiSearch.Query mQuery;
    private PoiSearch.OnPoiSearchListener mOnPoiSearchListener;
    @Manager
    GaoDeLocationManager gaoDeLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_address);
        ViewInject.inject(this);
        initListener();
    }

    private void initListener() {
        mSearchAddressAdapter = new SearchAddressAdapter(this);
        listView.setAdapter(mSearchAddressAdapter);

        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (null != editable) {
                    if (0 == editable.length()) {//没有输入则清空搜索记录
                        mSearchAddressAdapter.setDataNull();
                    } else {
                            doSearchQuery(editable.toString(), gaoDeLocationManager.getUserLocationInfo().getCity(),
                                    new LatLonPoint(gaoDeLocationManager.getUserLocationInfo().getLatitude(),
                                            gaoDeLocationManager.getUserLocationInfo().getLongitude()));

                    }
                }
            }
        });

        mSearchAddressAdapter.setOnAdapterItemClickListener(new SimpleBaseAdapter.OnAdapterItemClickListener<PoiItem>() {
            @Override
            public void onAdapterItemClick(int position, PoiItem poiItem) {
                Intent intent = new Intent();
                intent.putExtra("poiItem", poiItem);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        mOnPoiSearchListener = new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult result, int i) {
                if (i == 1000) {
                    if (result != null && result.getQuery() != null) {// 搜索poi的结果
                        if (result.getQuery().equals(mQuery)) {// 是否是同一条
                            if (null != mSearchAddressAdapter) {
                                mSearchAddressAdapter.setData(result.getPois());
                                listView.smoothScrollToPosition(0);
                            }
                        }
                    }
                }
            }

            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {

            }
        };

    }
    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery(String keyWord, String city, LatLonPoint lpTemp) {
        mQuery = new PoiSearch.Query(keyWord, "", city);//第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        mQuery.setPageSize(20);// 设置每页最多返回多少条poiitem
        mQuery.setPageNum(0);// 设置查第一页


        mPoiSearch = new PoiSearch(this, mQuery);
        mPoiSearch.setOnPoiSearchListener(mOnPoiSearchListener);
        if (lpTemp != null) {
            mPoiSearch.setBound(new PoiSearch.SearchBound(lpTemp, 10000, true));//该范围的中心点-----半径，单位：米-----是否按照距离排序
        }
        mPoiSearch.searchPOIAsyn();// 异步搜索
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mPoiSearch) {
            mPoiSearch = null;
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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
