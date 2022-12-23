package com.example.chatapp.messages;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Messages extends Activity {
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://dating-app-eddc9-default-rtdb.firebaseio.com/");
    private FirebaseAuth mAuth;

    private RecyclerView messagesRecyclerView;
    private final List<MessagesList> messagesLists=new ArrayList<MessagesList>();
    private MessagesAdapter messagesAdapter;

    private ImageView userPicture;
    private String  getChatKey="";
    private String lastMessage="";
    int unSeenMassage=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        //Objects
        userPicture=findViewById(R.id.userProfilePic);
        //Firebase authentication
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }
        //set Recycler View
        messagesRecyclerView=findViewById(R.id.list_messagesRecyclerView);
        messagesRecyclerView.setHasFixedSize(true);
        messagesRecyclerView.setLayoutManager( new LinearLayoutManager(this));
        messagesAdapter=new MessagesAdapter(messagesLists,Messages.this);
        messagesRecyclerView.setAdapter(messagesAdapter);
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
        //Add contact list
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesLists.clear();
                for(DataSnapshot dataSnapshot : snapshot.child("Users").child(mAuth.getUid()).child("ContactList").getChildren()){
                    final String mAuthUid=dataSnapshot.getKey();

                    if(!Objects.equals(mAuth.getUid(), mAuthUid)){
                        assert mAuthUid != null;
                        String Name=snapshot.child("Users").child(mAuthUid).child("Username").getValue(String.class);
                        String URL=snapshot.child("Users").child(mAuthUid).child("URL").getValue(String.class);

                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                lastMessage="";
                                getChatKey="";
                                // messagesLists.clear();

                                int getChatCounts=(int)snapshot.getChildrenCount();
                                if(getChatCounts>0){
                                    for (DataSnapshot chatKeyDataSnapshot:snapshot.child("Chat").getChildren()){
                                        if(chatKeyDataSnapshot.hasChild("FirstUID")&&chatKeyDataSnapshot.hasChild("SecondUID")&&chatKeyDataSnapshot.hasChild("Messages")){

                                            final String getFirstUid=chatKeyDataSnapshot.child("FirstUID").getValue(String.class);
                                            final String getSecondUid =chatKeyDataSnapshot.child("SecondUID").getValue(String.class);

                                            assert getFirstUid != null;
                                            if((getFirstUid.equals(mAuthUid) && Objects.equals(getSecondUid, mAuth.getUid())) || (Objects.equals(getSecondUid, mAuthUid) && getFirstUid.equals(mAuth.getUid()))){
                                                getChatKey=chatKeyDataSnapshot.getKey();
                                                for(DataSnapshot chatDataSnapshot:chatKeyDataSnapshot.child("Messages").getChildren()){
                                                    int countMsg = (int)snapshot.child("Chat").child(getChatKey).child("Messages").getChildrenCount();
                                                    int lastMsgSeen = snapshot.child("Users").child(mAuth.getUid()).child("ContactList").child(mAuthUid).getValue(Integer.class);
                                                    unSeenMassage=countMsg-lastMsgSeen;
                                                    lastMessage=chatDataSnapshot.child("msg").getValue(String.class);
                                                }
                                            }
                                        }
                                    }
                                }

                                MessagesList messagesList=new MessagesList(mAuthUid,Name,lastMessage,URL,unSeenMassage,getChatKey);
                                messagesLists.add(messagesList);
                                messagesAdapter.updateMessagesList(messagesLists);
                                messagesRecyclerView.scrollToPosition(messagesLists.size() - 1);
                                //messagesRecyclerView.setAdapter(new MessagesAdapter(messagesLists, Messages.this));
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}