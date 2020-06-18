package com.example.guoli.myandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CommentAdd extends AppCompatActivity {
    private Editable comment_title;
    private Editable comment_text;
    private String username;
    private String api_ip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_add);

        getApiIp(getBaseContext());

        final EditText titleText = (EditText)findViewById(R.id.comment_input_title);
        final  EditText comText = (EditText)findViewById(R.id.comment_input_text);

        Button addBtn = (Button)findViewById(R.id.addbtn);

        addBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getSp(getBaseContext());
                 comment_title = titleText.getText();
                 comment_text = comText.getText();
//                Toast.makeText(CommentAdd.this, titleText.getText(), Toast.LENGTH_SHORT).show();
                if(!comment_title.toString().isEmpty() && !comment_text.toString().isEmpty() && !username.toString().isEmpty()){
//                    System.out.println("comment_title------------->"+comment_title+comment_text);
                    sendRequestOkHttp();
                    Toast.makeText(CommentAdd.this, "添加成功", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(CommentAdd.this,MainActivity.class);
                    startActivity(intent);
                    titleText.setText("");
                    comText.setText("");
                }else {
                    Toast.makeText(CommentAdd.this, "请填写好在提交！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        findViewById(R.id.tologibBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CommentAdd.this,UserLogin.class);
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
                        Request request = new Request.Builder().url("http://"+api_ip+":8080/list/add?title="+comment_title+"&author="+username+"&detail="+comment_text).build();
                        try {
                            Response response = client.newCall(request).execute();
                            String result = response.body().string();
                            System.out.println("------------------------------>"+result);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).start();
    }


    private void getSp(Context context){
        SharedPreferences login = context.getSharedPreferences("Login", Context.MODE_PRIVATE);
        username = login.getString("username", null);
//        Toast.makeText(CommentAdd.this, username, Toast.LENGTH_SHORT).show();
    }

    private void getApiIp(Context context){
        SharedPreferences login = context.getSharedPreferences("SetAttribute", Context.MODE_PRIVATE);
        api_ip = login.getString("api_ip", "192.168.43.88");
        Toast.makeText(CommentAdd.this, api_ip, Toast.LENGTH_SHORT).show();
    }

}
