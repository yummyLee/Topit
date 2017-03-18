package com.example.qq985.topit;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.qq985.topit.Seat.Seat;
import com.example.qq985.topit.TopitAriticle.Login;
import com.example.qq985.topit.TopitAriticle.TopitArticleShow;
import com.example.qq985.topit.TopitAriticle.UserInfo;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private android.support.v4.app.FragmentManager fragment_manager = getSupportFragmentManager();
    private DrawerLayout dl_main;
    private LinearLayout ll_left_menu;
    private ImageView iv_main_drawer;
    private Button[] btn_left_menu = new Button[6];
    private ImageView iv_user_head;



    TopitArticleShow topitArticleShow = new TopitArticleShow();
    Seat seat = new Seat();
    LoginTest loginTest = new LoginTest();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initView();
        setClick();

        new ReplaceFragment().execute(2);

    }

    void initView() {
        dl_main = (DrawerLayout) findViewById(R.id.dl_main);
        iv_main_drawer = (ImageView) findViewById(R.id.iv_main_drawer);
        ll_left_menu = (LinearLayout) findViewById(R.id.ll_left_menu);

        btn_left_menu[0] = (Button) findViewById(R.id.btn_left_menu_seat);
        btn_left_menu[1] = (Button) findViewById(R.id.btn_left_menu_theme);
        btn_left_menu[2] = (Button) findViewById(R.id.btn_left_menu_topit);
        btn_left_menu[3] = (Button) findViewById(R.id.btn_left_menu_settings);
        btn_left_menu[4] = (Button) findViewById(R.id.btn_left_menu_time_machine);
        btn_left_menu[5] = (Button) findViewById(R.id.btn_left_menu_about);

        iv_user_head = (ImageView) findViewById(R.id.iv_user_head);
    }

    void setClick() {
        iv_main_drawer.setOnClickListener(this);
        iv_user_head.setOnClickListener(this);

        for (Button btn : btn_left_menu) {
            btn.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_user_head:
                new SignIn().execute();
                break;
            case R.id.iv_main_drawer:
                if (!dl_main.isDrawerOpen(ll_left_menu))
                    dl_main.openDrawer(ll_left_menu);
                else dl_main.closeDrawers();
                break;

            case R.id.btn_left_menu_seat:
                new ReplaceFragment().execute(0);
                dl_main.closeDrawers();
                break;
            case R.id.btn_left_menu_theme:
                new ReplaceFragment().execute(1);
                dl_main.closeDrawers();
                break;
            case R.id.btn_left_menu_topit:
                new ReplaceFragment().execute(2);
                dl_main.closeDrawers();
                break;
            case R.id.btn_left_menu_settings:
                new ReplaceFragment().execute(3);
                dl_main.closeDrawers();
                break;
            case R.id.btn_left_menu_time_machine:
                new ReplaceFragment().execute(4);
                dl_main.closeDrawers();
                break;
            case R.id.btn_left_menu_about:
                new ReplaceFragment().execute(5);
                dl_main.closeDrawers();
                break;
        }
    }

    class SignIn extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            SharedPreferences sp_get_user_info = getSharedPreferences("user_info", Activity.MODE_PRIVATE);
            String login_sign=sp_get_user_info.getString("login_sign","no");
            System.out.println("sign:"+login_sign);
            if(login_sign.equals("no")){
                System.out.println("还没有登录");
                Intent intent_to_login = new Intent(getBaseContext(),Login.class);
                startActivity(intent_to_login);
            }else if(login_sign.equals("yes")){
                System.out.println("已经登录");
                Intent intent_to_user_info=new Intent(getBaseContext(),UserInfo.class);
                startActivity(intent_to_user_info);
            }
            return null;
        }
    }

    class ReplaceFragment extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... params) {
            switch (params[0]) {
                case 0:
                    fragment_manager.beginTransaction().replace(R.id.fl_main, seat).commit();
                    break;
                case 1:

                    break;
                case 2:
                    fragment_manager.beginTransaction().replace(R.id.fl_main, topitArticleShow).commit();
                    break;
                case 3:
                    break;
                case 4:

                    break;
                case 5:
//                    fragment_manager.beginTransaction().replace(R.id.fl_main, loginTest).commit();
                    break;
            }
            return null;
        }
    }


}
