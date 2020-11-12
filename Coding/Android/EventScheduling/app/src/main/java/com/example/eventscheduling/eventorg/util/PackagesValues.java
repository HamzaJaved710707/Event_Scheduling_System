package com.example.eventscheduling.eventorg.util;


import android.widget.ProgressBar;

import java.util.ArrayList;

// Class to return values to packages of event Organizer
public class PackagesValues {
    // Variables
    private String PackageName;
    private String image;
    private ArrayList<String> Food;
    private ArrayList<String> Services;
    private ArrayList<String> venue;
    private String price;
    private String businessName;
    private String id;
    private String userId;


    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public PackagesValues(String packageName, String image, ArrayList<String> food, ArrayList<String> services, ArrayList<String> venue, String price, String businessName, String id, String userId) {
        PackageName = packageName;
        this.image = image;
        Food = food;
        Services = services;
        this.venue = venue;
        this.price = price;
        this.businessName = businessName;
        this.id = id;
        this.userId = userId;
    }

    public PackagesValues() {
        // empty constructor for firebase recycler
    }

    public String getPackageName() {
        return PackageName;
    }

    public void setPackageName(String packageName) {
        PackageName = packageName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<String> getFood() {
        return Food;
    }

    public void setFood(ArrayList<String> food) {
        Food = food;
    }

    public ArrayList<String> getServices() {
        return Services;
    }

    public void setServices(ArrayList<String> services) {
        Services = services;
    }

    public ArrayList<String> getVenue() {
        return venue;
    }

    public void setVenue(ArrayList<String> venue) {
        this.venue = venue;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


}
