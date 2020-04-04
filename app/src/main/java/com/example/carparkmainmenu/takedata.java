package com.example.carparkmainmenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.snapshot.DoubleNode;

public class takedata extends AppCompatActivity {
    private TextView parkName,parkAddress, numberM, numberP, numberT, parkingFee, minCharge,
            flexibleFee, avaMotor, avaPrivateCar, avaTruck, latitude, longitude;
    private Button update, logout;
    private DatabaseReference reff, myDatabaseRef;
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
            reff = FirebaseDatabase.getInstance().getReference().child("Park").child(firebaseAuth.getUid());
            reff.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

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

                    flexibleFee.setText("$" +flexibleFee1);
                    avaMotor.setText(avaMotor1);
                    avaPrivateCar.setText(avaPrivateCar1);
                    avaTruck.setText(avaTruck1);
                    minCharge.setText("$" +minimunCharge);
                    numberM.setText("motor: " + motor);
                    parkAddress.setText(parkAddress1);
                    parkName.setText(parkName1);
                    parkingFee.setText("$" +parkingFee1);
                    numberP.setText("private car: " + privateCar);
                    numberT.setText("Truck: " + truck);
                    latitude.setText(lat);
                    longitude.setText(lng);

                    Toast.makeText(takedata.this, "Welcome back", Toast.LENGTH_SHORT).show();
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
}
