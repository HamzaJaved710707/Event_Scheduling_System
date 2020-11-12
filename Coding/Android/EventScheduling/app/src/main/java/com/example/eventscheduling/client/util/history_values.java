package com.example.eventscheduling.client.util;

public class history_values {
    private String imgUrl;
    private String Name;
    private long rate;
    private String id;

    public history_values() {
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public long getRate() {
        return rate;
    }

    public void setRate(long rate) {
        this.rate = rate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public history_values(String imgUrl, String name, long rate, String id) {
        this.imgUrl = imgUrl;
        Name = name;
        this.rate = rate;
        this.id = id;
    }
}
