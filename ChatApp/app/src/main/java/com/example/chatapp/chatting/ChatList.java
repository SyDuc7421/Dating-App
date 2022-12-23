package com.example.chatapp.chatting;

public class ChatList {
    private String FromUID, ToUID, Message, date, time;

    public ChatList(String fromUID, String toUID, String message, String date, String time) {
        FromUID = fromUID;
        ToUID = toUID;
        Message = message;
        this.date = date;
        this.time = time;
    }
    public String getFromUID() {
        return FromUID;
    }

    public String getToUID() {
        return ToUID;
    }

    public String getMessage() {
        return Message;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
