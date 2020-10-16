package com.example.eventscheduling.eventorg.util;

// Class to provide data to message Adapter of Event Organizer
public class MessageValues {
    String imageResource;
    String Name;
    String message;

    public MessageValues(String imageResource, String name, String message) {
        this.imageResource = imageResource;
        Name = name;
        this.message = message;
    }

    public MessageValues() {
    }

    public String getImageResource() {
        return imageResource;
    }

    public void setImageResource(String imageResource) {
        this.imageResource = imageResource;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
