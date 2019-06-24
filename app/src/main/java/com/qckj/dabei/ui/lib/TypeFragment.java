package com.qckj.dabei.ui.lib;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.qckj.dabei.R;
import com.qckj.dabei.app.SimpleBaseAdapter;
import com.qckj.dabei.model.home.HomeFunctionInfo;
import com.qckj.dabei.ui.lib.adapter.FiltrateItemTypeAdapter;
import com.qckj.dabei.ui.lib.adapter.FiltrateItemTypeTwoAdapter;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.ViewInject;

import java.util.ArrayList;
import java.util.List;

public class TypeFragment extends Fragment {

    @FindViewById(R.id.type_name)
    private TextView typeName;

    @FindViewById(R.id.grid_view)
    private GridView gridView;

    @FindViewById(R.id.grid_view1)
    private GridView gridView1;

    @FindViewById(R.id.btn_back)
    private View btnBack;

    private List<HomeFunctionInfo> homeFunctionInfos = new ArrayList<>();
    private FiltrateItemTypeAdapter filtrateItemTypeAdapter;
    private FiltrateItemTypeTwoAdapter filtrateItemTypeTwoAdapter;
    private OnFiltrateItemTypeSelectListener onFiltrateItemTypeSelectListener;
    private View rootView;

    public void setHomeFunctionInfos(List<HomeFunctionInfo> homeFunctionInfos) {
        this.homeFunctionInfos = homeFunctionInfos;
    }

    public void setOnFiltrateItemTypeSelectListener(OnFiltrateItemTypeSelectListener onFiltrateItemTypeSelectListener) {
        this.onFiltrateItemTypeSelectListener = onFiltrateItemTypeSelectListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        filtrateItemTypeAdapter = new FiltrateItemTypeAdapter(getContext());
        filtrateItemTypeTwoAdapter = new FiltrateItemTypeTwoAdapter(getContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null != rootView) {
            return rootView;
        }
        rootView = inflater.inflate(R.layout.fragment_type, container, false);
        ViewInject.inject(this, rootView);
        initView();
        initData();
        initListener();
        return rootView;
    }

    private void initData() {
        if (homeFunctionInfos != null && homeFunctionInfos.size() > 0) {
            filtrateItemTypeAdapter.setData(homeFunctionInfos);
        }
    }

    private void initListener() {
        filtrateItemTypeAdapter.setOnAdapterItemClickListener(new SimpleBaseAdapter.OnAdapterItemClickListener<HomeFunctionInfo>() {
            @Override
            public void onAdapterItemClick(int position, HomeFunctionInfo info) {
                gridView.setVisibility(View.GONE);
                gridView1.setVisibility(View.VISIBLE);
                btnBack.setVisibility(View.VISIBLE);
                typeName.setText(info.getName());
                filtrateItemTypeTwoAdapter.setData(homeFunctionInfos.get(position).getCategoryList());
            }
        });

        filtrateItemTypeTwoAdapter.setOnAdapterItemClickListener(new SimpleBaseAdapter.OnAdapterItemClickListener<HomeFunctionInfo.Category>() {
            @Override
            public void onAdapterItemClick(int position, HomeFunctionInfo.Category category) {
                gridView1.setVisibility(View.GONE);
                gridView.setVisibility(View.VISIBLE);
                btnBack.setVisibility(View.GONE);
                typeName.setText("分类");
                if (onFiltrateItemTypeSelectListener != null) {
                    onFiltrateItemTypeSelectListener.OnFiltrateItemTypeSelect(position, category);
                }
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gridView1.setVisibility(View.GONE);
                gridView.setVisibility(View.VISIBLE);
                btnBack.setVisibility(View.GONE);
                typeName.setText("分类");
            }
        });
    }

    private void initView() {
        gridView.setAdapter(filtrateItemTypeAdapter);
        gridView1.setAdapter(filtrateItemTypeTwoAdapter);
    }

    public interface OnFiltrateItemTypeSelectListener {
        void OnFiltrateItemTypeSelect(int position, HomeFunctionInfo.Category category);
    }
}
