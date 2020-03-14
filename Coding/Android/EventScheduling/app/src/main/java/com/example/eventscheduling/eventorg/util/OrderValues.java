package com.example.eventscheduling.eventorg.util;

// Class to return values to Order in Event organizer part
public class OrderValues {

    private int imageResource;
    private String orderHeader;
    private String orderDetail;

    public OrderValues(int imgRes, String oheader, String oDetail) {
        imageResource = imgRes;
        orderHeader = oheader;
        orderDetail = oDetail;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getOrderHeader() {
        return orderHeader;
    }

    public String getOrderDetail() {
        return orderDetail;
    }
}
