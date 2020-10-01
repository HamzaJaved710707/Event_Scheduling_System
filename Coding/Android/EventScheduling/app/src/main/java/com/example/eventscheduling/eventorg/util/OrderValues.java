package com.example.eventscheduling.eventorg.util;

// Class to return values to Order in Event organizer part
public class OrderValues {
    String Name;
    String imgUrl;
    String id;

    public OrderValues(String name, String imgUrl, String id) {
        Name = name;
        this.imgUrl = imgUrl;
        this.id = id;
    }

    public OrderValues(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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


}
