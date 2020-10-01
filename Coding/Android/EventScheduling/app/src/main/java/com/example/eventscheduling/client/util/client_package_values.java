package com.example.eventscheduling.client.util;


import java.util.ArrayList;

public class client_package_values {
    private String PackageName;
    private String image;
    private ArrayList<String> Food;
    private ArrayList<String> Services;
    private String Venue;
    private String price;
    private String businessName;
    private String id;
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public client_package_values() {
    }


    public client_package_values(String packageName, String image, ArrayList<String> food, ArrayList<String> services, String venue, String price, String businessName, String id, String userId) {
        PackageName = packageName;
        this.image = image;
        Food = food;
        Services = services;
        Venue = venue;
        this.price = price;
        this.businessName = businessName;
        this.id = id;
        this.userId = userId;
    }

    public String getPackageName() {
        return PackageName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getVenue() {
        return Venue;
    }

    public void setVenue(String venue) {
        Venue = venue;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }
}
