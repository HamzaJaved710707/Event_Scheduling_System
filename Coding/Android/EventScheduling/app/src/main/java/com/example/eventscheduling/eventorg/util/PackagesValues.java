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
    private String Venue;
    private String Price;



    public PackagesValues(String packageName, String image, ArrayList<String> food, ArrayList<String> services, String venue, String price) {
        PackageName = packageName;
        this.image = image;
        Food = food;
        Services = services;
        Venue = venue;
        Price = price;
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

    public String getVenue() {
        return Venue;
    }

    public void setVenue(String venue) {
        Venue = venue;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }


}
