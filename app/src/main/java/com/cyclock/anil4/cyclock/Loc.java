package com.cyclock.anil4.cyclock;

public class Loc {
    public String lat;
    public String lon;


    public Loc() {
    }

    public Loc( String lat,String lon) {
        this.lat = lat;
        this.lon = lon;

    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }
}
