package com.example.qq985.topit.TopitAriticle;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.qq985.topit.MainActivity;
import com.example.qq985.topit.R;

public class UserInfo extends AppCompatActivity {

    private Button btn_ui_test;


    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        btn_ui_test= (Button) findViewById(R.id.btn_ui_test);



        btn_ui_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences=getSharedPreferences("user_info", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("login_sign","no");
                editor.commit();
                System.out.println("退出登录");
                System.out.println(sharedPreferences.getString("login_sign","no"));
            }
        });
    }
}
