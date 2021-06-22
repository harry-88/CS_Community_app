package com.atmostsoft.cs_community;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    Button btnaccountSetting,btnlogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

    }
    public void init()
    {
        btnaccountSetting = findViewById(R.id.mainaccountsetting);
        btnlogout  = findViewById(R.id.mainlogout);
        btnaccountSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,com.atmostsoft.cs_community.SetupActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this,com.atmostsoft.cs_community.Login.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null)
        {

            Intent intent = new Intent(MainActivity.this,com.atmostsoft.cs_community.Login.class);
            startActivity(intent);
            finish();
        }
    }
}