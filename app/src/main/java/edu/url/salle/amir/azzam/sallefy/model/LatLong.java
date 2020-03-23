package edu.url.salle.amir.azzam.sallefy.model;

public class LatLong {
    private double latitude;

    private double longitude;

    public LatLong(double latitude, double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setLatitude(int latitude){
        this.latitude = latitude;
    }
        public double getLatitude(){
        return this.latitude;
    }
        public void setLongitude(int longitude){
        this.longitude = longitude;
    }
        public double getLongitude(){
        return this.longitude;
    }
}
