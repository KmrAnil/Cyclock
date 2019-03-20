package com.cyclock.anil4.cyclock;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

public class AdminActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private Button login;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        username = (EditText)findViewById(R.id.username);
        password =(EditText)findViewById(R.id.password);
        login = (Button)findViewById(R.id.login);
        firebaseAuth =FirebaseAuth.getInstance();
        progressDialog =new ProgressDialog(this);
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user!=null){
            finish();
            startActivity(new Intent(AdminActivity.this,AdminSecond.class));
            finish();

        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name =username.getText().toString().trim()+"@gmail.com";
                validate(name, password.getText().toString().trim());
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
                    Toast.makeText(AdminActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AdminActivity.this,AdminSecond.class));

                }else {
                    progressDialog.dismiss();
                    Toast.makeText(AdminActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

}
