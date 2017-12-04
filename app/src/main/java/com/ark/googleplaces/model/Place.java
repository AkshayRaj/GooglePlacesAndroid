package com.ark.googleplaces.model;

public class Place {
    String id;
    String name;
    String icon; //icon url
    String vicinity;
    Double lat;
    Double lon;

    public Place(String id, String name, String icon, String vicinity, double lat, double lon){
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.vicinity = vicinity;
        this.lat = lat;
        this.lon = lon;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }

    public String getVicinity() {
        return vicinity;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLon() {
        return lon;
    }


}
