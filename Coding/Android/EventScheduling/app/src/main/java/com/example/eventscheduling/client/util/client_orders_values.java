package com.example.eventscheduling.client.util;

public class client_orders_values {
    String Name;
    String imgUrl;
    String packageId;
    String to;
    String from;
    String orderId;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public client_orders_values(String name, String imgUrl, String packageId, String to, String from, String orderId) {
        Name = name;
        this.imgUrl = imgUrl;
        this.packageId = packageId;
        this.to = to;
        this.from = from;
        this.orderId = orderId;
    }

    public client_orders_values() {
        // Empty constructor needed for firebase UI
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
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
