package com.example.chatapp;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SetUserProfile extends Activity {

    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://dating-app-eddc9-default-rtdb.firebaseio.com/");
    private FirebaseAuth mAuth;

    private EditText edt_fullName, edt_birthdDate;
    private RadioGroup rg_gender;
    private RadioButton rb_male, rb_female, rb_others;
    private TextView txt_birthDate;
    private AppCompatButton btn_datePicker, btn_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_user_profile);

        edt_birthdDate=findViewById(R.id.edt_birthDate);
        edt_fullName=findViewById(R.id.edt_fullName);
        rg_gender=findViewById(R.id.rg_gender);
        rb_male=findViewById(R.id.rb_male);
        rb_female=findViewById(R.id.rb_female);
        rb_others=findViewById(R.id.rb_others);
        btn_datePicker=findViewById(R.id.btn_date_picker);
        btn_submit=findViewById(R.id.btn_submit);
        mAuth=FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }
        Toast.makeText(SetUserProfile.this,mAuth.getUid(), Toast.LENGTH_SHORT).show();
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String uid=mAuth.getUid();
                final String fullName=edt_fullName.getText().toString();
                String gender;
                String birthDate=edt_birthdDate.getText().toString();
                if(rb_male.isChecked())
                    gender="Male";
                else if(rb_female.isChecked())
                    gender="Female";
                else gender="Others";
                //Toast.makeText(SetUserProfile.this,uid, Toast.LENGTH_SHORT).show();

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        databaseReference.child("Users").child(uid).child("Email").setValue("");
                        databaseReference.child("Users").child(uid).child("Username").setValue(fullName);
                        databaseReference.child("Users").child(uid).child("URL").setValue("");
                        databaseReference.child("Users").child(uid).child("Gender").setValue(gender);
                        databaseReference.child("Users").child(uid).child("BirthDate").setValue(birthDate);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                Intent myIntent=new Intent(SetUserProfile.this,MainActivity.class);
                startActivity(myIntent);
            }
        });

    }
}