package com.example.chatapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends Activity {
    private EditText edt_email, edt_password, edt_retypePassword;
    private AppCompatButton btn_login, btn_register;

    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://dating-app-eddc9-default-rtdb.firebaseio.com/");
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edt_email=findViewById(R.id.edt_email);
        edt_password=findViewById(R.id.edt_password);
        edt_retypePassword=findViewById(R.id.edt_retypePassword);
        btn_register=findViewById(R.id.btn_register);
        btn_login=findViewById(R.id.btn_login);
        //firebase authentication
        mAuth=FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String txt_email=edt_email.getText().toString();
                final String txt_password=edt_password.getText().toString();
                final String txt_retype=edt_retypePassword.getText().toString();
                if(txt_email.isEmpty()){
                    Toast.makeText(Register.this,"Please type your email!", Toast.LENGTH_SHORT).show();
                }
                else if(txt_password.isEmpty()){
                    Toast.makeText(Register.this,"Please type your password !", Toast.LENGTH_SHORT).show();
                }
                else if(txt_retype.isEmpty()){
                    Toast.makeText(Register.this,"Please retype your password !", Toast.LENGTH_SHORT).show();
                }
                else if(!txt_password.equals(txt_retype)){
                    Toast.makeText(Register.this,"Your password and retype password is not match !", Toast.LENGTH_SHORT).show();
                }
                else{
                    RegisterWithEmail(txt_email, txt_password);
                }
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent myIntent = new Intent(Register.this, Login.class);
                startActivity(myIntent);
            }
        });
    }

    private void RegisterWithEmail(String email, String password) {
        try {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                //FirebaseUser user = mAuth.getCurrentUser();
                                finish();
                                Intent intent = new Intent(Register.this, SetUserProfile.class);
                                startActivity(intent);
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(Register.this, "Register: Something went wrong !", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
        }
        catch (Exception e){
            Toast.makeText(Register.this, "Register: Something went wrong !", Toast.LENGTH_SHORT).show();
        }
    }
}