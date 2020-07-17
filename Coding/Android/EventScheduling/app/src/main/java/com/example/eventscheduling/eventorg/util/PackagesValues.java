package com.example.eventscheduling.eventorg.util;


import android.text.Editable;

import java.util.List;

// Class to return values to packages of event Organizer
public class PackagesValues {
    // Variables
    private String PackageName;
    private String Price;
    private String Venue;
    private String imageAdr;
    private int rating;

    public PackagesValues() {
        // empty constructor for firebase recycler
    }

    // Public constructor
    public PackagesValues(String packageName, List<String> food, List<String> services, String price, String venue, String imageAdr) {
        PackageName = packageName;
        Price = price;
        Venue = venue;
        this.imageAdr = imageAdr;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

// Getter and setter

    public String getPackageName() {
        return PackageName;
    }

    public void setPackageName(String packageName) {
        PackageName = packageName;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getVenue() {
        return Venue;
    }

    public void setVenue(String venue) {
        Venue = venue;
    }

    public String getImageAdr() {
        return imageAdr;
    }

    public void setImageAdr(String imageAdr) {
        this.imageAdr = imageAdr;
    }
}
