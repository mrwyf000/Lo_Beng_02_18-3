package com.example.carparkmainmenu;

//the name want to show in firebase
public class ParkUserProfile {
    public String aaaParkName;
    public String parkAddress;
    public String motor;
    public String privateCar;
    public String truck;
    public String parkingFee;
    public String minimunCharge;
    public String flexiblePriceFee;

    // this."name show in firebase" = "id used in program"
    public ParkUserProfile(String parkName, String parkAddress, String motor, String privateCar, String truck, String parkingFee, String minimunCharge, String flexiblePriceFee) {
        this.aaaParkName = parkName;
        this.parkAddress = parkAddress;
        this.motor = motor;
        this.privateCar = privateCar;
        this.truck = truck;
        this.parkingFee = parkingFee;
        this.minimunCharge = minimunCharge;
        this.flexiblePriceFee = flexiblePriceFee;
    }
}