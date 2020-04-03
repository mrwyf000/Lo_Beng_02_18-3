package com.example.carparkmainmenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReservationActivity extends AppCompatActivity {

    ListView listView;
    FirebaseDatabase database;
    DatabaseReference ref;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;
    ParkUserProfile parkUserProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        parkUserProfile = new ParkUserProfile();
        listView = (ListView) findViewById(R.id.listView);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Park");
        list = new ArrayList<>();
        adapter = new ArrayAdapter<String>(ReservationActivity.this, R.layout.park_user_info, R.id.parkUserInfo, list);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){

                    parkUserProfile = ds.getValue(ParkUserProfile.class);
                    assert parkUserProfile != null;
                    list.add(parkUserProfile.getAaaParkName()
                            + "   " + parkUserProfile.getFlexibleFee() + "\n"
                            + "M: " + parkUserProfile.getAvaMotor()
                            + "   P: " + parkUserProfile.getAvaPrivateCar()
                            + "   T: " + parkUserProfile.getAvaTruck()
                    );
                    Log.i("here", list.toString());

                    adapter.notifyDataSetChanged();

                }

                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
}
