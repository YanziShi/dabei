package com.qckj.dabei.app;

/**
 * 系统配置文件
 * <p>
 * Created by yangzhizhong on 2019/3/21.
 */
public class SystemConfig {

    public static final String serverUrl = "https://www.dabeiinfo.com";
    //static final String serverUrl = "http://192.168.0.125:8085/DaBei";

    public static final String webUrl = "http://www.dabeiinfo.com/db-retail";    //正式的服务器
    //public static final String webUrl = "http://192.168.1.103:8091";  //小高本地

    public static final String carUrl = "https://www.dabeicar.com/dabei/#/";
    //public static final String carUrl = "http://192.168.1.103:8090/#/";


    /**
     * 获取签约api地址
     *
     * @param routeUrl 路由字段，'/'开头
     * @return api地址
     */
    public static String getServerUrl(String routeUrl) {
        return serverUrl + routeUrl;
    }

}
