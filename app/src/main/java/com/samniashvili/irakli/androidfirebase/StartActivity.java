package com.samniashvili.irakli.androidfirebase;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartActivity extends AppCompatActivity {
public Button mRegBtn;
    private Button mAlready;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        mAlready = (Button) findViewById(R.id.loginBtn);
        mAlready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logine();
            }
        });

        mRegBtn = (Button) findViewById(R.id.start_reg_btn);
        mRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 reg();
            }
        });
    }
    public void reg() {
        Intent reg_intent = new Intent(StartActivity.this,RegActivity.class);
        startActivity(reg_intent);
    }

    public void logine() {
        Intent intent = new Intent(StartActivity.this, LoginActivity.class);
        startActivity(intent);
    }

}
