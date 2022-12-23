package com.example.chatapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;


import com.example.chatapp.chatting.Chat;
import com.example.chatapp.messages.Messages;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends Activity {

    private CircleImageView userPicture;
    private FirebaseAuth mAuth;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://dating-app-eddc9-default-rtdb.firebaseio.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userPicture=findViewById(R.id.userProfilePic);
        AppCompatButton btn_messages = findViewById(R.id.btn_messages);

        //Firebase authentication
        mAuth= FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }
        //get profile picture from realtime database
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final String mAuthUid= mAuth.getUid();
                assert mAuthUid != null;
                final String userProfileUrl = snapshot.child("Users").child(mAuthUid).child("URL").getValue(String.class);
                //set profile pic to images view.
                Picasso.get().load(userProfileUrl).into(userPicture);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(MainActivity.this, Messages.class);
                startActivity(intent);
            }
        });
    }
}