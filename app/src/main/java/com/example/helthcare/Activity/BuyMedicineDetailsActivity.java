package com.example.helthcare.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.helthcare.Database.FirebaseHelper;
import com.example.helthcare.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class BuyMedicineDetailsActivity extends AppCompatActivity {
    TextView tvPackageName, tvTotalCost, textViewODTitle2;
    EditText edDetails;
    Button btnBack, btnAddToCart;
    private FirebaseHelper firebaseHelper;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_medicine_details);

        tvPackageName = findViewById(R.id.textViewBMDTitle);
        tvTotalCost = findViewById(R.id.textViewBMDTotalCost);
        edDetails = findViewById(R.id.editTextBMDTextMultiLine);
        btnBack = findViewById(R.id.buttonBMDBack);
        textViewODTitle2 = findViewById(R.id.textViewODTitle2);
        btnAddToCart = findViewById(R.id.buttonBMDAddToCart);
        firebaseHelper = new FirebaseHelper(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        Intent intent = getIntent();
        tvPackageName.setText(intent.getStringExtra("text1"));
        edDetails.setText(intent.getStringExtra("text2"));
        tvTotalCost.setText("Total Cost : " + intent.getStringExtra("text7") + "/-");

        btnBack.setOnClickListener(v -> startActivity(new Intent(BuyMedicineDetailsActivity.this, BuyMedicineActivity.class)));

        btnAddToCart.setOnClickListener(view -> {
            String price = tvTotalCost.getText().toString();
            price = price.replace("Total Cost : ", "")
                        .replace("/-", "")
                        .replace("৳", "")
                        .trim();
            float totalAmount = Float.parseFloat(price);
            
            SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
            String username = sharedPreferences.getString("username", "");
            String product = tvPackageName.getText().toString();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                return;
            }


            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                            try {
                                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                if (!addresses.isEmpty()) {
                                    String district = addresses.get(0).getSubAdminArea();
                                    textViewODTitle2.setText(district);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            firebaseHelper.checkCart(username, product, task -> {
                if (task.isSuccessful()) {
                    if (task.getResult().isEmpty()) {
                        firebaseHelper.addCart(username, product, totalAmount, "medicine");
                        Toast.makeText(getApplicationContext(), "Added to Cart", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(BuyMedicineDetailsActivity.this, BuyMedicineActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "Product Already Added...", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Error checking cart: " + task.getException().getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
