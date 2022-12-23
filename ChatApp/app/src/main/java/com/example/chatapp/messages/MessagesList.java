package com.example.chatapp.messages;

public class MessagesList {
    final String userUID, username, lastMessage, userProfileUrl, chatKey;
    final int isUnSeenMessage;

    public MessagesList(String userUID, String username, String lastMessage,String userProfileUrl, int isUnSeenMessage, String chatKey) {
        this.userUID = userUID;
        this.username = username;
        this.lastMessage = lastMessage;
        this.userProfileUrl=userProfileUrl;
        this.isUnSeenMessage = isUnSeenMessage;
        this.chatKey=chatKey;
    }

    public String getChatKey() {
        return chatKey;
    }

    public String getUserUID() {
        return userUID;
    }

    public String getUserProfileUrl() {
        return userProfileUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public int getIsUnSeenMessage() {
        return isUnSeenMessage;
    }
}
