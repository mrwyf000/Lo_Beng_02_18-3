package com.example.carparkmainmenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class BookCarParkActivity extends AppCompatActivity {
    private static final String TAG = "BookCarParkActivity";

    private Button btBack,btBook;
    private TextView parkName,parkAddress, numberM, numberP, numberT, parkingFee, minCharge,
            flexibleFee, avaMotor, avaPrivateCar, avaTruck, latitude, longitude, parkInfo, totalSlot, avaSlot;
    private TextView empty, empty1, empty2, empty3, empty4;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_car_park);
        Intent intent = getIntent();

        parkName = (TextView) findViewById(R.id.tvParkName1);
        parkAddress = (TextView) findViewById(R.id.tvParkAddress1);
        numberM = (TextView) findViewById(R.id.tvMotor1);
        numberP = (TextView) findViewById(R.id.tvPrivateCar1);
        numberT = (TextView) findViewById(R.id.tvTruck1);
        parkingFee = (TextView) findViewById(R.id.tvNormalPrice);
        minCharge = (TextView) findViewById(R.id.tvMinCharge1);
        flexibleFee = (TextView) findViewById(R.id.tvCurrentPrice1);
        avaMotor = (TextView) findViewById(R.id.tvavaMotor1);
        avaPrivateCar = (TextView) findViewById(R.id.tvavaPrivateCar1);
        avaTruck = (TextView) findViewById(R.id.tvavaTruck1);

        parkInfo = (TextView) findViewById(R.id.tvinfo);
        totalSlot = (TextView) findViewById(R.id.tvTotal1);
        avaSlot = (TextView) findViewById(R.id.tvAva1);

        parkInfo.setText("Car Park Information");
        totalSlot.setText("Total Parking Slot");
        avaSlot.setText("Available Parking Slot");

        empty = (TextView) findViewById(R.id.tvempty);
        empty1 = (TextView) findViewById(R.id.tvempty1);
        empty2 = (TextView) findViewById(R.id.tvempty2);
        empty3 = (TextView) findViewById(R.id.tvempty3);
        empty4 = (TextView) findViewById(R.id.tvempty4);

        String park = intent.getStringExtra(ReservationActivity.EXTRA_PARK);
        parkName.setText("Park Name \n" + park);
        String parkAddress1 = intent.getStringExtra(ReservationActivity.EXTRA_PARKADDRESS);
        parkAddress.setText("Car Park Address \n" + parkAddress1);
        String motor = intent.getStringExtra(ReservationActivity.EXTRA_MOTOR);
        numberM.setText("Motor:                             " +motor);
        String parivateCar = intent.getStringExtra(ReservationActivity.EXTRA_PRIVATECAR);
        numberP.setText("Private Car:                    " +parivateCar);
        String truck = intent.getStringExtra(ReservationActivity.EXTRA_TRUCK);
        numberT.setText("Truck:                              " + truck);
        String avaMotor1 = intent.getStringExtra(ReservationActivity.EXTRA_AVAM);
        avaMotor.setText("Motor:                             " + avaMotor1);
        String avaPrivate = intent.getStringExtra(ReservationActivity.EXTRA_AVAP);
        avaPrivateCar.setText("Private Car:                    " + avaPrivate);
        String avaTruck1 = intent.getStringExtra(ReservationActivity.EXTRA_AVAT);
        avaTruck.setText("Truck:                              " + avaTruck1);
        String normalPrice = intent.getStringExtra(ReservationActivity.EXTRA_NP);
        parkingFee.setText("Normal Parking Fee:    $" + normalPrice);
        String minimunCharge = intent.getStringExtra(ReservationActivity.EXTRA_MINC);
        minCharge.setText("Minimun Charge:          $" + minimunCharge);
        String currentPrice = intent.getStringExtra(ReservationActivity.EXTRA_CP);
        flexibleFee.setText("Current Parking Fee:    $" + currentPrice);


        btBack = (Button) findViewById(R.id.btBack);
        btBook = (Button) findViewById(R.id.btBook);

        back();
        book();

    }


    private void back(){
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BookCarParkActivity.this, ReservationActivity.class));
            }
        });
    }

    private void book(){
        btBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BookCarParkActivity.this, PaymentActivity.class));
            }
        });
    }
}
