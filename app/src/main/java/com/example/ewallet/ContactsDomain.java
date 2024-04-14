package com.example.ewallet;

public class ContactsDomain {
    private String name;
    private String picPath;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public ContactsDomain(String name, String picPath) {
        this.name = name;
        this.picPath = picPath;
    }
}
