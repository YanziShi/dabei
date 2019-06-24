package com.qckj.dabei.view;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.view.View;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.qckj.dabei.model.CityBean;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangzhizhong on 2019/5/24.
 */
public class AddressPicker {
    static ArrayList<String> Provincestr = new ArrayList<>();//省
    static ArrayList<ArrayList<String>> Citystr = new ArrayList<>();//市
    static ArrayList<ArrayList<ArrayList<String>>> Areastr = new ArrayList<>();//区
    static String address ;

    public interface ClickListener{
        void onClick(String address);
    }

    static public void initAddrPicker(Context context){
        //选项选择器
        Gson gson = new GsonBuilder().serializeNulls().create();
        List<CityBean> listDatas = gson.fromJson(getJson(context, "china_city_data.json"), new TypeToken<List<CityBean>>() {
        }.getType());
        for (int i = 0; i < listDatas.size(); i++) {
            Provincestr.add(listDatas.get(i).getName());
            ArrayList<String> cityStr = new ArrayList<>();
            ArrayList<ArrayList<String>> options3Items_str = new ArrayList<>();
            for (int j = 0; j < listDatas.get(i).getCityList().size(); j++) {
                cityStr.add(listDatas.get(i).getCityList().get(j).getName());
                ArrayList<String> areaBeanstr = new ArrayList<>();
                for (int k = 0; k < listDatas.get(i).getCityList().get(j).getCityList().size(); k++) {
                    areaBeanstr.add(listDatas.get(i).getCityList().get(j).getCityList().get(k).getName());
                }
                options3Items_str.add(areaBeanstr);
            }
            Citystr.add(cityStr);
            Areastr.add(options3Items_str);
        }

    }

    static public void showAddrPicker(Context context,ClickListener clickListener) {
        //条件选择器
        //条件选择器
        OptionsPickerView pvOptions = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3 ,View v) {
                //返回的分别是三个级别的选中位置
                clickListener.onClick(Provincestr.get(options1)+"-"+Citystr.get(options1).get(option2)+"-"+Areastr.get(options1).get(option2).get(options3));
            }
        }).setContentTextSize(16).build();
        pvOptions.setPicker(Provincestr, Citystr, Areastr);
        pvOptions.show();
    }

    //读取方法
    public static String getJson(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
