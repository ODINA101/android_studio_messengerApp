package com.samniashvili.irakli.androidfirebase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Debug;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegActivity extends AppCompatActivity {
 private TextInputLayout mDisplayName;
 private TextInputLayout mEmail;
 private TextInputLayout mPassword;
 private FirebaseAuth mAuth;
 private ProgressDialog mRegProgress;
private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);


        Toolbar mToolbar = findViewById(R.id.login_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Create Account");
         getSupportActionBar().setDisplayHomeAsUpEnabled(true);

       mRegProgress = new ProgressDialog(this);


        mDisplayName = (TextInputLayout)findViewById(R.id.textInputLayout);
        mEmail= (TextInputLayout)findViewById(R.id.textInputLayout2);
        mPassword = (TextInputLayout)findViewById(R.id.textInputLayout3);
        Button mCreate = (Button) findViewById(R.id.reg_create_btn);

        mAuth = FirebaseAuth.getInstance();
        mCreate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String display_name = mDisplayName.getEditText().getText().toString();
                String email = mEmail.getEditText().getText().toString();
                String password = mPassword.getEditText().getText().toString();
                mRegProgress.setTitle("Registering user");
                mRegProgress.setMessage("Please wait while we create your account !");
                mRegProgress.setCanceledOnTouchOutside(false);
               mRegProgress.show();
                register_user(display_name,email,password);
                Log.d("name",display_name);
               Log.d("email",email);
               Log.d("password",password);
            }
        });

    }

    private void register_user(final String display_name, String email, String password) {
       mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {



                    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = current_user.getUid();
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                    HashMap<String, String> userMap = new HashMap<>();
                    userMap.put("name",display_name);
                    userMap.put("status","Hi there I'm using Lapit Chat");
                    userMap.put("image","default");
                    userMap.put("thumb_image","default");
                    mDatabase.setValue(userMap);
                    mRegProgress.hide();

                    Intent maIntent = new Intent(RegActivity.this,MainActivity.class);
                    maIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(maIntent);
                    finish();
                }else{
                    Toast.makeText(RegActivity.this,"Cannot Sign In. Please check form and try again",Toast.LENGTH_LONG).show();
                }
            }


       });
    }

}
