package com.example.eventscheduling.eventorg.util;

import java.util.Comparator;

public class evnt_analysis_client_recyc implements Comparable<evnt_analysis_client_recyc>  {
    public String Name;
    String imgUrl;
   public int count;
    public static final Comparator<evnt_analysis_client_recyc> DESCENDING_COMPARATOR = new Comparator<evnt_analysis_client_recyc>() {
        // Overriding the compare method to sort the age
        public int compare(evnt_analysis_client_recyc d, evnt_analysis_client_recyc d1) {
            return d.count - d1.count;
        }
    };
    public evnt_analysis_client_recyc(String name, String imgUrl, int count) {
        Name = name;
        this.imgUrl = imgUrl;
        this.count = count;
    }

    public evnt_analysis_client_recyc() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public int compareTo(evnt_analysis_client_recyc o) {
        return 0;
    }
}
