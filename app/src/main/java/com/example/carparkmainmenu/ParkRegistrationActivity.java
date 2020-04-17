package com.example.carparkmainmenu;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class ParkRegistrationActivity extends AppCompatActivity {

    //set the password pattern, the password must be at least...
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=\\S+$)" +           //no white spaces
                    ".{8,}" +               //at least 8 characters
                    "$");

    private EditText parkName, userEmail, userPassword, userPassword2;
    private Button regButton, backButton, checkEmail;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    String park_name, park_address, motor_Car, private_Car, truck_Car, parking_Fee, halfhr, onehr, twohr, flexible_Pricing, minimun_Charge, flexible_Fee;
    String park_Name, email, password, password2;



    //set up
    private void setupUIViews() {
        parkName = (EditText) findViewById(R.id.edParkName);
        userEmail = (EditText) findViewById(R.id.edUserEmail);
        userPassword = (EditText) findViewById(R.id.edUserPassword);
        userPassword2 = (EditText) findViewById(R.id.edUserPassword2);
        regButton = (Button) findViewById(R.id.btRegister);
        backButton = (Button) findViewById(R.id.btBack);
        checkEmail = (Button) findViewById(R.id.btCheckEmail);

    }

    //check is/are there any empty and password pattern
    private boolean validate() {
        boolean result = false;

        park_Name = parkName.getText().toString();
        email = userEmail.getText().toString();
        password = userPassword.getText().toString();
        password2 = userPassword2.getText().toString();


        if (park_Name.isEmpty() || password.isEmpty() || email.isEmpty() || password2.isEmpty()) {
            Toast.makeText(this, "please enter all the details", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            Toast.makeText(this, "Password too weak", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!password2.equals(password)) {
            Toast.makeText(this, "cannot confirm the password", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            result = true;
        }

        return result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_park_registration);
        setupUIViews();

        //go back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ParkRegistrationActivity.this, CarParkLogin.class));
            }
        });

        //check is the email usable when the email is not empty
        checkEmail();

        //create account
        registration();
    }

    private void registration(){
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    firebaseAuth = FirebaseAuth.getInstance();
                    FirebaseUser user = firebaseAuth.getCurrentUser();

                    //Upload data to the database
                    String user_email = userEmail.getText().toString().trim();
                    String user_password = userPassword.getText().toString().trim();
                    String park_name = "";

                    firebaseAuth.createUserWithEmailAndPassword(user_email, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                sendUserData();
                                sendEmailVerification();
                            } else {
                                Toast.makeText(ParkRegistrationActivity.this,
                                        "Registration Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private void checkEmail(){
        checkEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userEmail.getText().toString().isEmpty()) {
                    Toast.makeText(ParkRegistrationActivity.this,
                            "Please enter Email", Toast.LENGTH_SHORT).show();
                }else   {
                    checkEmail(userEmail.getText().toString());
                }
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
            Toast.makeText(ParkRegistrationActivity.this, "Logout Successful", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ParkRegistrationActivity.this, CarParkLogin.class));
        }else {
            Toast.makeText(ParkRegistrationActivity.this, "You have not login yet", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(ParkRegistrationActivity.this, "You are already login", Toast.LENGTH_SHORT).show();

                }else {
                    startActivity(new Intent(ParkRegistrationActivity.this, CarParkLogin.class));
                }
                break;
            }
            case R.id.mapMenu: {
                startActivity(new Intent(ParkRegistrationActivity.this, MapActivity.class));
                break;
            }
            case R.id.profileMenu:{
                firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    startActivity(new Intent(ParkRegistrationActivity.this, takedata.class));
                }else {
                    Toast.makeText(ParkRegistrationActivity.this, "You have not login yet", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.carParkRegMenu:
            case R.id.refreshMenu: {
                startActivity(new Intent(ParkRegistrationActivity.this, ParkRegistrationActivity.class));
                break;
            }
            case R.id.BookingRecord:{
                startActivity(new Intent(ParkRegistrationActivity.this, BookingRecord.class));
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }




    //check the email is registered or not
    public void checkEmail(String email) {
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        firebaseAuth.fetchProvidersForEmail(email).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<ProviderQueryResult> task) {

                Boolean check = !task.getResult().getProviders().isEmpty();

                if (!check) {
                    Toast.makeText(getApplicationContext(), "You can use this Email", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Email already present", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //send email verification to the user when they are successfully registered by email
    private void sendEmailVerification()    {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser!=null) {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ParkRegistrationActivity.this,
                                "Successfully Registered, Verification mail sent!", Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(ParkRegistrationActivity.this, CarParkLogin.class));
                    }else   {
                        Toast.makeText(ParkRegistrationActivity.this,
                                "Registration fail", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void sendUserData() {
        String park_name = park_Name;
        String park_address = "0";
        String motor_Car = "0";
        String private_Car = "0";
        String truck_Car = "0";
        String parking_Fee = "0";
        String flexible_Fee = "0";
        String minimun_Charge = "0";
        String ava_Motor = "0";
        String ava_Private_Car = "0";
        String ava_Truck = "0";
        String lat_latitude = "0";
        String lng_longitude = "0";

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference().child("Park")
                .child(firebaseAuth.getUid()).child(park_name + "1");
        ParkUserProfile parkUserProfile = new ParkUserProfile(
                park_name, park_address, motor_Car, private_Car, truck_Car,
                parking_Fee, flexible_Fee, minimun_Charge,
                ava_Motor, ava_Private_Car, ava_Truck, lat_latitude, lng_longitude);
        myRef.setValue(parkUserProfile);
    }
}
