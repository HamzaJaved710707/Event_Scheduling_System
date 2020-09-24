package com.example.eventscheduling.eventorg.model;

import androidx.annotation.NonNull;

public class DragData {

    public final int width;
    public final int height;
    public String name;
    public String url;
    public int val;

    public DragData( int width, int height, String name, String url,int val) {
        this.width = width;
        this.height = height;
        this.name = name;
        this.url = url;
        this.val = val;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }

    @NonNull
    @Override
    public String toString() {
        return "DragData{" +
                "width=" + width +
                ", height=" + height +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", val=" + val +
                '}';
    }
}
