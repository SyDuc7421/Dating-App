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

public class Login extends Activity {
    private EditText edt_email, edt_password;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edt_email=findViewById(R.id.edt_email);
        edt_password=findViewById(R.id.edt_password);
        AppCompatButton btn_login = findViewById(R.id.btn_login);
        AppCompatButton btn_register=findViewById(R.id.btn_register);
        //firebase authentication
        mAuth=FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email=edt_email.getText().toString();
                final String password=edt_password.getText().toString();
                LoginWithEmail(email,password);
            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent=new Intent(Login.this,Register.class);
                startActivity(intent);
            }
        });
    }

    private void LoginWithEmail(String email, String password) {
        try {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                finish();
                                Intent myIntent = new Intent(Login.this, MainActivity.class);
                                startActivity(myIntent);
                                Toast.makeText(Login.this, "Login success with Email/Password !", Toast.LENGTH_SHORT).show();
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(Login.this, "Login: Something went wrong !", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        catch (Exception e){
            Toast.makeText(Login.this, "Login: Something went wrong !", Toast.LENGTH_SHORT).show();
        }
    }
}