package com.example.carparkmainmenu;

public class ParkUserProfile {
    public String parkName;
    public String parkAddress;
    public String motor;
    public String privateCar;
    public String truck;
    public String parkingFee;
    public String minimunCharge;
    public String flexiblePriceFee;

    public ParkUserProfile(String parkName, String parkAddress, String motor, String privateCar, String truck, String parkingFee, String minimunCharge, String flexiblePriceFee) {
        this.parkName = parkName;
        this.parkAddress = parkAddress;
        this.motor = motor;
        this.privateCar = privateCar;
        this.truck = truck;
        this.parkingFee = parkingFee;
        this.minimunCharge = minimunCharge;
        this.flexiblePriceFee = flexiblePriceFee;
    }
}