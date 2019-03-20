package com.cyclock.anil4.cyclock;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminSecond extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private Button requestList,locationbtn,lock,unlock;
    private long backPressedTime;
    private Toast backToast;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_second);
        firebaseAuth =FirebaseAuth.getInstance();
        requestList =findViewById(R.id.requestList);
        locationbtn=findViewById(R.id.locationbtn);
        lock = findViewById(R.id.lock);
        unlock=findViewById(R.id.unlock);

        locationbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference("Location");
                firebaseDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Loc location= dataSnapshot.getValue(Loc.class);
                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                Uri.parse("https://www.google.com/maps?&z=15&mrt=yp&t=k&q="+location.getLat()+"+"+location.getLon()));
                        intent.setPackage("com.android.chrome");
                        startActivity(intent);

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(AdminSecond.this, databaseError.getCode(), Toast.LENGTH_LONG).show();

                    }
                });


            }
        });
        final DatabaseReference newdata=FirebaseDatabase.getInstance().getReference().child("Locker");
        lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newdata.child("Status").setValue("0").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(AdminSecond.this, "Cycle Locked", Toast.LENGTH_SHORT).show();
                            lock.setEnabled(false);
                            unlock.setEnabled(true);

                        }else{
                            Toast.makeText(AdminSecond.this, "Failed in Locking", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
        //Unlock Btn
        unlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newdata.child("Status").setValue("1").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(AdminSecond.this, "Cycle Unlocked", Toast.LENGTH_SHORT).show();
                            unlock.setEnabled(false);
                            lock.setEnabled(true);

                        }else{
                            Toast.makeText(AdminSecond.this, "Failed in Unlocking", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        requestList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminSecond.this, RequestListActivity.class));
            }
        });
    }
    private void Logout(){

        firebaseAuth.signOut();
        startActivity(new Intent(AdminSecond.this,MainActivity.class));
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logoutMenu:{
                Logout();
                break;
            }
            case R.id.newUser:{
                startActivity(new Intent(AdminSecond.this,Registration.class));
            }

        }
        return super.onOptionsItemSelected(item);
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
