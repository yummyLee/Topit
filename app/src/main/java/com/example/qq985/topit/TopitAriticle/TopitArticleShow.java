package com.example.qq985.topit.TopitAriticle;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qq985.topit.GetHtml;
import com.example.qq985.topit.R;
import com.example.qq985.topit.RefreshableView;
import com.example.qq985.topit.TopitAriticle.TAAdapter;
import com.example.qq985.topit.TopitAriticle.TopicArticleInfo;

import java.util.List;


public class TopitArticleShow extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private View view;
    private LinearLayout ll_pb;
    private LinearLayout ll_tas_main;
    private ProgressBar pb_update_article_list;
    private RefreshableView rv_update_article_list;
    private ListView lv_show_list;
    private List<TopicArticleInfo> list_article;
    private TAAdapter taAdapter;
    private EditText et_goto_the_page;
    private TextView tv_show_page;
    private Button btn_last_page, btn_next_page, btn_goto_page;
    private ProgressDialog progressDialog = null;

    String html_code = "";
    String url_article_pre = "http://www.topit.me/topics?p=";
    int page_num = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_topit_article_show, container, false);
        ll_pb = (LinearLayout) view.findViewById(R.id.ll_pb);
        ll_tas_main = (LinearLayout) view.findViewById(R.id.ll_tas_main);
        pb_update_article_list = (ProgressBar) view.findViewById(R.id.pb_update_article_list);
        rv_update_article_list = (RefreshableView) view.findViewById(R.id.rv_update_article_list);
        lv_show_list = (ListView) view.findViewById(R.id.lv_tas_list);
        et_goto_the_page = (EditText) view.findViewById(R.id.et_goto_the_page);
        tv_show_page = (TextView) view.findViewById(R.id.tv_show_page);
        btn_last_page = (Button) view.findViewById(R.id.btn_last_page);
        btn_next_page = (Button) view.findViewById(R.id.btn_next_page);
        btn_goto_page = (Button) view.findViewById(R.id.btn_goto_the_page);

        new SetListView().execute();

        rv_update_article_list.setOnRefreshListener(new RefreshableView.PullToRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                rv_update_article_list.finishRefreshing();
            }
        }, 0);

        lv_show_list.setOnItemClickListener(this);
        setClick();
        lv_show_list.setFocusableInTouchMode(true);
        et_goto_the_page.clearFocus();
        return view;
    }

    void setClick() {
        btn_goto_page.setOnClickListener(this);
        btn_next_page.setOnClickListener(this);
        btn_last_page.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_last_page:
//                progressDialog = ProgressDialog.show(getActivity(), "请稍等…", "获取数据中…", true);
                goToLastPage();
                System.out.println("last_page");
                break;
            case R.id.btn_next_page:
//                progressDialog = ProgressDialog.show(getActivity(), "请稍等…", "获取数据中…", true);
                goToNextPage();
                System.out.println("next_page");
                break;
            case R.id.btn_goto_the_page:
//                progressDialog = ProgressDialog.show(getActivity(), "请稍等…", "获取数据中…", true);
                goToThePage();
                System.out.println("the_page");
                break;

        }
    }

    void goToNextPage() {
        if (page_num <= 691) {
            ll_tas_main.setVisibility(View.INVISIBLE);
            ll_pb.setVisibility(View.VISIBLE);
            page_num++;
            new GetArticleList().execute(url_article_pre + page_num);
        } else {
            Toast.makeText(getActivity(), "到底了(⊙_⊙)?", Toast.LENGTH_SHORT).show();
        }
    }

    void goToLastPage() {
        if (page_num > 2) {
            ll_tas_main.setVisibility(View.INVISIBLE);
            ll_pb.setVisibility(View.VISIBLE);
            page_num--;
            new GetArticleList().execute(url_article_pre + page_num);
        } else {
            Toast.makeText(getActivity(), "到头了(⊙_⊙)?", Toast.LENGTH_SHORT).show();
        }
    }

    void goToThePage() {
        if (et_goto_the_page.getText() != null) {
            if (et_goto_the_page.getText().toString().length() > 0) {
                int temp_page_num = Integer.parseInt(et_goto_the_page.getText().toString());
                if (temp_page_num >= 2 && temp_page_num <= 692) {
                    page_num=temp_page_num;
                    ll_tas_main.setVisibility(View.INVISIBLE);
                    ll_pb.setVisibility(View.VISIBLE);
                    new GetArticleList().execute(url_article_pre + page_num);
                } else {
                    Toast.makeText(getActivity(), "没有这一页(⊙_⊙)?", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "没有这一页(⊙_⊙)?", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "没有这一页(⊙_⊙)?", Toast.LENGTH_SHORT).show();
        }
    }

    class GetArticleList extends AsyncTask<String, Integer, TAAdapter> {

        @Override
        protected TAAdapter doInBackground(String... params) {
            try {
                html_code = GetHtml.getHtml(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            list_article = TopicSpider.getArticle(html_code);
            taAdapter = new TAAdapter(list_article, getContext());

            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            return taAdapter;
        }

        @Override
        protected void onPostExecute(TAAdapter taAdapter) {
            super.onPostExecute(taAdapter);
            tv_show_page.setText(page_num + "");
            lv_show_list.setAdapter(taAdapter);
            ll_tas_main.setVisibility(View.VISIBLE);
            ll_pb.setVisibility(View.GONE);

        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent_to_details = new Intent(getActivity(), TopicArticleDetails.class);
        intent_to_details.putExtra("title", list_article.get(position).getTitle());
        intent_to_details.putExtra("author", list_article.get(position).getAuthor());
        intent_to_details.putExtra("details", list_article.get(position).getFirst_line()+'\n'+list_article.get(position).getDetails());

        startActivity(intent_to_details);
    }

    class SetListView extends AsyncTask<Void, Void, TAAdapter> {

        @Override
        protected void onPostExecute(TAAdapter taAdapter) {
            super.onPostExecute(taAdapter);
            tv_show_page.setText(page_num + "");
            lv_show_list.setAdapter(taAdapter);
        }

        @Override
        protected TAAdapter doInBackground(Void... params) {

            Intent intent_from_splash = getActivity().getIntent();

            Bundle bundle_list = intent_from_splash.getBundleExtra("bundle_data");

            list_article = (List<TopicArticleInfo>) bundle_list.getSerializable("list_article");
            page_num = bundle_list.getInt("page_num");

            taAdapter = new TAAdapter(list_article, getContext());
            return taAdapter;
        }
    }


}
