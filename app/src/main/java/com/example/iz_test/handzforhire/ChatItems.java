package com.example.iz_test.handzforhire;

/**
 * Created by IZ-Parimala on 09-07-2018.
 */

public class ChatItems {

    public String senderId;

    public String photoURL;

    public String message;

    public boolean has_Attachemnt;

    public boolean isMe;

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    private String senderName;

    public String getSenderId() {
        return senderId;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public boolean isHas_Attachemnt() {
        return has_Attachemnt;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }


    public void setHas_Attachemnt(boolean has_Attachemnt) {
        this.has_Attachemnt = has_Attachemnt;
    }

    public boolean isMe() {
        return isMe;
    }

    public void setMe(boolean me) {
        isMe = me;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
