package com.example.eventscheduling.eventorg.util;

import com.google.firebase.firestore.Exclude;

public class msgDetail_values {
    String message;
    String imgUrl;
    Boolean seen;
    String from;
    String timeStamp;
    public msgDetail_values(){
        // empty constructor needed for firebaseUI
    }

    public msgDetail_values(String message,String imgUrl,  Boolean seen, String from, String timeStamp) {
        this.message = message;
        this.seen = seen;
        this.imgUrl = imgUrl;
        this.from = from;
        this.timeStamp = timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public Boolean getSeen() {
        return seen;
    }

    public String getImgUrl(){ return imgUrl;}

    public String getFrom(){ return from; }
    @Exclude
    public String getTimeStamp(){ return timeStamp;}
}
