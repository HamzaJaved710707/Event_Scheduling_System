package com.example.eventscheduling.client.util;

public class history_values {
    private int imageResource;
    private   String m_name;
    private String m_priceTag;
    public history_values(int imgRes, String name, String priceTag){
        imageResource = imgRes;
        m_name = name;
        m_priceTag = priceTag;
    }
    public int getImageResource(){
        return  imageResource;
    }
    public String getM_name(){
        return m_name;
    }
    public String getM_priceTag() { return m_priceTag;}
}
