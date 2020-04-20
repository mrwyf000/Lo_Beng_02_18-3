package com.example.carparkmainmenu;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.carparkmainmenu.ui.map.ClusterMarker;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.maps.android.clustering.ClusterManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This shows how to create a simple activity with a raw MapView and add a marker to it. This
 * requires forwarding all the important lifecycle methods onto MapView.
 */
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = "MapActivity";
    private MapView mMapView;


    private Button btBook;

    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private GoogleMap mGoogleMap;
    private LatLngBounds mMapBoundary;
    private UserLocation mUserPosition;
    private FusedLocationProviderClient mFusedLocationClient;
    private LatLng latLng, userlocation;
    private MyClusterManagerRenderer myClusterManagerRenderer;

    private ClusterManager<ClusterMarker> mClusterManager;
    private ArrayList<ClusterMarker> mClusterMarkers = new ArrayList<>();


    float zoomLevel = 16.0f; //This goes up to 21
    private static final float DEFAULT_ZOOM = 15f;
    private ImageView mGps;
    private Place mplace;

    //take data from firebase
    private DatabaseReference reff;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    String title, snippet;

    //widgets
    private EditText mSearchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_fragment);

        btBook = (Button)findViewById(R.id.btBook);

        mGps = (ImageView) findViewById(R.id.ic_gps);
        mSearchText = (EditText) findViewById(R.id.input_search);

        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mMapView = (MapView) findViewById(R.id.user_list_map);
        mMapView.onCreate(mapViewBundle);

        mMapView.getMapAsync(this);
        init();

        btBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapActivity.this, ReservationActivity.class));
            }
        });

        parkList();

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
            Toast.makeText(MapActivity.this, "Logout Successful", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(MapActivity.this, "You have not login yet", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(MapActivity.this, "You are already login", Toast.LENGTH_SHORT).show();

                }else {
                    startActivity(new Intent(MapActivity.this, CarParkLogin.class));
                }
                break;
            }
            case R.id.mapMenu: {
                Toast.makeText(MapActivity.this, "You are already in MapActivity", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.profileMenu:{
                firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    startActivity(new Intent(MapActivity.this, takedata.class));
                }else {
                    Toast.makeText(MapActivity.this, "You have not login yet", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.carParkRegMenu:{
                startActivity(new Intent(MapActivity.this, ParkRegistrationActivity.class));
                break;
            }
            case R.id.refreshMenu:{
                startActivity(new Intent(MapActivity.this, MapActivity.class));
                break;
            }
            case R.id.BookingRecord:{
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    startActivity(new Intent(MapActivity.this, BookingRecord.class));
                }else{
                    Toast.makeText(MapActivity.this, "Please login first",Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    //adapter for custer the info-window size
    private Marker mMarker;
    private void parkList(){

        reff = FirebaseDatabase.getInstance().getReference().child("Park");
        reff.addChildEventListener(new ChildEventListener() {

//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//
//            }

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    String lat = String.valueOf(dataSnapshot1.child("latitude").getValue());
                    String lng = String.valueOf(dataSnapshot1.child("longitude").getValue());
                    String minimunCharge = dataSnapshot1.child("minimunCharge").getValue().toString();
                    String motor = dataSnapshot1.child("motor").getValue().toString();
                    String parkAddress1 = dataSnapshot1.child("parkAddress").getValue().toString();
                    String aaaParkName = dataSnapshot1.child("aaaParkName").getValue().toString();
                    String parkingFee1 = dataSnapshot1.child("parkingFee").getValue().toString();
                    String privateCar = dataSnapshot1.child("privateCar").getValue().toString();
                    String truck = dataSnapshot1.child("truck").getValue().toString();
                    String flexibleFee1 = dataSnapshot1.child("flexibleFee").getValue().toString();
                    String avaMotor1 = dataSnapshot1.child("avaMotor").getValue().toString();
                    String avaPrivateCar1 = dataSnapshot1.child("avaPrivateCar").getValue().toString();
                    String avaTruck1 = dataSnapshot1.child("avaTruck").getValue().toString();

                    LatLng latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));

                    double dprivate, davaprivate, dflexibleFee1, dparkingFee1,dminimunCharge;
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

                    String snippet = aaaParkName + "\n" +
                            "Available parking slot: " + "\n" +
                            "Motor: " + avaMotor1 + "\n" +
                            "Private car: " + avaPrivateCar1 + "\n" +
                            "Truck: " + avaTruck1 + "\n" +
                            "Normal Price: $" + parkingFee1 + "/hr \n" +
                            "Current Price: $" + flexibleFee1 + "/hr";

                    mMarker = mGoogleMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(title)
                            .snippet(snippet)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                    if (mGoogleMap != null) {
                        mGoogleMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapActivity.this));
                    }
                }
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



    //read the database and take the data to String
    //set car park UID for take data
    private String parkID, fdtinHeng, fdtinChak, fdtinYan, fdfortuneKingswood;


//    //make marker to the map
////    private Marker mTinHeng, mTinChak, mMarker;
////    private static final LatLng lngTinHeng = new LatLng(22.4698, 114.0002);
////    private static final LatLng lngTinChak = new LatLng(22.4684, 113.9987);
////    private static final LatLng lngFortuneKingswood = new LatLng(22.4570, 114.0052);
////
////
////    private void addMarkersToMap() {
////        //set the UID of the car park operator
////        String fdtinHeng = "3hLZBaPJP0R736AIwgONmz85Aqo2";
////        String fdtinChak = "OEgCxUXsT1Ta9HtjYJ1aHVa0mk13";
////        String fdtinYan =  "TUn3QNNacEUzyfEKQPrgPnj1Hkt1";
////
////        infowindow(fdtinHeng);
////        infowindow(fdtinChak);
////        infowindow(fdtinYan);
////
////    }



    //move the camera function
    private void moveCamera(LatLng latLng, float zoomLevel, String title){
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
        hideKeyboard(MapActivity.this);
    }

    private void init(){
        Log.d(TAG, "init: initializing");

        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        ||keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        ||keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){
                    //execute our method for searching
                    geoLocate();
                }
                return false;
            }
        });

        //locate the users location, and move the camera to the users location, gps icon
        mGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked gps icon");
                getDeviceLocation();
                mSearchText.setText("");
                hideKeyboard(MapActivity.this);
            }
        });

    }



    private void geoLocate(){
        Log.d(TAG, "geoLocate: geolocating");

        String searchString = mSearchText.getText().toString();
        Geocoder geocoder = new Geocoder(MapActivity.this);
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(searchString,1);
        }catch (IOException e){
            Log.d(TAG, "geoLocate: IOException: " + e.getMessage());
        }
        if (list.size() > 0){
            Address address = list.get(0);
            Log.d(TAG, "geoLocate found a location: " + address.toString());

            latLng = new LatLng(address.getLatitude(), address.getLongitude());
            moveCamera(latLng, zoomLevel, address.getAddressLine(0));
            mGoogleMap.addMarker(new MarkerOptions().position(latLng).snippet(address.getFeatureName()));


        }else{
            moveCamera(latLng, zoomLevel, title);
        }
    }

    //get user current location
    private void getDeviceLocation(){
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {

            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Log.d(TAG, "running");
                if (task.isSuccessful()) {
                    Location currentlocation = task.getResult();
                    assert currentlocation != null;
                    float zoomLevel = 16.0f; //This goes up to 21
                    String title = null;
                    LatLng latLng = new LatLng(currentlocation.getLatitude(), currentlocation.getLongitude());

                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
                    Log.d(TAG, "latLng: " + latLng.toString());
                    Toast.makeText(MapActivity.this, "center your location", Toast.LENGTH_SHORT).show();

                    moveCamera(latLng, zoomLevel, null);

                }
            }
        });
    }


    //hide the key board function
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.setMyLocationEnabled(true);
        mGoogleMap = map;
        getDeviceLocation();
//        addMarkersToMap();



    }

    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this, "Info window clicked",
                Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}