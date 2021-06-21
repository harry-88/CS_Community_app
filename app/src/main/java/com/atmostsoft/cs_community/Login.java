package com.atmostsoft.cs_community;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {


    private EditText etEmail,etPassword;
    private Button btnLogin,btnSignup;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }
    public void init()
    {
        etEmail = findViewById(R.id.regEmail);
        etPassword = findViewById(R.id.regpass);
        progressBar = findViewById(R.id.regprogressBar);
        btnLogin = findViewById(R.id.regBtnSign);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                if (!email.isEmpty() && !password.isEmpty())
                {
                    progressBar.setVisibility(View.VISIBLE);
                    firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {

                                sendToMainPage();
                            }
                            else
                            {
                                String error = task.getException().getMessage();

                                Toast.makeText(Login.this, error, Toast.LENGTH_SHORT).show();
                            }

                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }
        });


        btnSignup = findViewById(R.id.regLogin);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(com.atmostsoft.cs_community.Login.this,com.atmostsoft.cs_community.RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null)
        {
            sendToMainPage();
        }
    }

    public void sendToMainPage()
    {
        Intent intent = new Intent(this,com.atmostsoft.cs_community.MainActivity.class);
        startActivity(intent);
        finish();

    }
}