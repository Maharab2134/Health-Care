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
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class LabTestDetailsActivity extends AppCompatActivity {
    private String[][] packages = {};
    private HashMap<String, String> item;
    private ArrayList list;
    private SimpleAdapter sa;
    private ListView lst;
    TextView textViewODTitle;

    private Button btnBack, btnAddToCart, btnGetLocation;
    private TextView tvPackageName, tvTotalCost, tvCurrentLocation;
    private FirebaseHelper firebaseHelper;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_test_details);

        btnAddToCart = findViewById(R.id.buttonLTDAddToCart);
        btnBack = findViewById(R.id.buttonLTDGoBack);
        tvPackageName = findViewById(R.id.textViewLTDTitle);
        tvTotalCost = findViewById(R.id.textViewLTDTotalCost);
        textViewODTitle = findViewById(R.id.textViewODTitle);
        lst = findViewById(R.id.listViewLTD);
        firebaseHelper = new FirebaseHelper(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        Intent it = getIntent();
        String packageName = it.getStringExtra("text1");
        String packageDetails = it.getStringExtra("text2");
        String price = it.getStringExtra("text3");

        tvPackageName.setText(packageName);
        tvTotalCost.setText(String.format("Total Cost: ৳%.2f", parsePrice(price)));

        btnBack.setOnClickListener(view -> startActivity(new Intent(LabTestDetailsActivity.this, LabTestActivity.class)));
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
                                textViewODTitle.setText(district);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });



        btnAddToCart.setOnClickListener(view -> {
            SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
            String username = sharedPreferences.getString("username", "");

            try {
                float priceValue = parsePrice(price);
                firebaseHelper.addCart(username, packageName, priceValue, "lab");
                Toast.makeText(getApplicationContext(), "Added to Cart", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LabTestDetailsActivity.this, LabTestActivity.class));
            } catch (NumberFormatException e) {
                Toast.makeText(getApplicationContext(), "Error parsing price: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        String[] details = packageDetails.split("\n");
        packages = new String[details.length][];
        for (int i = 0; i < packages.length; i++) {
            packages[i] = new String[5];
        }

        for (int i = 0; i < details.length; i++) {
            packages[i][0] = details[i];
        }

        list = new ArrayList();
        for (int i = 0; i < packages.length; i++) {
            item = new HashMap<>();
            item.put("line1", packages[i][0]);
            item.put("line2", packages[i][1]);
            item.put("line3", packages[i][2]);
            item.put("line4", packages[i][3]);
            item.put("line5", packages[i][4]);
            list.add(item);
        }

        sa = new SimpleAdapter(this, list, R.layout.multi_lines_lab,
                new String[]{"line1", "line2", "line3", "line4", "line5"},
                new int[]{R.id.line_a, R.id.line_b, R.id.line_c, R.id.line_d, R.id.line_e});
        lst.setAdapter(sa);
    }


    private float parsePrice(String priceStr) {
        // Remove the ৳ symbol and any whitespace
        String cleanPrice = priceStr.replace("৳", "").trim();
        return Float.parseFloat(cleanPrice);
    }
}