package com.example.qq985.topit.TopitAriticle;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.qq985.topit.GetHtml;
import com.example.qq985.topit.MainActivity;
import com.example.qq985.topit.R;
import com.example.qq985.topit.SplashActivity;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login extends AppCompatActivity implements View.OnClickListener{

    private EditText et_account;
    private EditText et_password;
    private Button btn_login;
    private String [] strings_login_info=new String[2];
    private String user_url;

    private ArrayList<TopicArticleInfo> list_article;
    String url_article_pre = "http://www.topit.me/topics?p=";
    int page_num = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et_account= (EditText) findViewById(R.id.et_login_account);
        et_password=(EditText) findViewById(R.id.et_login_password);
        btn_login= (Button) findViewById(R.id.btn_login);

        setOnClick();
    }

    public void setOnClick(){
        btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                strings_login_info[0]=et_account.getText().toString();
                strings_login_info[1]=et_password.getText().toString();
                new LoginTopit().execute(strings_login_info);
                new SendHtml().execute();
                break;
        }
    }

    class LoginTopit extends AsyncTask<String [],String,Cookie []>{

        @Override
        protected Cookie[] doInBackground(String []... strings) {

            String loginUrl = "http://www.topit.me/login";
            // 需登陆后访问的 Url
            String dataUrl = "http://www.topit.me";

            HttpClient httpClient = new HttpClient();

            // 模拟登陆，按实际服务器端要求选用 Post 或 Get 请求方式
            PostMethod postMethod = new PostMethod(loginUrl);

            // 设置登陆时要求的信息，用户名和密码
            NameValuePair[] data = {new NameValuePair("commit", "登录！"), new NameValuePair("user[email]", strings_login_info[0]),
                    new NameValuePair("user[password]", strings_login_info[1])};
            postMethod.setRequestBody(data);
            try {
                // 设置 HttpClient 接收 Cookie,用与浏览器一样的策略
                httpClient.getState().setCookiePolicy(
                        CookiePolicy.COMPATIBILITY);
                httpClient.executeMethod(postMethod);
                // 获得登陆后的 Cookie
                Cookie[] cookies = httpClient.getState().getCookies();
                StringBuffer tmpcookies = new StringBuffer();
                for (Cookie c : cookies) {
                    tmpcookies.append(c.toString() + ";");
                }
                // 进行登陆后的操作1581,1602,1603,1610,1609,1608,1607,1606,1605,1620,1619,1617,1616,1622,1626,1642,1648,1647,1657
                GetMethod getMethod = new GetMethod(dataUrl);
                // 每次访问需授权的网址时需带上前面的 cookie 作为通行证
                getMethod.setRequestHeader("cookie", tmpcookies.toString());
                // 你还可以通过 PostMethod/GetMethod 设置更多的请求后数据
                // 例如，referer 从哪里来的，UA 像搜索引擎都会表名自己是谁，无良搜索引擎除外
                postMethod.setRequestHeader("Referer", "http://www.cc");
                postMethod.setRequestHeader("User-Agent", "www Spot");

                httpClient.executeMethod(getMethod);
                // 打印出返回数据，检验一下是否成功
                String text = getMethod.getResponseBodyAsString();
                System.out.println(text);

                Pattern pattern;
                Matcher matcher;
                // 匹配标题
                pattern = Pattern.compile("<a href=\"http://www.topit.me/user/([1-9]+)\">主页</a>");
                matcher = pattern.matcher(text);
                System.out.println(text);
                if (matcher.find()) {
                    user_url = matcher.group(1);
                    SharedPreferences sp_user_info=getSharedPreferences("user_info", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor=sp_user_info.edit();
                    editor.putString("login_sign","yes");
                    editor.putString("user_url",user_url);
                    editor.commit();
                    System.out.println("登录成功");
                    Toast.makeText(getBaseContext(),"登录成功",Toast.LENGTH_LONG);
//            System.out.println("获取到标题!");
                } else {
                    System.out.println("没有获取到用户!");
                }


            }catch (Exception e){
                System.out.println("登录失败");
            }

            return null;
        }
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

            Intent intent_to_main = new Intent(Login.this, MainActivity.class);
            intent_to_main.putExtra("bundle_data", bundle_html_code);
            startActivity(intent_to_main);

            finish();
            return null;
        }
    }

}
