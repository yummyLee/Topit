package com.example.qq985.topit.TopitAriticle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.qq985.topit.R;

public class TopicArticleDetails extends AppCompatActivity {

    private TextView tad_title,tad_author,tad_details;

//    private View view1,view2,view3;
//    private List<View> view_list;
//    ViewPager view_pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_article_details);
//        initView();
        tad_author= (TextView) findViewById(R.id.tad_author);
        tad_title= (TextView) findViewById(R.id.tad_title);
        tad_details= (TextView) findViewById(R.id.tad_details);

        Intent intent_from_lis = getIntent();
        String title=intent_from_lis.getStringExtra("title");
        String author=intent_from_lis.getStringExtra("author");
        String details=intent_from_lis.getStringExtra("details");

        tad_title.setText(title);
        tad_author.setText(author);
        tad_details.setText(details);
    }

//    private void initView(){
//        view_pager= (ViewPager) findViewById(R.id.viewpager);
//
//        LayoutInflater layout_inflater=getLayoutInflater().from(this);
//        view1=layout_inflater.inflate(R.layout.article_layout1,null);
//        view2=layout_inflater.inflate(R.layout.article_layout2,null);
//
////        tad_author= (TextView) findViewById(R.id.tad_author1);
////        tad_title= (TextView) findViewById(R.id.tad_title1);
////        tad_details= (TextView) findViewById(R.id.tad_details1);
//
////        Intent intent_from_lis = getIntent();
////        String title=intent_from_lis.getStringExtra("title");
////        String author=intent_from_lis.getStringExtra("author");
////        String details=intent_from_lis.getStringExtra("details");
////
////        System.out.println(title+author+details);
////
////        tad_title.setText(title);
////        tad_author.setText(author);
////        tad_details.setText(details);
//
//        view_list=new ArrayList<View>();
//        view_list.add(view1);
//        view_list.add(view2);
//
//        view_pager.setAdapter(new MyViewPaperAdapter(view_list));
//    }

}
