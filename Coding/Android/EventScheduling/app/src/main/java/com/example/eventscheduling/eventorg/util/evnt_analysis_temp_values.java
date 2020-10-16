package com.example.eventscheduling.eventorg.util;

public class evnt_analysis_temp_values {
    String id;
    int count;

    public evnt_analysis_temp_values(String id, int count) {
        this.id = id;
        this.count = count;
    }

    public evnt_analysis_temp_values() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
