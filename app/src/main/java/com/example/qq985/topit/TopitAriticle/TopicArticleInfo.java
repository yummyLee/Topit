package com.example.qq985.topit.TopitAriticle;

import com.example.qq985.topit.GetHtml;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by qq985 on 2016/3/11.
 */
public class TopicArticleInfo implements Serializable {

    public String title;
    public String author;
    public String articleUrl;
    public String introductions;

    public String first_line;
    public String details;


    public String content = null;

    public TopicArticleInfo() {
        title = "";
        articleUrl = "";
        introductions = "";
        author = "";
        first_line="";
        details = "";
    }

    public TopicArticleInfo(String title, String author, String articleUrl, String introductions,String first_line, String details) {
        this.title = title;
        this.author = author;
        this.articleUrl = articleUrl;
        this.introductions = introductions;
        this.first_line=first_line;
        this.details = details;
    }

    public TopicArticleInfo(String Url) {
        title = "";
        author = "";
        articleUrl = Url;
        introductions = "";
        first_line="";
        details = "";

        MyThreadOfArticleDetails my_thread_of_article_details = new MyThreadOfArticleDetails(Url);
        my_thread_of_article_details.start();
        try {
            my_thread_of_article_details.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        System.out.println("正在抓取" + Url);
        // 根据url获取该问答的细节


        Pattern pattern;
        Matcher matcher;
        // 匹配标题
        pattern = Pattern.compile("<title>(.+?) -- TOPIT.ME 收录优美图片</title>");
        matcher = pattern.matcher(content);
        if (matcher.find()) {
            title = matcher.group(1);
//            System.out.println("获取到标题!");
        } else {
            System.out.println("没有获取到标题!");
        }

        //匹配作者
        pattern = Pattern.compile("<div class=\"userinfo_blk\"><h2>(.+?)</h2>");
        matcher = pattern.matcher(content);
        if (matcher.find()) {
            author = matcher.group(1);
//            System.out.println("获取到标题!");
        } else {
            System.out.println("没有获取到作者!");
            author="佚名";
        }


        // 匹配描述
        pattern = Pattern.compile("<br />([^div].+?)<br />");
        matcher = pattern.matcher(content);
        matcher.find();

        String author_temp;
        try {
            author_temp = matcher.group(1).replace("<br />", "");

        } catch (Exception e) {
            author_temp = "佚名";
        }

        first_line = author_temp;

        pattern = Pattern.compile("<br />([^div].+?)<br />");
        matcher = pattern.matcher(content);
        boolean author_sign = false;
        int i = 1;
        while (matcher.find()) {
            if (!author_sign) {
                author_sign = true;
                continue;
            }

            String temp = matcher.group(1).replace("<br />", "");
            if (i != 3) {
                i++;
                introductions += (temp + '\n');
            }
            details += (temp + '\n');

        }

//        System.out.println(introductions);
//		// 匹配答案
//		pattern = Pattern.compile("/answer/content.+?<div.+?>(.*?)</div>");
//		matcher = pattern.matcher(content);
//		boolean isFind = matcher.find();
//		while (isFind) {
//			answers.add(matcher.group(1));
//			isFind = matcher.find();
//		}
//
        int index = details.indexOf("<");
        if (index > 100)
            details = details.substring(0, index);

    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        String style =
                "标题:" + title
                        + "\n文章链接:" + articleUrl
                        + "\n简要:" + introductions + "\n";

        return style;
    }

    public String getArticleUrl() {
        return articleUrl;
    }

    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }

    public String getIntroductions() {
        return introductions;
    }

    public void setIntroductions(String introductions) {
        this.introductions = introductions;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getFirst_line() {
        return first_line;
    }

    private class MyThreadOfArticleDetails extends Thread implements Serializable {

        public String Url = "";

        public MyThreadOfArticleDetails(String Url) {
            this.Url = Url;
        }

        @Override
        public void run() {
            try {
                content = GetHtml.getHtml(Url);
            } catch (Exception e) {
                System.out.println("获取文章详情html失败");
                e.printStackTrace();
            }
        }
    }
}

