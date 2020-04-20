package com.example.carparkmainmenu;

import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BookingRecord extends AppCompatActivity {
    private static final String TAG = "BookingRecord";

    private TextView tvBookingTime, tvparkName;
    private TextView btCancel;
    private Button btBack;
    FirebaseAuth firebaseAuth;
    DatabaseReference reff;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_record);

        tvBookingTime = (TextView) findViewById(R.id.tvBookingTime);
        tvparkName = (TextView) findViewById(R.id.tvParkName2);
        btCancel = (TextView) findViewById(R.id.btCancel);
        btBack = (Button)findViewById(R.id.btBack);

        showTime();
        cancelBooking();
        back();
    }

    private void back(){
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BookingRecord.this, MapActivity.class));
            }
        });
    }


    private void cancelBooking(){
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });
    }

    //alertDialog popup
    private void showAlertDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Do you really want to cancel the Booking?");
        alert.setMessage("The minimum charge can not be refund.");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseReference drBooking = FirebaseDatabase.getInstance().getReference("Driver")
                        .child("Booking").child(firebaseAuth.getUid());
                drBooking.removeValue();
                Toast.makeText(BookingRecord.this, "data removed!", Toast.LENGTH_SHORT);
                startActivity(new Intent(BookingRecord.this, BookingRecord.class));
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(BookingRecord.this, BookingRecord.class));
            }
        });
        alert.create().show();
    }

    private void showTime(){
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth != null) {

            reff = FirebaseDatabase.getInstance().getReference().child("Driver").child("Booking").child(firebaseAuth.getUid());
            reff.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    String parkName = String.valueOf(dataSnapshot.child("ParkName").getValue());
                    String date = String.valueOf(dataSnapshot.child("Time").child("date").getValue());
                    String hours = String.valueOf(dataSnapshot.child("Time").child("hours").getValue());
                    String minutes = String.valueOf(dataSnapshot.child("Time").child("minutes").getValue());
                    String month = String.valueOf(dataSnapshot.child("Time").child("month").getValue());
                    String seconds = String.valueOf(dataSnapshot.child("Time").child("seconds").getValue());
                    String year = String.valueOf(dataSnapshot.child("Time").child("year").getValue());

                    Double dmonth,dyear,dhours,dminutes, dseconds;
                    dmonth = Double.parseDouble(month);
                    dyear = Double.parseDouble(year);
                    dhours = Double.parseDouble(hours);
                    dminutes = Double.parseDouble(minutes);
                    dseconds = Double.parseDouble(seconds);
                    dmonth = dmonth + 1;
                    dyear = dyear + 1900;

                    int imonth, iyear, ihours;
                    imonth = dmonth.intValue();
                    iyear = dyear.intValue();
                    ihours = dhours.intValue();

                    String shours,sminutes, sseconds;
                    if(dhours < 10){
                        shours = "0" + dhours.intValue();
                    }else{
                        shours = hours;
                    }
                    if(dminutes < 10){
                        sminutes = "0" + dminutes.intValue();
                    }else{
                        sminutes = minutes;
                    }
                    if(dseconds < 10){
                        sseconds = "0" + dseconds.intValue();
                    }else{
                        sseconds = seconds;
                    }

                    tvparkName.setText(parkName);
                    tvBookingTime.setText(date + "/" + imonth + "/" + iyear + " \n" + shours + ":" + sminutes + ":" + sseconds);
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

                }
            });
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
            Toast.makeText(BookingRecord.this, "Logout Successful", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(BookingRecord.this, "You have not login yet", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(BookingRecord.this, "You are already login", Toast.LENGTH_SHORT).show();

                }else {
                    startActivity(new Intent(BookingRecord.this, CarParkLogin.class));
                }
                break;
            }
            case R.id.mapMenu: {
                startActivity(new Intent(BookingRecord.this, MapActivity.class));
                break;
            }
            case R.id.profileMenu:{
                firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    startActivity(new Intent(BookingRecord.this, takedata.class));
                }else {
                    Toast.makeText(BookingRecord.this, "You have not login yet", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.carParkRegMenu:{
                startActivity(new Intent(BookingRecord.this, ParkRegistrationActivity.class));
                break;
            }
            case R.id.refreshMenu:
            case R.id.BookingRecord:{
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    startActivity(new Intent(BookingRecord.this, BookingRecord.class));
                }else{
                    Toast.makeText(BookingRecord.this, "Please login first",Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
