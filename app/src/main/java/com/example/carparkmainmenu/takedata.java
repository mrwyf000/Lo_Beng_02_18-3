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

public class takedata extends AppCompatActivity {
    private TextView parkName,parkAddress, numberM, numberP, numberT, parkingFee, minCharge, flexiblePricing;
    private Button update, logout;
    private DatabaseReference reff, myDatabaseRef;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_takedata);

        parkName = (TextView)findViewById(R.id.edParkName);
        parkAddress = (TextView)findViewById(R.id.edCarParkAddress);
        numberM = (TextView)findViewById(R.id.edMotorcycle);
        numberP = (TextView)findViewById(R.id.edPrivateCar);
        numberT = (TextView) findViewById(R.id.edTruck);
        parkingFee = (TextView) findViewById(R.id.edParkingFee);
        minCharge = (TextView) findViewById(R.id.edMinimunCharge);
        flexiblePricing = (TextView) findViewById(R.id.edFlexiblePricingFee);
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
        reff = FirebaseDatabase.getInstance().getReference().child("Park").child(firebaseAuth.getUid());
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String flexiblePriceFee = dataSnapshot.child("flexiblePriceFee").getValue().toString();
                String minimunCharge = dataSnapshot.child("minimunCharge").getValue().toString();
                String motor = dataSnapshot.child("motor").getValue().toString();
                String parkAddress1 = dataSnapshot.child("parkAddress").getValue().toString();
                String parkName1 = dataSnapshot.child("parkName").getValue().toString();
                String parkingFee1 = dataSnapshot.child("parkingFee").getValue().toString();
                String privateCar = dataSnapshot.child("privateCar").getValue().toString();
                String truck = dataSnapshot.child("truck").getValue().toString();

                flexiblePricing.setText(flexiblePriceFee);
                minCharge.setText(minimunCharge);
                numberM.setText("motor: " + motor);
                parkAddress.setText(parkAddress1);
                parkName.setText(parkName1);
                parkingFee.setText(parkingFee1);
                numberP.setText("private car:" + privateCar);
                numberT.setText("Truck: " + truck);

                Toast.makeText(takedata.this, "Welcome back", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(takedata.this, "fail to connect to the database", Toast.LENGTH_SHORT).show();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(takedata.this, ParkFile.class));

            }
        });

    }
    private void Logout() {
        firebaseAuth.signOut();
        finish();
        Toast.makeText(takedata.this, "Logout Successful", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(takedata.this, CarParkLogin.class));
    }
}
