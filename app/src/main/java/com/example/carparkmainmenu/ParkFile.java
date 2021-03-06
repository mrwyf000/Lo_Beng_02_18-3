package com.example.carparkmainmenu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;


public class ParkFile extends AppCompatActivity {

    //set the password pattern, the password must be at least...
    private static final Pattern price_Patten =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    //"(?=.*[a-zA-Z])" +      //any letter
                    //"(?=\\S+$)" +           //no white spaces
                    //".{8,}" +               //at least 8 characters
                    "$");

    private static final String TAG = "ParkFile";

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private Button logout, submit, back;
    private EditText parkName, parkAddress, motor, privateCar, truck, parkingFee, minimunCharge,avamotor, avaprivateCar, avaTruck, latitude, longitude;
    private TextView flexibleFee;
    String park_name, park_address, motor_Car, private_Car, truck_Car, parking_Fee,
            halfhr, onehr, twohr, flexible_Pricing, minimun_Charge, flexible_Fee,
            ava_Motor, ava_Private_Car, ava_Truck, lat_latitude, lng_longitude;
    String name, email;

    //set up
    @SuppressLint("CutPasteId")
    private void setupUIViews() {
        parkName = (EditText) findViewById(R.id.edParkName);
        parkAddress = (EditText) findViewById(R.id.edCarParkAddress);
        motor = (EditText) findViewById(R.id.edMotorcycle);
        privateCar = (EditText) findViewById(R.id.edPrivateCar);
        truck = (EditText) findViewById(R.id.edTruck);
        parkingFee = (EditText) findViewById(R.id.edParkingFee);
        minimunCharge = (EditText) findViewById(R.id.edMinimunCharge);
        flexibleFee = (TextView) findViewById(R.id.tvFlexibleFee);
        avamotor = (EditText)findViewById(R.id.edMotorcycle2);
        avaprivateCar = (EditText)findViewById(R.id.edMotorcycle2);
        avaTruck = (EditText)findViewById(R.id.edTruck2);
        latitude = (EditText)findViewById(R.id.edLat);
        longitude = (EditText)findViewById(R.id.edLng);
        back = (Button)findViewById(R.id.btBack);


        //flexiblePricing = (CheckBox) findViewById(R.id.cbFlexiblePricing);
        //mcHalfHour = (CheckBox) findViewById(R.id.cbMCHalfHour);
        //mcOneHour = (CheckBox) findViewById(R.id.cbMCOneHour);
        //mcTwoHour = (CheckBox) findViewById(R.id.cbMCTwoHour);

    }

    private boolean validate() {
        boolean result = false;

        park_name = parkName.getText().toString();
        park_address = parkAddress.getText().toString();
        motor_Car = motor.getText().toString();
        private_Car = privateCar.getText().toString();
        truck_Car = truck.getText().toString();
        parking_Fee = parkingFee.getText().toString();
        minimun_Charge = minimunCharge.getText().toString();
        flexible_Fee =  parkingFee.getText().toString();
        flexible_Pricing = parkingFee.getText().toString();
        ava_Motor = avamotor.getText().toString();
        ava_Private_Car = avaprivateCar.getText().toString();
        ava_Truck = avaTruck.getText().toString();
        lat_latitude = latitude.getText().toString();
        lng_longitude = longitude.getText().toString();


        if (park_name.isEmpty() || park_address.isEmpty() || motor_Car.isEmpty()
                || private_Car.isEmpty() || truck_Car.isEmpty()
                || parking_Fee.isEmpty() || minimun_Charge.isEmpty()
                || ava_Motor.isEmpty() || ava_Private_Car.isEmpty() || ava_Truck.isEmpty()
                || lat_latitude.isEmpty() || lng_longitude.isEmpty()){
            Toast.makeText(this, "please enter all the details", Toast.LENGTH_SHORT).show();
            return false;
        }

//        }if (!price_Patten.matcher(parking_Fee).matches()) {
//            Toast.makeText(this, "Please enter only digit amount for the parking fee", Toast.LENGTH_SHORT).show();
//            return false;

        if (Double.parseDouble(ava_Motor) > Double.parseDouble(motor_Car)
                ||Double.parseDouble(ava_Private_Car) > Double.parseDouble(private_Car)
                ||Double.parseDouble(ava_Truck) > Double.parseDouble(truck_Car)){
            Toast.makeText(this, "Number of Empty Slot should not larger than number of Parking Slot", Toast.LENGTH_SHORT).show();
            return false;
        }
//        if (!(flexible_Fee.equals("yes") || flexible_Fee.equals("no"))) {
//            Toast.makeText(this, "Flexible Fee needed to be Yes or No", Toast.LENGTH_SHORT).show();
//            return false;
//            }
        else {
            result = true;
        }

        return result;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_park_file);
        setupUIViews();

        submit = (Button) findViewById(R.id.btSubmit);

//        logout = (Button) findViewById(R.id.btLogout);

        //logout button
//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Logout();
//            }
//        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ParkFile.this, takedata.class));
            }
        });

        //upload user information button
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth = FirebaseAuth.getInstance();
                firebaseDatabase = FirebaseDatabase.getInstance();
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser!=null) {
                    if (validate()) {
                        sendUserData();
//                      sendParkingSlot();
                        Toast.makeText(ParkFile.this, "upload successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ParkFile.this, takedata.class));
                    } else {
                        Toast.makeText(ParkFile.this, "upload fail", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(ParkFile.this, "upload fail", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    //toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    private void Logout() {
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            firebaseAuth.signOut();
            finish();
            Toast.makeText(ParkFile.this, "Logout Successful", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ParkFile.this, CarParkLogin.class));
        }else {
            Toast.makeText(ParkFile.this, "You have not login yet", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logoutMenu: {
                Logout();
                break;
            }
            case R.id.loginMenu:{
                firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    Toast.makeText(ParkFile.this, "You are already login", Toast.LENGTH_SHORT).show();
                }else {
                    startActivity(new Intent(ParkFile.this, CarParkLogin.class));
                }
                break;
            }
            case R.id.mapMenu: {
                startActivity(new Intent(ParkFile.this, MapActivity.class));
                break;
            }
            case R.id.profileMenu:{
                startActivity(new Intent(ParkFile.this, takedata.class));
                break;
            }
            case R.id.carParkRegMenu:{
                startActivity(new Intent(ParkFile.this, ParkRegistrationActivity.class));
                break;
            }
            case R.id.refreshMenu:{
                startActivity(new Intent(ParkFile.this, ParkFile.class));
                break;
            }
            case R.id.BookingRecord:{
                startActivity(new Intent(ParkFile.this, BookingRecord.class));
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendUserData()  {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference().child("Park")
                .child(firebaseAuth.getUid()).child(park_name + "1");
        ParkUserProfile parkUserProfile = new ParkUserProfile(
                park_name, park_address, motor_Car, private_Car, truck_Car,
                parking_Fee, minimun_Charge, flexible_Fee, ava_Motor,
                ava_Private_Car, ava_Truck, lat_latitude, lng_longitude);
        myRef.setValue(parkUserProfile);
        Toast.makeText(ParkFile.this, "Upload Successful!", Toast.LENGTH_SHORT).show();
    }

//    private void sendParkingSlot(){
//        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//        DatabaseReference myRef2 = firebaseDatabase.getReference().child("Park").child(firebaseAuth.getUid()).child("AvailableParkingSlot");
//        ParkUserProfile_avaSlot parkUserProfile_avaSlot = new ParkUserProfile_avaSlot(motor_Car, private_Car, truck_Car, flexible_Pricing);
//        myRef2.setValue(parkUserProfile_avaSlot);
//        Log.d(TAG, "Slot data sent!");
//    }

}
