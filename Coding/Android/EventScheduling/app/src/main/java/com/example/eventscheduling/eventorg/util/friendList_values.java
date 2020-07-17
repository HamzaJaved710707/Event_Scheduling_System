package com.example.eventscheduling.eventorg.util;

public class friendList_values {
    String Name;
    int imgUrl;

    public friendList_values(){

    }

    // Constructor for this class
    public friendList_values(String name, int imgUrl) {
        this.Name = name;
        this.imgUrl = imgUrl; }

    public String getName() { return Name; }

    public void setName(String name) { this.Name = name; }

    public int getImgUrl() { return imgUrl; }

    public void setImgUrl(int imgUrl) { this.imgUrl = imgUrl; }
}
