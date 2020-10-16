package com.example.eventscheduling.client.util;

public class client_friendList_values {

    String Name;
    String imgUrl;

    String id;
     long type;

    public client_friendList_values(String name, String imgUrl, String id, long type) {
        Name = name;
        this.imgUrl = imgUrl;
        this.id = id;
        this.type = type;
    }

    public client_friendList_values() {
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

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getType() {
        return type;
    }

    public void setType(long type) {
        this.type = type;
    }
}
