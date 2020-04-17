package com.example.carparkmainmenu;

import java.util.Date;

public class ParkBookingRef {
    public String ParkName;
    public Date Time;


    // this."name show in firebase" = "id used in program"
    public ParkBookingRef(String park_Name, Date time) {
        this.ParkName = park_Name;
        this.Time = time;
    }
}
