package com.example.carparkmainmenu;

//the name want to show in firebase
public class ParkUserProfile_avaSlot {
    public String aaaParkName;
    public String motor;
    public String privateCar;
    public String truck;

    // this."name show in firebase" = "id used in program"
    public ParkUserProfile_avaSlot(String parkName, String motor, String privateCar, String truck) {
        this.aaaParkName = parkName;
        this.motor = motor;
        this.privateCar = privateCar;
        this.truck = truck;
    }
}
