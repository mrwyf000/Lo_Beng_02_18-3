package com.example.carparkmainmenu;

//the name want to show in firebase
public class ParkUserProfile {
    public String aaaParkName;
    public String parkAddress;
    public String motor;
    public String privateCar;
    public String truck;
    public String parkingFee;
    public String flexibleFee;
    public String minimunCharge;
    public String avaMotor;
    public String avaPrivateCar;
    public String avaTruck;



    // this."name show in firebase" = "id used in program"
    public ParkUserProfile(String parkName, String parkAddress, String motor, String privateCar,
                           String truck, String parkingFee, String flexibleFee, String minimunCharge,
                           String avaMotor, String avaPrivateCar, String avaTruck) {
        this.aaaParkName = parkName;
        this.parkAddress = parkAddress;
        this.motor = motor;
        this.privateCar = privateCar;
        this.truck = truck;
        this.parkingFee = parkingFee;
        this.flexibleFee = flexibleFee;
        this.minimunCharge = minimunCharge;
        this.avaMotor = avaMotor;
        this.avaPrivateCar = avaPrivateCar;
        this.avaTruck = avaTruck;
    }

    public ParkUserProfile() {

    }

    public String getAaaParkName() {
        return aaaParkName;
    }

    public void setAaaParkName(String aaaParkName) {
        this.aaaParkName = aaaParkName;
    }

    public String getParkAddress() {
        return parkAddress;
    }

    public void setParkAddress(String parkAddress) {
        this.parkAddress = parkAddress;
    }

    public String getMotor() {
        return motor;
    }

    public void setMotor(String motor) {
        this.motor = motor;
    }

    public String getPrivateCar() {
        return privateCar;
    }

    public void setPrivateCar(String privateCar) {
        this.privateCar = privateCar;
    }

    public String getTruck() {
        return truck;
    }

    public void setTruck(String truck) {
        this.truck = truck;
    }

    public String getParkingFee() {
        return parkingFee;
    }

    public void setParkingFee(String parkingFee) {
        this.parkingFee = parkingFee;
    }

    public String getFlexibleFee() {
        return flexibleFee;
    }

    public void setFlexibleFee(String flexibleFee) {
        this.flexibleFee = flexibleFee;
    }

    public String getMinimunCharge() {
        return minimunCharge;
    }

    public void setMinimunCharge(String minimunCharge) {
        this.minimunCharge = minimunCharge;
    }

    public String getAvaMotor() {
        return avaMotor;
    }

    public void setAvaMotor(String avaMotor) {
        this.avaMotor = avaMotor;
    }

    public String getAvaPrivateCar() {
        return avaPrivateCar;
    }

    public void setAvaPrivateCar(String avaPrivateCar) {
        this.avaPrivateCar = avaPrivateCar;
    }

    public String getAvaTruck() {
        return avaTruck;
    }

    public void setAvaTruck(String avaTruck) {
        this.avaTruck = avaTruck;
    }
}