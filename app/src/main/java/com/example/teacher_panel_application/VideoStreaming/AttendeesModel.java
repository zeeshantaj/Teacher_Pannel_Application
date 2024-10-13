package com.example.teacher_panel_application.VideoStreaming;

public class AttendeesModel {
    private String image,name;

    public AttendeesModel() {
    }

    public AttendeesModel(String image, String name) {
        this.image = image;
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
