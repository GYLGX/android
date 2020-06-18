package com.example.guoli.myandroid;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CommentDetails extends AppCompatActivity {
   private Comment comment;
    private String comment_id;
    private TextView comment_details_name;
    private TextView comment_details_text;
    private TextView comment_details_author;
    private TextView comment_details_time;
    private String api_ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_details);
        getApiIp(getBaseContext());

         comment_details_name = (TextView)findViewById(R.id.comment_details_name);
         comment_details_text = (TextView)findViewById(R.id.comment_details_text);
        comment_details_author = (TextView)findViewById(R.id.comment_details_author);
        comment_details_time = (TextView)findViewById(R.id.comment_details_time);

        //新页面接收数据
        Bundle bundle = this.getIntent().getExtras();
        //接收name值
         comment_id = bundle.getString("id");
        if (comment_id != ""){
            sendRequestOkHttp();
        }
    }

    private void sendRequestOkHttp() {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {

                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder().url("http://"+api_ip+":8080/list/get?id="+comment_id).build();
                        try {
                            Response response = client.newCall(request).execute();
                            String result = response.body().string();
                            arrangeData(result);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).start();
    }

    private void arrangeData(String result){
        Gson gson = new Gson();
        comment = gson.fromJson(result, Comment.class);
        comment_details_name.setText(comment.getTitle());
        comment_details_text.setText(comment.getDetail());
        comment_details_author.setText(comment.getAuthor());
        comment_details_time.setText(comment.getCreateTime());
    }

    private void getApiIp(Context context){
        SharedPreferences login = context.getSharedPreferences("SetAttribute", Context.MODE_PRIVATE);
        api_ip = login.getString("api_ip", "192.168.43.88");
    }

}
