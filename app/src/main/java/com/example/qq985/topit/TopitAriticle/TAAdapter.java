package com.example.qq985.topit.TopitAriticle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.qq985.topit.R;

import java.io.Serializable;
import java.util.List;

/**
 * Created by qq985 on 2016/3/11.
 */
public class TAAdapter extends BaseAdapter implements Serializable {

    private List<TopicArticleInfo> ta_list;
    private Context ta_context;

    public TAAdapter(List<TopicArticleInfo> ta_list, Context ta_context) {
        this.ta_list = ta_list;
        this.ta_context = ta_context;
    }

    @Override
    public int getCount() {
        return ta_list.size();
    }

    @Override
    public Object getItem(int position) {
        return ta_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TAViewHolder ta_view_holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(ta_context).
                    inflate(R.layout.topic_article_cell, null);

            ta_view_holder=new TAViewHolder();

            ta_view_holder.ta_title_tv
                    =(TextView)convertView.findViewById(R.id.tv_ta_title);
            ta_view_holder.ta_author_tv
                    =(TextView)convertView.findViewById(R.id.tv_ta_author);
            ta_view_holder.ta_introductions_tv
                    =(TextView)convertView.findViewById(R.id.tv_ta_introductions);

            ta_view_holder.ta_title_tv
                    .setText(ta_list.get(position).getTitle());
            ta_view_holder.ta_author_tv
                    .setText(ta_list.get(position).getAuthor());
            ta_view_holder.ta_introductions_tv
                    .setText(ta_list.get(position).getIntroductions());

            convertView.setTag(ta_view_holder);

        }else{
            ta_view_holder=(TAViewHolder)convertView.getTag();
            ta_view_holder.ta_title_tv.setText(ta_list.get(position).getTitle());
            ta_view_holder.ta_author_tv.setText(ta_list.get(position).getAuthor());
            ta_view_holder.ta_introductions_tv.setText(ta_list.get(position).getIntroductions());
        }

        return convertView;
    }

    private static class TAViewHolder implements Serializable {
        TextView ta_title_tv;
        TextView ta_author_tv;
        TextView ta_url_tv;
        TextView ta_introductions_tv;
    }

}
