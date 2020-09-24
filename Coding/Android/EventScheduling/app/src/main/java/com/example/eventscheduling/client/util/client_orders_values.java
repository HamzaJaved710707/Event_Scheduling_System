package com.example.eventscheduling.client.util;

public class client_orders_values {
    String Name;
    String imgUrl;
    String id;

    public client_orders_values() {
        // Empty constructor needed for firebase UI
    }

    public client_orders_values(String name, String imgUrl, String id) {
        Name = name;
        this.imgUrl = imgUrl;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
