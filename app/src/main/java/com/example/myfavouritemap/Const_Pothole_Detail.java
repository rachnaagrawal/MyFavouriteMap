package com.example.myfavouritemap;

public class Const_Pothole_Detail {

    public Const_Pothole_Detail(String location, String tolocation, String severity, String description, String namee, String urlimage) {
        this.location = location;
        this.urlimage=urlimage;
        this.tolocation = tolocation;
        this.severity = severity;
        this.description = description;
        this.namee = namee;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTolocation() {
        return tolocation;
    }

    public void setTolocation(String tolocation) {
        this.tolocation = tolocation;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNamee() {
        return namee;
    }

    public void setNamee(String namee) {
        this.namee = namee;
    }

    public String location;
    public String tolocation;
    public String severity;
    public String description;
    public String namee;

    public String getUrlimage() {
        return urlimage;
    }

    public void setUrlimage(String urlimage) {
        this.urlimage = urlimage;
    }

    public String urlimage;

    public Const_Pothole_Detail() {

    }

}
