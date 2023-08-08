package com.example.v_chat.models;

public class MessageModel {

    private String uID;
    private String message;
    private String messageID;
    private long timestamp;

    public MessageModel() {
    }

    public MessageModel(String uID, String message, long timestamp) {
        this.uID = uID;
        this.message = message;
        this.timestamp = timestamp;
    }

    public MessageModel(String uID, String message) {
        this.uID = uID;
        this.message = message;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}

