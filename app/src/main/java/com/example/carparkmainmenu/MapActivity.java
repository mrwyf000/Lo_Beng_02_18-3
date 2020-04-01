package com.example.carparkmainmenu;

import com.example.carparkmainmenu.ui.map.ClusterMarker;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.GeoPoint;
import com.google.maps.android.clustering.ClusterManager;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This shows how to create a simple activity with a raw MapView and add a marker to it. This
 * requires forwarding all the important lifecycle methods onto MapView.
 */
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = "MapActivity";
    private MapView mMapView;

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

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

    }


    //read the database and take the data to String
    //make marker to the map
    private Marker mTinHeng, mTinChak, mFortunKingswood;
    private static final LatLng TinHeng = new LatLng(22.4698, 114.0002);
    private static final LatLng TinChak = new LatLng(22.4684, 113.9987);
    private static final LatLng FortuneKingswood = new LatLng(22.4570, 114.0052);


    private void addMarkersToMap(){

        reff = FirebaseDatabase.getInstance().getReference().child("Park").child(firebaseAuth.getUid());
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String flexiblePriceFee = dataSnapshot.child("flexiblePriceFee").getValue().toString();
                String minimunCharge = dataSnapshot.child("minimunCharge").getValue().toString();
                String motor = dataSnapshot.child("motor").getValue().toString();
                String parkAddress1 = dataSnapshot.child("parkAddress").getValue().toString();
                String parkName1 = dataSnapshot.child("aaaParkName").getValue().toString();
                String parkingFee1 = dataSnapshot.child("parkingFee").getValue().toString();
                String privateCar = dataSnapshot.child("privateCar").getValue().toString();
                String truck = dataSnapshot.child("truck").getValue().toString();

                title = parkName1;
                snippet = "Available parking slot: " + "\n" +
                        "Motor: " + motor + "\n" +
                        "Private car: " + privateCar + "\n" +
                        "truck: " + truck + "\n" +
                        "Price: $" + parkingFee1;

                mTinHeng = mGoogleMap.addMarker(new MarkerOptions()
                        .position(TinHeng)
                        .title(parkName1)
                        .snippet(snippet)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

                mTinChak = mGoogleMap.addMarker(new MarkerOptions()
                        .position(TinChak)
                        .title("Tin Chak Car Park")
                        .snippet("222")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

                mFortunKingswood = mGoogleMap.addMarker(new MarkerOptions()
                        .position(FortuneKingswood)
                        .title("FortuneKingswood Car Park")
                        .snippet("333")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

                //custom map marker info window
                if (mGoogleMap != null){
                    mGoogleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                        @Override
                        public View getInfoWindow(Marker marker) {
                            return null;
                        }

                        @Override
                        public View getInfoContents(Marker marker) {

                            View row = getLayoutInflater().inflate(R.layout.custom_info_window,null);
                            TextView tvtitle = (TextView)row.findViewById(R.id.title1);
                            TextView tvsnippet = (TextView)row.findViewById(R.id.snippet);

                            tvsnippet.setText(snippet);
                            tvtitle.setText(title);



                            return row;
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }





    //move the camera function
    private void moveCamera(LatLng latLng, float zoomLevel, String title){
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
        mGoogleMap.addMarker(new MarkerOptions().position(latLng).title(title));
        hideKeyboard(MapActivity.this);

        mGoogleMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapActivity.this));

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
            //Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();

            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), zoomLevel, address.getAddressLine(0));


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
                    String title;
                    LatLng latLng = new LatLng(currentlocation.getLatitude(), currentlocation.getLongitude());

                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
                    Log.d(TAG, "latLng: " + latLng.toString());
                    Toast.makeText(MapActivity.this, "center your location", Toast.LENGTH_SHORT).show();
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
        addMarkersToMap();

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