package com.example.guoli.myandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    List<Comment> comment = new ArrayList<>();
    List<Map<String,Object>> listitem = new ArrayList<>();
    private String username;
    private String api_ip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getApiIp(getBaseContext());

        ListView listView = (ListView) findViewById(R.id.commentListView);
        ImageButton toAddBtn = (ImageButton)findViewById(R.id.toAddBtn);

        sendRequestOkHttp();
//        SimpleAdapter adapter = new SimpleAdapter(this,listitem,R.layout.comment_item,new String[]{"comment_id",
//                "comment_name","comment_time"},new int[]{R.id.comment_id,R.id.comment_name,R.id.comment_time});

        SimpleAdapter adapter = new SimpleAdapter(this,listitem,R.layout.comment_item,new String[]{"comment_id",
                "comment_title","comment_detail","comment_author","comment_time"},new int[]{R.id.comment_id,R.id.comment_title,
                R.id.comment_detail,R.id.comment_author,R.id.comment_time});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?>parent, View view, int postiton, long id){
                Map<String,Object> map = (Map<String,Object>)parent.getItemAtPosition(postiton);
//                Toast.makeText(MainActivity.this, map.get("comment_id").toString(), Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(MainActivity.this,CommentDetails.class);

                //用Bundle携带数据
                Bundle bundle=new Bundle();
                //传递name参数为tinyphp
                bundle.putString("id",  map.get("comment_id").toString());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        toAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSp(getBaseContext());
            }
        });

        findViewById(R.id.toAttributeBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,SetAttribute.class);
                startActivity(intent);
            }
        });
    }

    private void sendRequestOkHttp() {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder().url("http://"+api_ip+":8080/list/get_all").build();
                        try {
                            Response response = client.newCall(request).execute();
                            String result = response.body().string();
//                            System.out.println("------------------------------>"+result);
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
        comment = gson.fromJson(result, new TypeToken<List<Comment>>() {}.getType());
//        System.out.println("------------------------------>"+comment.size());

        for(int i=0;i<comment.size();i++){
            Map<String,Object> map = new HashMap<>();
            map.put("comment_id",comment.get(i).getId());
            map.put("comment_title",comment.get(i).getTitle());
            map.put("comment_detail",comment.get(i).getDetail());
            map.put("comment_author",comment.get(i).getAuthor());
            map.put("comment_time",comment.get(i).getCreateTime());
            listitem.add(map);
        }

    }


    private void getSp(Context context){
        SharedPreferences login = context.getSharedPreferences("Login", Context.MODE_PRIVATE);
        System.out.println("login------------->"+login);
        username = login.getString("username", null);
//        Toast.makeText(MainActivity.this, username, Toast.LENGTH_SHORT).show();
        if (username == null){
//            Toast.makeText(MainActivity.this,"空", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(MainActivity.this,UserLogin.class);
            startActivity(intent);
        }else {
            Intent intent = new Intent(MainActivity.this, CommentAdd.class);
            startActivity(intent);
//            Toast.makeText(MainActivity.this,"有", Toast.LENGTH_SHORT).show();
        }
    }


    private void getApiIp(Context context){
        SharedPreferences login = context.getSharedPreferences("SetAttribute", Context.MODE_PRIVATE);
        api_ip = login.getString("api_ip", "192.168.43.88");
        Toast.makeText(MainActivity.this, "当前ip地址:"+api_ip, Toast.LENGTH_SHORT).show();
    }


}
