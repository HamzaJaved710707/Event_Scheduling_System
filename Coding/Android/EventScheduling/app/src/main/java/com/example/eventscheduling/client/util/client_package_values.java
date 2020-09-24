package com.example.eventscheduling.client.util;


import java.util.ArrayList;

public class client_package_values {
    private String PackageName;
    private String price;
    private String Venue;
    private String image;
    private int rating;
    private String businessName;
    public client_package_values(){
        // empty constructor for firebase
    }

    public  client_package_values(String packageName, String image, ArrayList<String> food, ArrayList<String> services, String venue, String price, String businessName) {
        PackageName = packageName;
        this.price = price;
        Venue = venue;
        this.image = image;
        this.rating = rating;
        this.businessName = businessName;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getPackageName() {
        return PackageName;
    }

    public void setPackageName(String packageName) {
        PackageName = packageName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getVenue() {
        return Venue;
    }

    public void setVenue(String venue) {
        Venue = venue;
    }

    public String getImageAdr() {
        return image;
    }

    public void setImageAdr(String imageAdr) {
        this.image = imageAdr;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
