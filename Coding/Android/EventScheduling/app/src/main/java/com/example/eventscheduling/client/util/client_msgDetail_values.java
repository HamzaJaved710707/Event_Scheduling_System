package com.example.eventscheduling.client.util;

import com.google.firebase.Timestamp;

public class client_msgDetail_values {
    String message;
    String imgUrl;
    Boolean seen;
    String from ;
    Timestamp timeStamp;
    public client_msgDetail_values(){
        // empty constructor for firebase values
    }

    public client_msgDetail_values(String message, String imgUrl, Boolean seen, String from, Timestamp timeStamp) {
        this.message = message;
        this.imgUrl = imgUrl;
        this.seen = seen;
        this.from = from;
        this.timeStamp = timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Boolean getSeen() {
        return seen;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }
}
