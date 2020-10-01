package com.example.eventscheduling.eventorg.util;

public class friendList_values {
    String Name;
    String imgUrl;
    String email;

    public friendList_values() {

    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public friendList_values(String name, String imgUrl, String email) {
        Name = name;
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


}
