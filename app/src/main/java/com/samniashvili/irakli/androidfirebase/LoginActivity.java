package com.samniashvili.irakli.androidfirebase;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.TextInputEditText;

import android.view.View;
import android.widget.Button;

import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity {
    public Toolbar mToolBar;
    public Button lgnBt;
    private FirebaseAuth mAuth;
    public EditText mEmail;
    public EditText mPassword;
    public String user_email;
    public String user_password;
    public Intent main_page;
  public ProgressDialog progressBar;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mToolBar = findViewById(R.id.login_toolbar);

        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Login to Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mEmail = findViewById(R.id.userEmail);
       mPassword = findViewById(R.id.userPassword);
        lgnBt = (Button)findViewById(R.id.userLGN);
        mAuth = FirebaseAuth.getInstance();
        progressBar = new ProgressDialog(this);

        lgnBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            user_email = mEmail.getText().toString();
               user_password = mPassword.getText().toString();
            LoginFire(user_email,user_password);
            }
        });



    }
    public void LoginFire(String email,String password) {

    progressBar.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressBar.hide();

                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            main_page = new Intent(LoginActivity.this,MainActivity.class);
                            main_page.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(main_page);


                        } else {
                            progressBar.hide();
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this,"Cannot Sign In. Please check form and try again",Toast.LENGTH_LONG).show();

                        }

                        // ...
                    }
                });
    }
}
