package com.example.eventscheduling.client.util;

public class client_friendList_values {

    String Name;
    int imgUrl;
    String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public client_friendList_values(String name, int imgUrl, String Email) {
        Name = name;
        this.imgUrl = imgUrl;
        email = Email;
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

    public int getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(int imgUrl) {
        this.imgUrl = imgUrl;
    }
}
