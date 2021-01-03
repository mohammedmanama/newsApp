package com.example.hp15_bs.newsApp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RequestQueue queue;
    List<NewsItem> news = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        queue = Volley.newRequestQueue(getApplicationContext());
        ListView lv = findViewById(R.id.lv);
        final NewsAdapter adapter = new NewsAdapter(getApplicationContext(),news);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = news.get(position).getLink();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
        String link = "https://feeds.alwatanvoice.com/ar/palestine.xml";
        StringRequest request = new StringRequest(Request.Method.GET, link, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser parser = factory.newPullParser();
                    parser.setInput(new StringReader(response));
                    int eventType = parser.getEventType();
                    NewsItem item = null;
                    String text = null;
                    while (eventType != XmlPullParser.END_DOCUMENT){
                        if(eventType == XmlPullParser.START_TAG){
                            String tagName = parser.getName();
                            if(tagName.equalsIgnoreCase("item")){
                                item = new NewsItem();
                            }else if (tagName.equalsIgnoreCase("content") && item!=null){
                                String url = parser.getAttributeValue("null","url");
                                item.setContent(url);
                            }
                        }else if(eventType == XmlPullParser.TEXT){
                                text = parser.getText();
                        }else if(eventType == XmlPullParser.END_TAG){
                                if (item!=null) {
                                    String tagName = parser.getName();
                                    if (tagName.equalsIgnoreCase("title")) {
                                        item.setTitle(text);
                                    } else if (tagName.equalsIgnoreCase("link")) {
                                        item.setLink(text);
                                    }else if (tagName.equalsIgnoreCase("pubdate")){
                                        item.setPubdate(text);
                                    }else if (tagName.equalsIgnoreCase("item")){
                                        news.add(item);
                                        item=null;
                                    }
                                }
                        }
                        eventType = parser.next();
                    }
                    /*
                    for (NewsItem newsItem:news){
                        Log.d("ttt",newsItem.getTitle());
                        Log.d("ttt",newsItem.getLink());
                        Log.d("ttt","-------------");
                    }
                    */
                    adapter.notifyDataSetChanged();

                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Unable to handle request: "+error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        queue.add(request);
    }
}
