package com.qckj.dabei.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.qckj.dabei.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yangzhizhong on 2019/6/4.
 */
public class SelectMapDialog implements View.OnClickListener {

    private Context context;
    private Dialog bottomDialog;
    String jsonPoi;
    String longitude;   //经度
    String latitude;   //纬度

    public SelectMapDialog(Context context) {
        this.context = context;
    }

    public void setData(String json){
        jsonPoi = json;
    }

    public void show() {
        bottomDialog = new Dialog(context, R.style.BottomDialog);
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_select_map, null);
        contentView.findViewById(R.id.btn_gaode).setOnClickListener(this);
        contentView.findViewById(R.id.btn_baidu).setOnClickListener(this);
        contentView.findViewById(R.id.btn_dismiss).setOnClickListener(this);
        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = context.getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        bottomDialog.show();

        parseJson();
    }

    void parseJson(){
        try {
            JSONObject jsonObject = new JSONObject(jsonPoi);
            JSONArray end = jsonObject.getJSONArray("end");
            longitude = end.getString(0);
            latitude = end.getString(1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        bottomDialog.dismiss();
        switch (v.getId()){
            case R.id.btn_gaode:
                if(isAppInstalled("com.autonavi.minimap")){
                    goToGaodeMap(context,latitude,longitude);
                }else Toast.makeText(context,"您还没有安装高德地图，请前往应用市场安装！",Toast.LENGTH_LONG).show();
                break;
            case R.id.btn_baidu:
                if(isAppInstalled("com.baidu.BaiduMap")){
                    goToBaiduMap(context,latitude,longitude);
                }else Toast.makeText(context,"您还没有安装百度地图，请前往应用市场安装！",Toast.LENGTH_LONG).show();
                break;
        }
    }

    boolean isAppInstalled(String packageName){
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo=null;
            e.printStackTrace();
        }
        if(packageInfo==null){
            return false;
        }else {
            return true;
        }
    }

    public void goToBaiduMap(Context context, String latitude,String longitude) {
        String uri = "baidumap://map/direction"
                + "?origin=我的位置"
                +"&destination=name:终点|latlng:"+latitude+","+longitude
                +"&coord_type=bd09ll"
                + "&mode=driving"
                + "&src=andr.companyName.appName";//src为统计来源必填，companyName、appName是公司名和应用名
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(uri));
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setPackage("com.baidu.BaiduMap");
        context.startActivity(intent);
    }

    public void goToGaodeMap(Context context, String latitude,String longitude) {
        //默认驾车
        String uri = "amapuri://route/plan/"
                + "?dlat="+ latitude+"&dlon="+longitude
                + "&sname=我的位置"
                + "&dname=终点"
                + "&dev=1"
                + "&t=0";
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(uri));
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setPackage("com.autonavi.minimap");
        context.startActivity(intent);
    }
}
