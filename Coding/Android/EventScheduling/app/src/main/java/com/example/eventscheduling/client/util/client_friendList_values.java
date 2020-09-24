package com.example.eventscheduling.client.util;

public class client_friendList_values {

    String Name;
    String imgUrl;

    String id;

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public client_friendList_values(String name, String imgUrl, String id) {
        Name = name;
        this.imgUrl = imgUrl;
        this.id = id;
    }

    public client_friendList_values(){
        // empty constructor for firebase values
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

}
