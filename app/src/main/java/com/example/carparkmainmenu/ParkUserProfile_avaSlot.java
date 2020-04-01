package com.example.carparkmainmenu;

//the name want to show in firebase
public class ParkUserProfile_avaSlot {
    public String motor;
    public String privateCar;
    public String truck;
    public String flexible_Pricing;

    // this."name show in firebase" = "id used in program"
    public ParkUserProfile_avaSlot(String motor, String privateCar, String truck, String flexible_Pricing) {
        this.motor = motor;
        this.privateCar = privateCar;
        this.truck = truck;
        this.flexible_Pricing = flexible_Pricing;
    }
}
