package com.example.qq985.topit.TopitAriticle;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by qq985 on 2016/3/11.
 */
public class TopicSpider {

    public static ArrayList<TopicArticleInfo> getArticle(String content) {

        ArrayList<TopicArticleInfo> tai = new ArrayList<>();

        Pattern url_pattern = Pattern.compile("(http://www.topit.me/user/topic/\\d+)\">[^<>查看全部].+?<");
        Matcher url_matcher = url_pattern.matcher(content);

        Boolean isFind = url_matcher.find();
        int i = 1;
        while (isFind && i <= 5) {
            i++;
            TopicArticleInfo TAI_temp = new TopicArticleInfo(url_matcher.group(1));

            tai.add(TAI_temp);

            isFind = url_matcher.find();
        }
        return tai;

    }


}
