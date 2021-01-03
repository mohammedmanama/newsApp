package com.example.hp15_bs.newsApp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter extends BaseAdapter {

    Context context;
    List<NewsItem> news;

    public NewsAdapter(Context context , List<NewsItem>news){
        this.context = context;
        this.news = news;
    }
    @Override
    public int getCount() {
        return news.size();
    }

    @Override
    public Object getItem(int position) {
        return news.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.news_item_layout,null);
            ViewHolder vh = new ViewHolder();
            vh.titleTv = convertView.findViewById(R.id.newsTitleTv);
            vh.dateTv = convertView.findViewById(R.id.newsDateTv);
            vh.newIm = convertView.findViewById(R.id.newsImage);
            convertView.setTag(vh);
        }
        ViewHolder  vh = (ViewHolder)convertView.getTag();
        vh.titleTv.setText(news.get(position).getTitle());
        vh.dateTv.setText(news.get(position).getPubdate());
        Picasso.get().load(news.get(position).getContent()).into(vh.newIm);
        return convertView;
    }

    class ViewHolder{
        ImageView newIm;
        TextView titleTv;
        TextView dateTv;
    }

}
