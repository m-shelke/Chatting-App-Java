package com.example.chattingappjava;

public class messagesModel {

    String messages;
    String senderId;
    long timestamp;
    String currentTime;

    public messagesModel() {
    }

    public messagesModel(String messages, String senderId, long timestamp, String currentTime) {
        this.messages = messages;
        this.senderId = senderId;
        this.timestamp = timestamp;
        this.currentTime = currentTime;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }
}
