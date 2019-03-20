package com.cyclock.anil4.cyclock;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserSecond extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private Button request,cancelRequest,lock,unlock;
    private DatabaseReference databaseReference;
    private String state;
    private TextView requestStatus;
    private long backPressedTime;
    private Toast backToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_second);
        firebaseAuth =FirebaseAuth.getInstance();
        request =findViewById(R.id.request);
        state = "not_request";
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        cancelRequest =findViewById(R.id.cancelRequest);
        requestStatus =findViewById(R.id.requestStatus);
        lock = findViewById(R.id.lock);
        unlock =findViewById(R.id.unlock);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Request_List");
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(state.equals("not_request")){
                    databaseReference.child(currentUser.getUid()).child("email").setValue(currentUser.getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(UserSecond.this, "Request Send", Toast.LENGTH_SHORT).show();
                                request.setEnabled(false);
                                request.setText("Request Sended");

                            }else{
                                Toast.makeText(UserSecond.this, "Failed Sending Request", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });
        cancelRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child(currentUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        request.setEnabled(true);
                        request.setText("Request");
                        Toast.makeText(UserSecond.this, "Request Canceled", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userNameRef = rootRef.child("Accept_List").child(currentUser.getUid());
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()) {
                    requestStatus.setText("Rejected");
                    request.setEnabled(false);
                }
                else{
                    requestStatus.setText("Accepted");
                    final DatabaseReference newdata=FirebaseDatabase.getInstance().getReference().child("Locker");
                    lock.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            newdata.child("Status").setValue("0").addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(UserSecond.this, "Cycle Locked", Toast.LENGTH_SHORT).show();
                                        lock.setEnabled(false);
                                        unlock.setEnabled(true);

                                    }else{
                                        Toast.makeText(UserSecond.this, "Failed in Locking", Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(UserSecond.this, "Cycle Unlocked", Toast.LENGTH_SHORT).show();
                                        unlock.setEnabled(false);
                                        lock.setEnabled(true);

                                    }else{
                                        Toast.makeText(UserSecond.this, "Failed in Unlocking", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                    });


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        userNameRef.addListenerForSingleValueEvent(eventListener);


    }

    private void logout(){
        firebaseAuth.signOut();
        startActivity(new Intent(UserSecond.this,MainActivity.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logoutMenu1:
                logout();
                break;
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
