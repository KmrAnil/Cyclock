package com.cyclock.anil4.cyclock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PopUpWindow extends Activity {
    private DatabaseReference newdatabase;
    private Button accept,cancel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popupwindow);

        accept =findViewById(R.id.accept);
        cancel =findViewById(R.id.cancel);

        newdatabase=FirebaseDatabase.getInstance().getReference().child("Accept_List");

        final String user_id = getIntent().getStringExtra("user_id");

        DisplayMetrics dm =new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width =dm.widthPixels;
        int height =dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.4) );
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Request_List");

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newdatabase.child(user_id).child("email").setValue("true").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            databaseReference.child(user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                }
                            });
                            Toast.makeText(PopUpWindow.this, "Request Accepted", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(PopUpWindow.this,RequestListActivity.class));

                        }else{
                            Toast.makeText(PopUpWindow.this, "Request Declined", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PopUpWindow.this,RequestListActivity.class));


            }
        });



    }
}
