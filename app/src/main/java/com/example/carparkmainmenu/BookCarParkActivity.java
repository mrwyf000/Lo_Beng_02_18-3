package com.example.carparkmainmenu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class BookCarParkActivity extends AppCompatActivity {
    public static final String EXTRA_PARK2 = "com.example.carparkmainmenu.EXTRA_PARK2";

    private static final String TAG = "BookCarParkActivity";

    FirebaseAuth firebaseAuth;

    private Button btBack,btBook;
    private TextView parkName,parkAddress, numberM, numberP, numberT, parkingFee, minCharge,
            flexibleFee, avaMotor, avaPrivateCar, avaTruck, latitude, longitude, parkInfo, totalSlot, avaSlot;
    private TextView empty, empty1, empty2, empty3, empty4;

    String Park;

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
        Park = park.toString();

        String parkAddress1 = intent.getStringExtra(ReservationActivity.EXTRA_PARKADDRESS);
        parkAddress.setText("Car Park Address \n" + parkAddress1);
        String motor = intent.getStringExtra(ReservationActivity.EXTRA_MOTOR);
        numberM.setText("Motor:                                  " +motor);
        String parivateCar = intent.getStringExtra(ReservationActivity.EXTRA_PRIVATECAR);
        numberP.setText("Private Car:                         " +parivateCar);
        String truck = intent.getStringExtra(ReservationActivity.EXTRA_TRUCK);
        numberT.setText("Truck:                                   " + truck);
        String avaMotor1 = intent.getStringExtra(ReservationActivity.EXTRA_AVAM);
        avaMotor.setText("Motor:                                  " + avaMotor1);
        String avaPrivate = intent.getStringExtra(ReservationActivity.EXTRA_AVAP);
        avaPrivateCar.setText("Private Car:                         " + avaPrivate);
        String avaTruck1 = intent.getStringExtra(ReservationActivity.EXTRA_AVAT);
        avaTruck.setText("Truck:                                   " + avaTruck1);
        String normalPrice = intent.getStringExtra(ReservationActivity.EXTRA_NP);
        parkingFee.setText("Normal Parking Fee:         $" + normalPrice);
        String minimunCharge = intent.getStringExtra(ReservationActivity.EXTRA_MINC);
        minCharge.setText("Minimun Charge:               $" + minimunCharge);
        String currentPrice = intent.getStringExtra(ReservationActivity.EXTRA_CP);
        flexibleFee.setText("Current Parking Fee:         $" + currentPrice);


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
                Intent intent = new Intent(BookCarParkActivity.this, PaymentActivity.class);
                intent.putExtra(EXTRA_PARK2, Park);
                startActivity(intent);
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
            Toast.makeText(BookCarParkActivity.this, "Logout Successful", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(BookCarParkActivity.this, "You have not login yet", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(BookCarParkActivity.this, "You are already login", Toast.LENGTH_SHORT).show();

                }else {
                    startActivity(new Intent(BookCarParkActivity.this, CarParkLogin.class));
                }
                break;
            }
            case R.id.mapMenu: {
                startActivity(new Intent(BookCarParkActivity.this, MapActivity.class));
                break;
            }
            case R.id.profileMenu:{
                firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    startActivity(new Intent(BookCarParkActivity.this, takedata.class));
                }else {
                    Toast.makeText(BookCarParkActivity.this, "You have not login yet", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.carParkRegMenu:{
                startActivity(new Intent(BookCarParkActivity.this, ParkRegistrationActivity.class));
                break;
            }
            case R.id.refreshMenu:{
                startActivity(new Intent(BookCarParkActivity.this, BookCarParkActivity.class));
                break;
            }
            case R.id.BookingRecord:{
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    startActivity(new Intent(BookCarParkActivity.this, BookingRecord.class));
                }else{
                    Toast.makeText(BookCarParkActivity.this, "Please login first",Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
