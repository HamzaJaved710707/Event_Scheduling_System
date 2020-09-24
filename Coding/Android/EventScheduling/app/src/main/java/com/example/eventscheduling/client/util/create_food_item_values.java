package com.example.eventscheduling.client.util;

public class create_food_item_values {
    String name;
    String imgUrl;

    public create_food_item_values(){
        // empty constructor needed for firebase
    }

    public create_food_item_values(String name, String imgUrl) {
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

    @Override
    public String toString() {
        return "create_food_item_values{" +
                "name='" + name + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }
}
