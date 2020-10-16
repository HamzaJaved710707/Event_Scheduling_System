package com.example.eventscheduling.eventorg.util;

public class friendList_values {
    String Name;
    String imgUrl;
    String email;
    long type;
    String id;

    public friendList_values() {

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getType() {
        return type;
    }

    public void setType(long type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public friendList_values(String name, String imgUrl, String email, long type, String id) {
        Name = name;
        this.imgUrl = imgUrl;
        this.email = email;
        this.type = type;
        this.id = id;
    }
}
