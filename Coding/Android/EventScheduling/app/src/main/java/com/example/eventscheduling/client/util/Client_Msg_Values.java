package com.example.eventscheduling.client.util;

public class Client_Msg_Values {
    int imageResource;
    String Name;
    String message;

    public Client_Msg_Values(int imageResource, String name, String message) {
        this.imageResource = imageResource;
        Name = name;
        this.message = message;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Client_Msg_Values(){ // Empty constructor
         }

}
