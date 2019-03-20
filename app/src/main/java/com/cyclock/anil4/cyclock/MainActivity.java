package com.cyclock.anil4.cyclock;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private Button AdminBtn;
    private Button UserBtn;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private long backPressedTime;
    private Toast backToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AdminBtn = findViewById(R.id.AdminBtn);
        UserBtn =findViewById(R.id.UserBtn);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser =FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser!=null && currentUser.getUid().equals("ZxPrUEYN3AZDm3zmAntE0kjMW3h2")){
            startActivity(new Intent(MainActivity.this,AdminSecond.class));
            finish();
        }
        if(currentUser!=null && !currentUser.getUid().equals("ZxPrUEYN3AZDm3zmAntE0kjMW3h2")){
            startActivity(new Intent(MainActivity.this,UserSecond.class));
            finish();

        }
        AdminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,AdminActivity.class));
                finish();
            }
        });
        UserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,UserActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(backPressedTime+2000>System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            return;
        }else{
            backToast =Toast.makeText(getBaseContext(),"Enter back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime =System.currentTimeMillis();
    }



}
