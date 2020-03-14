package com.example.eventscheduling.eventorg.util;

// Class to provide data to message Adapter of Event Organizer
public class MessageValues {
    private int imageResource;
    private String messageHeader;
    private String messageDetail;

    public MessageValues(int imgRes, String msgHeader, String msgDetail) {
        imageResource = imgRes;
        messageHeader = msgHeader;
        messageDetail = msgDetail;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getMessageHeader() {
        return messageHeader;
    }

    public String getMessageDetail() {
        return messageDetail;
    }
}
