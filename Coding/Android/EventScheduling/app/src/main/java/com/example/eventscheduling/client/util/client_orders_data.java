package com.example.eventscheduling.client.util;

public class client_orders_data {
    String to;
    String packageId;
    String orderId;
    String from;

    public client_orders_data() {
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public client_orders_data(String to, String packageId, String orderId, String from) {
        this.to = to;
        this.packageId = packageId;
        this.orderId = orderId;
        this.from = from;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }
}
