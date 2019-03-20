package com.cyclock.anil4.cyclock;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private Button login;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private TextView forgetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        username = (EditText)findViewById(R.id.username);
        password =(EditText)findViewById(R.id.password);
        login = (Button)findViewById(R.id.login);
        forgetPassword =(TextView)findViewById(R.id.forgetPassword);
        firebaseAuth =FirebaseAuth.getInstance();
        progressDialog =new ProgressDialog(this);
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user!=null){
            finish();
            startActivity(new Intent(UserActivity.this,UserSecond.class));
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(username.getText().toString().trim(), password.getText().toString().trim());
            }
        });
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserActivity.this,PasswordForget.class));
            }
        });
    }
    private void validate(String username,String password){
        progressDialog.setMessage("Logging");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(username,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(UserActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(UserActivity.this,UserSecond.class));

                }else {
                    progressDialog.dismiss();
                    Toast.makeText(UserActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}

