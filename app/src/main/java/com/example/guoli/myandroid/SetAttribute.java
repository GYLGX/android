package com.example.guoli.myandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SetAttribute extends AppCompatActivity {
    private EditText api_ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_attribute);

        api_ip = (EditText)findViewById(R.id.setIp);

        findViewById(R.id.setIpBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setApiIp(getBaseContext());
            }
        });
    }


    private void setApiIp(Context context){
        SharedPreferences login = context.getSharedPreferences("SetAttribute", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = login.edit();
        edit.putString("api_ip",api_ip.getText().toString());
        edit.apply();
        Toast.makeText(SetAttribute.this, "设置成功", Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(SetAttribute.this,MainActivity.class);
        startActivity(intent);

    }

}
