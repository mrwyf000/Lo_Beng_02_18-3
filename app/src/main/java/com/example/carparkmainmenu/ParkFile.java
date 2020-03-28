package com.example.carparkmainmenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;


public class ParkFile extends AppCompatActivity {

    private static final String TAG = "ParkFile";

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private Button logout, submit;
    private EditText parkName, parkAddress, motor, privateCar, truck, parkingFee, minimunCharge,flexiblePricing, flexiblePriceFee;
    //private CheckBox flexiblePricing, mcHalfHour, mcOneHour, mcTwoHour;
    String park_name, park_address, motor_Car, private_Car, truck_Car, parking_Fee, halfhr, onehr, twohr, flexible_Pricing, minimun_Charge, flexible_Fee;
    String name, email;

    //set up
    private void setupUIViews() {
        parkName = (EditText) findViewById(R.id.edParkName);
        parkAddress = (EditText) findViewById(R.id.edCarParkAddress);
        motor = (EditText) findViewById(R.id.edMotorcycle);
        privateCar = (EditText) findViewById(R.id.edPrivateCar);
        truck = (EditText) findViewById(R.id.edTruck);
        parkingFee = (EditText) findViewById(R.id.edParkingFee);
        minimunCharge = (EditText) findViewById(R.id.edMinimunCharge);
        flexiblePriceFee = (EditText) findViewById(R.id.edFlexiblePricingFee);
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
        flexible_Fee = flexiblePriceFee.getText().toString();
        flexible_Fee = flexible_Fee.toLowerCase();

        if (park_name.isEmpty() || park_address.isEmpty() || motor_Car.isEmpty() || private_Car.isEmpty() || truck_Car.isEmpty() || parking_Fee.isEmpty() || minimun_Charge.isEmpty() || flexible_Fee.isEmpty()) {
            Toast.makeText(this, "please enter all the details", Toast.LENGTH_SHORT).show();
            return false;

        }
        if (!(flexible_Fee.equals("yes") || flexible_Fee.equals("no"))) {
            Toast.makeText(this, "Flexible Fee needed to be Yes or No", Toast.LENGTH_SHORT).show();
            return false;
        }else {
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

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();


        logout = (Button) findViewById(R.id.btLogout);

        //logout button
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logout();
            }
        });

        //upload user information button
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser!=null) {
                    if (validate()) {
                        sendUserData();
                        sendParkingSlot();
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

    //logout and go to login page
    private void Logout() {
        firebaseAuth.signOut();
        finish();
        Toast.makeText(ParkFile.this, "Logout Successful", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(ParkFile.this, CarParkLogin.class));
    }

    //create a menu bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //logout from menu bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.logoutMenu: {
                Logout();
            }
        }
        return super.onOptionsItemSelected(item);
    }
    private void sendUserData()  {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference().child("Park").child(firebaseAuth.getUid());
        ParkUserProfile parkUserProfile = new ParkUserProfile(park_name, park_address, motor_Car, private_Car, truck_Car, parking_Fee, minimun_Charge, flexible_Fee);
        myRef.setValue(parkUserProfile);
    }

    private void sendParkingSlot(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef2 = firebaseDatabase.getReference().child("AvailableParkingSlot").child(firebaseAuth.getUid());
        ParkUserProfile_avaSlot parkUserProfile_avaSlot = new ParkUserProfile_avaSlot(park_name, motor_Car, private_Car, truck_Car);
        myRef2.setValue(parkUserProfile_avaSlot);
        Log.d(TAG, "Slot data sent!");
    }

}
