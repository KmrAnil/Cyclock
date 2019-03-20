package com.cyclock.anil4.cyclock;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Registration extends AppCompatActivity {
    private EditText Remail,Rpassword,Rconfirmpassword;
    private Button Register;
    private FirebaseAuth firebaseAuth;
    String email,password,confirmpasword;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Remail =findViewById(R.id.Remail);
        Rpassword =findViewById(R.id.Rpassword);
        Rconfirmpassword =findViewById(R.id.Rconfirmpassword);
        Register=findViewById(R.id.NewUserBtn);
        firebaseAuth =FirebaseAuth.getInstance();
        progressDialog =new ProgressDialog(this);

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){
                    if(Rpassword.getText().toString().equals(Rconfirmpassword.getText().toString())){
                        progressDialog.setMessage("Adding New User!");
                        firebaseAuth.createUserWithEmailAndPassword(Remail.getText().toString(),Rpassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    progressDialog.dismiss();
                                    Toast.makeText(Registration.this, "Registration Successful", Toast.LENGTH_LONG).show();
                                    finish();
                                    startActivity(new Intent(Registration.this,AdminSecond.class));
                                }else{
                                    progressDialog.dismiss();
                                    Toast.makeText(Registration.this, "Registation Failed", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }else{
                        Toast.makeText(Registration.this, "Password and Confirm Password must be Same", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }
    private Boolean validate(){
        Boolean result =false;
        password = Rpassword.getText().toString();
        email = Remail.getText().toString();
        confirmpasword =Rconfirmpassword.getText().toString();

        if(password.isEmpty() ||  email.isEmpty() || confirmpasword.isEmpty() ){
            Toast.makeText(this, "Please enter all Details", Toast.LENGTH_SHORT).show();
        }else{
            result = true;
        }
        return result;
    }
}
