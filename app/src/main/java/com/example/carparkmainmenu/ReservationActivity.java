package com.example.carparkmainmenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ReservationActivity extends AppCompatActivity {
    public static final String EXTRA_PARK = "com.example.carparkmainmenu.EXTRA_PARK";
    public static final String EXTRA_PARKADDRESS = "com.example.carparkmainmenu.EXTRA_PARKADDRESS";
    public static final String EXTRA_MOTOR = "com.example.carparkmainmenu.EXTRA_MOTOR";
    public static final String EXTRA_PRIVATECAR = "com.example.carparkmainmenu.EXTRA_PRIVATECAR";
    public static final String EXTRA_TRUCK = "com.example.carparkmainmenu.EXTRA_TRUCK";
    public static final String EXTRA_AVAM = "com.example.carparkmainmenu.EXTRA_AVAM";
    public static final String EXTRA_AVAP = "com.example.carparkmainmenu.EXTRA_AVAP";
    public static final String EXTRA_AVAT = "com.example.carparkmainmenu.EXTRA_AVAT";
    public static final String EXTRA_NP = "com.example.carparkmainmenu.EXTRA_NP";
    public static final String EXTRA_MINC = "com.example.carparkmainmenu.EXTRA_MINC";
    public static final String EXTRA_CP = "com.example.carparkmainmenu.EXTRA_CP";


    ListView listView;
    FirebaseDatabase database;
    DatabaseReference ref;
    ArrayList<String> list, listPark,listParkAddress, listMotor, listPrivateCar, listTruck,
            listAvaM, listAvaP, listAvaT, listNormalPrice, listMinimunCharge, listCurrentPrice;
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

        listPark = new ArrayList<>();
        listParkAddress = new ArrayList<>();
        listMotor = new ArrayList<>();
        listPrivateCar = new ArrayList<>();
        listTruck = new ArrayList<>();
        listAvaM = new ArrayList<>();
        listAvaP = new ArrayList<>();
        listAvaT = new ArrayList<>();
        listNormalPrice = new ArrayList<>();
        listMinimunCharge = new ArrayList<>();
        listCurrentPrice = new ArrayList<>();

        adapter = new ArrayAdapter<String>(ReservationActivity.this, R.layout.park_user_info, R.id.parkUserInfo, list);
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    parkUserProfile = ds.getValue(ParkUserProfile.class);

                    assert parkUserProfile != null;
                    String parkName = parkUserProfile.getAaaParkName();
                    String parkAddress = parkUserProfile.getParkAddress();
                    String motor = parkUserProfile.getMotor();
                    String privateCar = parkUserProfile.getPrivateCar();
                    String truck = parkUserProfile.getTruck();
                    String avaMotor = parkUserProfile.getAvaMotor();
                    String avaPrivateCar1 = parkUserProfile.getAvaPrivateCar();
                    String avaTruck = parkUserProfile.getAvaTruck();
                    String parkingFee1 = parkUserProfile.getParkingFee();
                    String minimunCharge = parkUserProfile.getMinimunCharge();
                    String flexibleFee1 = parkUserProfile.getFlexibleFee();

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

                    assert parkUserProfile != null;
                    list.add(parkName + "\n"
                            + "Current Price: $" + flexibleFee1 + "\n"
                            + "M: " + parkUserProfile.getAvaMotor()
                            + "   P: " + parkUserProfile.getAvaPrivateCar()
                            + "   T: " + parkUserProfile.getAvaTruck()
                    );

                    adapter.notifyDataSetChanged();

                    listPark.add(parkUserProfile.getAaaParkName());
                    listAvaM.add(parkUserProfile.getAvaMotor());
                    listAvaP.add(parkUserProfile.getAvaPrivateCar());
                    listAvaT.add(parkUserProfile.getAvaTruck());
                    listCurrentPrice.add(parkUserProfile.getFlexibleFee());
                    listMinimunCharge.add(parkUserProfile.getMinimunCharge());
                    listMotor.add(parkUserProfile.getMotor());
                    listParkAddress.add(parkUserProfile.getParkAddress());
                    listNormalPrice.add(parkUserProfile.getParkingFee());
                    listPrivateCar.add(parkUserProfile.getPrivateCar());
                    listTruck.add(parkUserProfile.getTruck());

                }
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        String park = listPark.get(position);
                        String parkAddress = listParkAddress.get(position);
                        String motor = listMotor.get(position);
                        String parivateCar = listPrivateCar.get(position);
                        String truck = listTruck.get(position);
                        String avaMotor = listAvaM.get(position);
                        String avaPrivate = listAvaP.get(position);
                        String avaTruck = listAvaT.get(position);
                        String normalPrice = listNormalPrice.get(position);
                        String minimunCharge = listMinimunCharge.get(position);
                        String currentPrice = listCurrentPrice.get(position);

                        Intent intent = new Intent(ReservationActivity.this, BookCarParkActivity.class);
                        intent.putExtra(EXTRA_PARK, park);
                        intent.putExtra(EXTRA_PARKADDRESS, parkAddress);
                        intent.putExtra(EXTRA_MOTOR, motor);
                        intent.putExtra(EXTRA_PRIVATECAR, parivateCar);
                        intent.putExtra(EXTRA_TRUCK, truck);
                        intent.putExtra(EXTRA_AVAM, avaMotor);
                        intent.putExtra(EXTRA_AVAP, avaPrivate);
                        intent.putExtra(EXTRA_AVAT, avaTruck);
                        intent.putExtra(EXTRA_NP, normalPrice);
                        intent.putExtra(EXTRA_MINC, minimunCharge);
                        intent.putExtra(EXTRA_CP, currentPrice);

                        startActivity(intent);
//                        Toast.makeText(ReservationActivity.this, "Park: " + list.get(position), Toast.LENGTH_SHORT).show();
                    }
                });
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
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }
}
