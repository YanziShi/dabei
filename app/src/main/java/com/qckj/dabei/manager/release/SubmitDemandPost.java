package com.qckj.dabei.manager.release;

import com.qckj.dabei.app.SystemConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by yangzhizhong on 2019/6/10.
 */
public class SubmitDemandPost {
    public static String post(JSONObject jsonObject) {
        // 使用POST方式向目的服务器发送请求
        URL connect;
        StringBuffer data = new StringBuffer();
        try {
            connect = new URL(SystemConfig.serverUrl+"/publishXuqiuByclassifytwoIdAndroid");
            HttpURLConnection connection = (HttpURLConnection)connect.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            OutputStreamWriter paramout = new OutputStreamWriter(
                    connection.getOutputStream(),"UTF-8");
            paramout.write(jsonObject.toString());
            paramout.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(), "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                data.append(line);
            }
            paramout.close();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(data.toString());
        return data.toString();
    }
}
