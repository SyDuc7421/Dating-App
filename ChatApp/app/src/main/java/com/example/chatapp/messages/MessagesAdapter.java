package com.example.chatapp.messages;

import android.content.Context;
import android.content.Intent;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.chatting.Chat;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MyViewHolder>{

    private List<MessagesList> messagesLists;
    private final Context context;
    public MessagesAdapter(List<MessagesList> messagesLists, Context context) {
        this.messagesLists = messagesLists;
        this.context = context;
    }

    @NonNull
    @Override
    public MessagesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.messages_adapter_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesAdapter.MyViewHolder holder, int position) {
        MessagesList list=messagesLists.get(position);

        if(!list.getUserProfileUrl().isEmpty()){
            Picasso.get().load(list.getUserProfileUrl()).into(holder.ptr_userProfile);
        }
        holder.txt_username.setText(list.getUsername());
        holder.txt_lastMessage.setText(list.getLastMessage());
        holder.txt_unSeenMassage.setText(list.getIsUnSeenMessage());
        if(list.getIsUnSeenMessage()==0){
            holder.txt_unSeenMassage.setVisibility(View.GONE);
            holder.txt_lastMessage.setTextColor(context.getResources().getColor(R.color.theme_color_light));
        }
        else{
            holder.txt_unSeenMassage.setVisibility(View.VISIBLE);
            holder.txt_lastMessage.setTextColor(context.getResources().getColor(R.color.red));
        }

        holder.l_rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, Chat.class);
                intent.putExtra("UserName",list.getUsername());
                intent.putExtra("UserProfile",list.getUserProfileUrl());
                intent.putExtra("ChatKey",list.getChatKey());
                intent.putExtra("UID",list.getUserUID());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return messagesLists.size();
    }
    public void updateMessagesList(List<MessagesList> messagesLists){
        this.messagesLists=messagesLists;
    }
    static class MyViewHolder extends RecyclerView.ViewHolder {
        final ImageView ptr_userProfile;
        final private TextView txt_username;
        final private TextView txt_lastMessage;
        final private TextView txt_unSeenMassage;
        final private LinearLayout l_rootLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ptr_userProfile=itemView.findViewById(R.id.ptr_userProfile);
            txt_username=itemView.findViewById(R.id.txt_username);
            txt_lastMessage=itemView.findViewById(R.id.txt_lastMessage);
            txt_unSeenMassage=itemView.findViewById(R.id.txt_unSeenMessage);
            l_rootLayout=itemView.findViewById(R.id.rootLayout);
        }
    }
}
