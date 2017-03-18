package com.example.qq985.topit.Seat;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qq985.topit.GetHtml;
import com.example.qq985.topit.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * A simple {@link Fragment} subclass.
 */
public class Seat extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private View view;
    private Button btn_refresh;
    private TextView tv_time;
    private Switch sw_circle_notify;
    private TextView[] tv_seat_number = new TextView[16];

    NotificationCompat.Builder mBuilder;
    NotificationManager mNotificationManager;
    Notification circle_free_notification;
    Handler handler_update_seat = new Handler();

    private String[] seat_info = new String[16];
    String html_code = "";

    public Seat() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_seat, container, false);
        btn_refresh = (Button) view.findViewById(R.id.btn_refresh);
        tv_time = (TextView) view.findViewById(R.id.tv_time);
        sw_circle_notify = (Switch) view.findViewById(R.id.sw_circle_notify);
        tv_seat_number[0] = (TextView) view.findViewById(R.id.tv_one_west_free);
        tv_seat_number[1] = (TextView) view.findViewById(R.id.tv_one_west_rest);
        tv_seat_number[2] = (TextView) view.findViewById(R.id.tv_one_east_free);
        tv_seat_number[3] = (TextView) view.findViewById(R.id.tv_one_east_rest);
        tv_seat_number[4] = (TextView) view.findViewById(R.id.tv_two_flat_free);
        tv_seat_number[5] = (TextView) view.findViewById(R.id.tv_two_flat_rest);
        tv_seat_number[6] = (TextView) view.findViewById(R.id.tv_two_free);
        tv_seat_number[7] = (TextView) view.findViewById(R.id.tv_two_rest);
        tv_seat_number[8] = (TextView) view.findViewById(R.id.tv_circle_free);
        tv_seat_number[9] = (TextView) view.findViewById(R.id.tv_circle_rest);
        tv_seat_number[10] = (TextView) view.findViewById(R.id.tv_three_free);
        tv_seat_number[11] = (TextView) view.findViewById(R.id.tv_three_rest);
        tv_seat_number[12] = (TextView) view.findViewById(R.id.tv_five_free);
        tv_seat_number[13] = (TextView) view.findViewById(R.id.tv_five_rest);
        tv_seat_number[14] = (TextView) view.findViewById(R.id.tv_all_free);
        tv_seat_number[15] = (TextView) view.findViewById(R.id.tv_all_rest);


        setClick();
        initNotification();

        new UpdateSeatInfo().execute();

        return view;
    }

    void setClick() {
        btn_refresh.setOnClickListener(this);
        sw_circle_notify.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_refresh:
                new UpdateSeatInfo().execute();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.sw_circle_notify:

                Runnable runnable = new ThreadUpdateSeatInfo();
                if (sw_circle_notify.isChecked()) {
                    handler_update_seat.postDelayed(runnable, 5000);
                    if(Integer.parseInt(tv_seat_number[9].getText().toString())>0){
                        mNotificationManager.notify(1,mBuilder.build());
                    }
                } else {
                    handler_update_seat.removeCallbacks(runnable);
                }
                break;
        }
    }

    void initNotification() {
        mNotificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(getContext());
        mBuilder.setContentTitle("座位空余")//设置通知栏标题
                .setContentText("环形区有空余座位了") //<span style="font-family: Arial;">/设置通知栏显示内容</span>
                .setContentIntent(getDefalutIntent(Notification.FLAG_AUTO_CANCEL)) //设置通知栏点击意图
//                .setNumber(number) //设置通知集合的数量
                .setTicker("环形区有空余座位了") //通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
//                .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
                .setSmallIcon(R.drawable.seat_icon);//设置通知小ICON

        circle_free_notification = mBuilder.build();
        circle_free_notification.flags = Notification.FLAG_AUTO_CANCEL;
    }

    public PendingIntent getDefalutIntent(int flags) {
        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 1, new Intent(), flags);
        return pendingIntent;
    }

    class UpdateSeatInfo extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            try {
                html_code = GetHtml.getHtml("http://210.45.242.123/roomshow/");
            } catch (Exception e) {
                e.printStackTrace();
            }
            Pattern seat_pattern;
            Matcher seat_matcher;

            seat_pattern = Pattern.compile("style=\"font-size:20.0pt\">(\\d+?)</td>");
            seat_matcher = seat_pattern.matcher(html_code);

            boolean is_find = seat_matcher.find();

            for (int i = 0; i < 16 && is_find; i++) {
                seat_info[i] = seat_matcher.group(1);
                System.out.println((seat_matcher.group(1)));
                is_find = seat_matcher.find();
            }
            return seat_info;

        }

        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);
            for (int i = 0; i < 16; i++) {
                tv_seat_number[i].setText(strings[i]);
            }
            Toast.makeText(getActivity(), "刷新完成", Toast.LENGTH_SHORT).show();

        }
    }

    class ThreadUpdateSeatInfo implements Runnable {

        @Override
        public void run() {
            new UpdateSeatInfo().execute();
            handler_update_seat.postDelayed(this,5000);
        }
    }
}
