package com.example.guoli.myandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UserLogin extends AppCompatActivity {
    private String user_naem;
    private String pass_word;
    private String api_ip;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        getApiIp(getBaseContext());

        final EditText username =(EditText)findViewById(R.id.username);
        final EditText password =(EditText)findViewById(R.id.password);

        Button loginBtn =(Button)findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 user_naem = username.getText().toString();
                 pass_word = password.getText().toString();
                if (!user_naem.isEmpty() && !pass_word.isEmpty()){
                    sendRequestOkHttp();
                }else {
                    Toast.makeText(UserLogin.this, "用户名或密码为空！", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }
    private void sendRequestOkHttp() {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder().url("http://"+api_ip+":8080/user/login?username="+user_naem+"&password="+pass_word).build();
                        try {
                            Response response = client.newCall(request).execute();
                            String result = response.body().string();
//                            System.out.println("------------------------------>"+result);
                            JSONObject jsonObject = new JSONObject(result);
                            String code = jsonObject.get("code").toString();
                            if(code.equals("200")){
                                System.out.println("code------------------->"+code.equals("200"));
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(UserLogin.this, "登录成功", Toast.LENGTH_SHORT).show();
                                        preservationData();
                                        Intent intent=new Intent(UserLogin.this,CommentAdd.class);
                                        startActivity(intent);
                                    }
                                });
                            }
                            if(!code.equals("200")){
                                System.out.println("code------------------->"+!code.equals("200"));
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(UserLogin.this, "登录失败，用户名或密码错误！", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).start();
    }

    private void  preservationData(){
        System.out.println("user_naem--------->"+user_naem);
        setSp(getBaseContext());
    }

    private void setSp(Context context){
        SharedPreferences login = context.getSharedPreferences("Login", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = login.edit();
        edit.putString("username",user_naem);
        edit.apply();
    }

    private void getApiIp(Context context){
        SharedPreferences login = context.getSharedPreferences("SetAttribute", Context.MODE_PRIVATE);
        api_ip = login.getString("api_ip", "192.168.43.88");
    }


}
