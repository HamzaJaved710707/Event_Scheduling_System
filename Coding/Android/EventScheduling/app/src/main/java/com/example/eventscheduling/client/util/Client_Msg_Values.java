package com.example.eventscheduling.client.util;

public class Client_Msg_Values {
    private int imageResource;
    private   String messageHeader;
    private String messageDetail;
    public Client_Msg_Values(int imgRes, String msgHeader, String msgDetail){
        imageResource = imgRes;
        messageHeader = msgHeader;
        messageDetail = msgDetail;
    }
    public Client_Msg_Values(){ // Empty constructor
         }
    public int getImageResource(){
        return  imageResource;
    }
    public String getMessageHeader(){
        return messageHeader;
    }
    public String getMessageDetail() { return messageDetail;}
}
