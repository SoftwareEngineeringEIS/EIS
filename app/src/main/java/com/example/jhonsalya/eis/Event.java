package com.example.jhonsalya.eis;

/**
 * Created by jhonsalya on 12/15/17.
 */

public class Event {
    private String title, desc, image;

    public Event(){

    }
    public Event(String title, String desc, String image){
        this.title = title;
        this.desc = desc;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public String getImage() {
        return image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
