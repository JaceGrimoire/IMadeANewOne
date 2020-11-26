package com.example.ordering;

public class Location {
    double origin_lat;
    double origin_long;
    double destination_lat;
    double destination_long;
    String uid;
    String rider_uid;
    String state;

    public Location(){}

    public Location(double origin_lat, double origin_long, double destination_lat, double destination_long, String uid, String rider_uid, String state) {
        this.origin_lat = origin_lat;
        this.origin_long = origin_long;
        this.destination_lat = destination_lat;
        this.destination_long = destination_long;
        this.uid = uid;
        this.rider_uid = rider_uid;
        this.state = state;
    }

    public double getOrigin_lat() {
        return origin_lat;
    }

    public void setOrigin_lat(double origin_lat) {
        this.origin_lat = origin_lat;
    }

    public double getOrigin_long() {
        return origin_long;
    }

    public void setOrigin_long(double origin_long) {
        this.origin_long = origin_long;
    }

    public double getDestination_lat() {
        return destination_lat;
    }

    public void setDestination_lat(double destination_lat) {
        this.destination_lat = destination_lat;
    }

    public double getDestination_long() {
        return destination_long;
    }

    public void setDestination_long(double destination_long) {
        this.destination_long = destination_long;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getRider_uid() {
        return rider_uid;
    }

    public void setRider_uid(String rider_uid) {
        this.rider_uid = rider_uid;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
