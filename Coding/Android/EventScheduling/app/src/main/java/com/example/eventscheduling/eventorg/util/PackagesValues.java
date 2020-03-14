package com.example.eventscheduling.eventorg.util;


// Class to return values to packages of event Organizer
public class PackagesValues {

    private int imageResource;
    private String PackageName;
    private String packagePrice;
    private float rating;
    private String location;

    public PackagesValues(int imgRes, String packageName, String mpackagePrice, String mlocation, float mRating) {
        imageResource = imgRes;
        PackageName = packageName;
        packagePrice = mpackagePrice;
        location = mlocation;
        rating = mRating;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getPackageName() {
        return PackageName;
    }

    public String getPackagePrice() {
        return packagePrice;
    }

    public String getLocation() {
        return location;
    }

    public float getRating() {
        return rating;
    }
}
