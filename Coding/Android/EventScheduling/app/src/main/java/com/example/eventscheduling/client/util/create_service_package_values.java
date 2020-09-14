package com.example.eventscheduling.client.util;

public class create_service_package_values {
    private String name;
    private String imgUrl;
public create_service_package_values(){
    // needed for firebase
}
    public create_service_package_values(String name, String imgUrl) {
        this.name = name;
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
