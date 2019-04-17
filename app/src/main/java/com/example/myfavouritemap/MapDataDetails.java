package com.example.myfavouritemap;

public class MapDataDetails {
double lang;
double lat;
String severity;
String image;

    public MapDataDetails(double lat, double lang, String severity, String image) {
        this.lang = lang;
        this.lat = lat;
        this.severity = severity;
        this.image=image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getLang() {
        return lang;
    }

    public void setLang(int lang) {
        this.lang = lang;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(int lat) {
        this.lat = lat;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }
}

