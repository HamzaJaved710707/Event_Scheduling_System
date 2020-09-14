package com.example.eventscheduling.eventorg.util;

public class friendList_values {
    String Name;
    int imgUrl;
    String email;

    public friendList_values() {

    }

    // Constructor for this class
    public friendList_values(String name, int imgUrl, String email) {
        this.Name = name;
        this.imgUrl = imgUrl;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        email = email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public int getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(int imgUrl) {
        this.imgUrl = imgUrl;
    }
}
