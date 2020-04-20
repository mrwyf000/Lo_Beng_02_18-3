package com.example.carparkmainmenu;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class takedata extends AppCompatActivity {
    private TextView parkName,parkAddress, numberM, numberP, numberT, parkingFee, minCharge,
            flexibleFee, avaMotor, avaPrivateCar, avaTruck, latitude, longitude;
    private Button update, logout;
    private DatabaseReference reff, myDatabaseRef, reff1;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_takedata);

        parkName = (TextView) findViewById(R.id.edParkName);
        parkAddress = (TextView) findViewById(R.id.edCarParkAddress);
        numberM = (TextView) findViewById(R.id.edMotorcycle);
        numberP = (TextView) findViewById(R.id.edPrivateCar);
        numberT = (TextView) findViewById(R.id.edTruck);
        parkingFee = (TextView) findViewById(R.id.edParkingFee);
        minCharge = (TextView) findViewById(R.id.edMinimunCharge);
        flexibleFee = (TextView) findViewById(R.id.tvFlexibleFee);
        avaMotor = (TextView)findViewById(R.id.tvavaMotor);
        avaPrivateCar = (TextView)findViewById(R.id.tvavaPrivateCar);
        avaTruck = (TextView)findViewById(R.id.tvavaTruck);
        latitude = (TextView)findViewById(R.id.edLat);
        longitude = (TextView)findViewById(R.id.edLng);

        update = (Button) findViewById(R.id.btUpdate);
        logout = (Button) findViewById(R.id.btLogout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logout();
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        getData();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(takedata.this, ParkFile.class));

            }
        });
    }

    private void getData(){
        if (firebaseAuth != null) {
//            reff1 = FirebaseDatabase.getInstance().getReference().child("Park").child(firebaseAuth.getUid());

            reff = FirebaseDatabase.getInstance().getReference().child("Park").child(firebaseAuth.getUid());
            reff.addChildEventListener(new ChildEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                    Toast.makeText(takedata.this, "Welcome back", Toast.LENGTH_SHORT).show();
//                }

                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    String lat = String.valueOf(dataSnapshot.child("latitude").getValue());
                    String lng = String.valueOf(dataSnapshot.child("longitude").getValue());
                    String minimunCharge = dataSnapshot.child("minimunCharge").getValue().toString();
                    String motor = dataSnapshot.child("motor").getValue().toString();
                    String parkAddress1 = dataSnapshot.child("parkAddress").getValue().toString();
                    String parkName1 = dataSnapshot.child("aaaParkName").getValue().toString();
                    String parkingFee1 = dataSnapshot.child("parkingFee").getValue().toString();
                    String privateCar = dataSnapshot.child("privateCar").getValue().toString();
                    String truck = dataSnapshot.child("truck").getValue().toString();
                    String flexibleFee1 = dataSnapshot.child("flexibleFee").getValue().toString();
                    String avaMotor1 = dataSnapshot.child("avaMotor").getValue().toString();
                    String avaPrivateCar1 = dataSnapshot.child("avaPrivateCar").getValue().toString();
                    String avaTruck1 = dataSnapshot.child("avaTruck").getValue().toString();

                    double dprivate, davaprivate, dflexibleFee1, dparkingFee1, dminimunCharge;
                    dprivate = Double.parseDouble(privateCar);
                    davaprivate = Double.parseDouble(avaPrivateCar1);
                    dflexibleFee1 = Double.parseDouble(flexibleFee1);
                    dparkingFee1 = Double.parseDouble(parkingFee1);
                    dminimunCharge = Double.parseDouble(minimunCharge);
                    if (davaprivate <= 0.2*dprivate && davaprivate > 0.1*dprivate) {
                        dflexibleFee1 = dflexibleFee1 * 2;
                    }else if(davaprivate <= 0.1*dprivate){
                        dflexibleFee1 = dflexibleFee1 * 3;
                    }else{
                    }
                    flexibleFee1 = String.valueOf(dflexibleFee1);
                    parkingFee1 = String.valueOf(dparkingFee1);
                    minimunCharge = String.valueOf(dminimunCharge);

                    flexibleFee.setText("$" +flexibleFee1 + "/hr");
                    avaMotor.setText(avaMotor1);
                    avaPrivateCar.setText(avaPrivateCar1);
                    avaTruck.setText(avaTruck1);
                    minCharge.setText("$" +minimunCharge +"/hr");
                    numberM.setText("motor: " + motor);
                    parkAddress.setText(parkAddress1);
                    parkName.setText(parkName1);
                    parkingFee.setText("$" +parkingFee1 + "/hr");
                    numberP.setText("private car: " + privateCar);
                    numberT.setText("Truck: " + truck);
                    latitude.setText(lat);
                    longitude.setText(lng);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(takedata.this, "fail to connect to the database", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(takedata.this, "Please sign first", Toast.LENGTH_SHORT).show();
        }
    }

    private void Logout() {
        firebaseAuth.signOut();
        finish();
        Toast.makeText(takedata.this, "Logout Successful", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(takedata.this, CarParkLogin.class));
    }

    //toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logoutMenu: {
                Logout();
                break;
            }
            case R.id.mapMenu: {
                startActivity(new Intent(takedata.this, MapActivity.class));
                break;
            }
            case R.id.profileMenu:{
                Toast.makeText(takedata.this, "you are already in profile page",Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.carParkRegMenu:{
                startActivity(new Intent(takedata.this, ParkRegistrationActivity.class));
                break;
            }
            case R.id.refreshMenu:{
                startActivity(new Intent(takedata.this, takedata.class));
                break;
            }
            case R.id.BookingRecord:{
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    startActivity(new Intent(takedata.this, BookingRecord.class));
                }else{
                    Toast.makeText(takedata.this, "Please login first",Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
