package com.example.eventscheduling.eventorg.util;

public class OrderValues {

    private int imageResource;
    private   String orderHeader;
    private String orderDetail;
    public OrderValues(int imgRes, String oheader, String oDetail){
        imageResource = imgRes;
        orderHeader = oheader;
        orderDetail = oDetail;
    }
    public int getImageResource(){
        return  imageResource;
    }
    public String getOrderHeader(){
        return orderHeader;
    }
    public String getOrderDetail() { return orderDetail;}
}
