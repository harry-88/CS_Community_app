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

import java.time.Instant;

public class RegisterActivity extends AppCompatActivity {

    private EditText etemail,etpassword,etCpassword;
    private Button btnSignup,btnLogin;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    public void init()
    {
        progressBar = findViewById(R.id.regprogressBar);
        firebaseAuth = FirebaseAuth.getInstance();
        etemail = findViewById(R.id.regEmail);
        etpassword = findViewById(R.id.regpass);
        etCpassword = findViewById(R.id.regCPass);
        btnSignup = findViewById(R.id.regBtnSign);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etemail.getText().toString().trim();
                String pass = etpassword.getText().toString().trim();
                String cpass = etCpassword.getText().toString().trim();

                if (!email.isEmpty() && !pass.isEmpty() && !cpass.isEmpty())
                {
                    if (pass.equals(cpass))
                    {
                        firebaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.VISIBLE);
                                if (task.isSuccessful())
                                {
                                    Intent instant = new Intent(com.atmostsoft.cs_community.RegisterActivity.this,com.atmostsoft.cs_community.SetupActivity.class);
                                    startActivity(instant);
                                    finish();

                                }
                                else
                                {
                                    String error = task.getException().getMessage();
                                    Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_SHORT).show();
                                }
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        });
                    }else
                    {
                        Toast.makeText(RegisterActivity.this, "Password and confirm password must be same", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(RegisterActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnLogin = findViewById(R.id.regLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(com.atmostsoft.cs_community.RegisterActivity.this,com.atmostsoft.cs_community.Login.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null)
            goToMainPage();
    }

    public void goToMainPage()
    {


        Intent instant = new Intent(this,com.atmostsoft.cs_community.MainActivity.class);
        startActivity(instant);
        finish();
    }
}