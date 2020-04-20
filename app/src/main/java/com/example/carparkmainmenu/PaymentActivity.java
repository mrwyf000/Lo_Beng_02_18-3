package com.example.carparkmainmenu;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;


public class PaymentActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    private Button btConfirm;
    String park_Name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        btConfirm = (Button) findViewById(R.id.btConfirm);
        setBtConfirm();

    }

    private void setBtConfirm(){
        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUniqueCodeToDriver();
            }
        });
    }


    private void sendUniqueCodeToDriver(){
        Intent intent = getIntent();
        String Park = intent.getStringExtra(BookCarParkActivity.EXTRA_PARK2);
        park_Name = Park;
        Date currentTime = Calendar.getInstance().getTime();


        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null){
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference myRef = firebaseDatabase.getReference().child("Driver")
                    .child("Booking").child(firebaseAuth.getUid()).child("Park");
            ParkBookingRef parkBookingRef = new ParkBookingRef(
                    park_Name, currentTime);
            myRef.setValue(parkBookingRef);
            Toast.makeText(PaymentActivity.this, "Data sent, reservation succeed!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(PaymentActivity.this, BookingRecord.class));

        }else {
            Toast.makeText(this, "please login first", Toast.LENGTH_SHORT).show();
        }
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
            Toast.makeText(PaymentActivity.this, "Logout Successful", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(PaymentActivity.this, CarParkLogin.class));
        }else {
            Toast.makeText(PaymentActivity.this, "You have not login yet", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(PaymentActivity.this, "You are already login", Toast.LENGTH_SHORT).show();

                }else {
                    startActivity(new Intent(PaymentActivity.this, CarParkLogin.class));
                }
                break;
            }
            case R.id.mapMenu: {
                startActivity(new Intent(PaymentActivity.this, MapActivity.class));
                break;
            }
            case R.id.profileMenu:{
                firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    startActivity(new Intent(PaymentActivity.this, takedata.class));
                }else {
                    Toast.makeText(PaymentActivity.this, "You have not login yet", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.carParkRegMenu:{
                startActivity(new Intent(PaymentActivity.this, ParkRegistrationActivity.class));
                break;
            }
            case R.id.refreshMenu:{
                startActivity(new Intent(PaymentActivity.this, PaymentActivity.class));
                break;
            }
            case R.id.BookingRecord:{
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    startActivity(new Intent(PaymentActivity.this, BookingRecord.class));
                }else{
                    Toast.makeText(PaymentActivity.this, "Please login first",Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
