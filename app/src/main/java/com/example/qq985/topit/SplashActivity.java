package com.example.qq985.topit;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.qq985.topit.TopitAriticle.TopicArticleInfo;
import com.example.qq985.topit.TopitAriticle.TopicSpider;

import java.util.ArrayList;


public class SplashActivity extends Activity {

    private ArrayList<TopicArticleInfo> list_article;
    String url_article_pre = "http://www.topit.me/topics?p=";
    int page_num = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

//        handler_get_html=new HandlerGetHtml();
//        ThreadGetHtml threadGetHtml=new ThreadGetHtml();
//        new Thread(threadGetHtml).start();

        new SendHtml().execute();
    }

    class SendHtml extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {

            String html_code = "";
            try {
                html_code = GetHtml.getHtml(url_article_pre + page_num);
            } catch (Exception e) {
                e.printStackTrace();
            }
            list_article = TopicSpider.getArticle(html_code);
            Bundle bundle_html_code = new Bundle();
            bundle_html_code.putSerializable("list_article", list_article);
            bundle_html_code.putInt("page_num", page_num);

            Intent intent_to_main = new Intent(SplashActivity.this, MainActivity.class);
            intent_to_main.putExtra("bundle_data", bundle_html_code);
            startActivity(intent_to_main);

            finish();
            return null;
        }
    }

}
