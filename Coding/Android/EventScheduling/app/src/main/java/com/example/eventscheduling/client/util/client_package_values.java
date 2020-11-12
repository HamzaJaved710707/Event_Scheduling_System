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
    private long ratingStar;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public client_package_values() {
    }

    public long getRatingStar() {

        return ratingStar;
    }

    public void setRatingStar(long ratingStar) {
        this.ratingStar = ratingStar;
    }

    public client_package_values(String packageName, String image, ArrayList<String> food, ArrayList<String> services, String venue, String price, String businessName, String id, String userId, long ratingStar) {
        PackageName = packageName;
        this.image = image;
        Food = food;
        Services = services;
        Venue = venue;
        this.price = price;
        this.businessName = businessName;
        this.id = id;
        this.userId = userId;
        this.ratingStar = ratingStar;
    }

    public String getPackageName() {
        if(PackageName != null)
            return PackageName;
        return "";
    }

    public String getId() {
        if(id == null)
            return "";
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPackageName(String packageName) {
        PackageName = packageName;
    }

    public String getImage() {
        if(image == null)
            return "";
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
        if(price == null)
            return "";
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getBusinessName() {
        if(businessName == null)
            return "";
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }
}
