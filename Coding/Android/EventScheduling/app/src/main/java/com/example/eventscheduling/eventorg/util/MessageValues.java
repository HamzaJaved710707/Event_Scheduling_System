package com.example.eventscheduling.eventorg.util;

public class MessageValues {
    private int imageResource;
    private   String messageHeader;
    private String messageDetail;
    public MessageValues(int imgRes, String msgHeader, String msgDetail){
        imageResource = imgRes;
        messageHeader = msgHeader;
        messageDetail = msgDetail;
    }
    public int getImageResource(){
        return  imageResource;
    }
    public String getMessageHeader(){
        return messageHeader;
    }
    public String getMessageDetail() { return messageDetail;}
}
