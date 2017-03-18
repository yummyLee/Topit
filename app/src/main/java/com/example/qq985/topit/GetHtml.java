package com.example.qq985.topit;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by qq985 on 2016/4/28.
 */
public class GetHtml {
    
    public static String getHtml(String url_path) throws Exception {

        URL url = new URL(url_path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(6 * 1000);
        connection.setRequestMethod("GET");

        if (connection.getResponseCode() == 200) {
            System.out.println("开始获取");
            BufferedReader in=new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            String line,result="";
            int i= 0;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            System.out.println("返回获取的结果"+result.substring(0,20));
            return result;
        }
        System.out.println("未开始获取");
        return null;
    }
}
