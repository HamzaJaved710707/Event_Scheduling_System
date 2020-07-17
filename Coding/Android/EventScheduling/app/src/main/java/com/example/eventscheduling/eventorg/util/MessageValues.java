package com.example.eventscheduling.eventorg.util;

// Class to provide data to message Adapter of Event Organizer
public class MessageValues {
    int imageResource;
    String Name;
    String message;

    public MessageValues() {
    }

    public MessageValues( String Name, int imageResource,String message) {
        this.Name = Name;
        this.imageResource = imageResource;
        this.message = message;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getName() {
        return Name;
    }

    public String getMessage() {
        return message;
    }
}
