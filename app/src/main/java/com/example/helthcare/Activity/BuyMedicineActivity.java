package com.example.helthcare.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.helthcare.HomeActivity;
import com.example.helthcare.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class BuyMedicineActivity extends AppCompatActivity {

    private String[][] medicines = {
            {"Medicine : Napa 500mg", "Hospital : Square Pharmaceuticals", "৳ 2.00", "Tablet", "Paracetamol 500mg"},
            {"Medicine : Napa Extra", "Hospital : Square Pharmaceuticals", "৳ 3.00", "Tablet", "Paracetamol 500mg + Caffeine 65mg"},
            {"Medicine : Napa Rapid", "Hospital : Square Pharmaceuticals", "৳ 4.00", "Tablet", "Paracetamol 500mg"},
            {"Medicine : Napa Suspension", "Hospital : Square Pharmaceuticals", "৳ 45.00", "Suspension", "Paracetamol 120mg/5ml"},
            {"Medicine : Napa Suppository", "Hospital : Square Pharmaceuticals", "৳ 15.00", "Suppository", "Paracetamol 125mg"},
            {"Medicine : Napa Plus", "Hospital : Square Pharmaceuticals", "৳ 3.50", "Tablet", "Paracetamol 500mg + Caffeine 65mg"},
            {"Medicine : Napa 650mg", "Hospital : Square Pharmaceuticals", "৳ 3.00", "Tablet", "Paracetamol 650mg"},
            {"Medicine : Napa 1000mg", "Hospital : Square Pharmaceuticals", "৳ 4.00", "Tablet", "Paracetamol 1000mg"},
            {"Medicine : Napa 500mg (Strip)", "Hospital : Square Pharmaceuticals", "৳ 20.00", "Strip", "10 tablets"},
            {"Medicine : Napa 650mg (Strip)", "Hospital : Square Pharmaceuticals", "৳ 30.00", "Strip", "10 tablets"}
    };

    private String[][] medicine_details = {
            {"Napa 500mg is used for the treatment of fever and pain. It contains Paracetamol 500mg.",
             "Take 1-2 tablets every 4-6 hours as needed.",
             "Do not exceed 8 tablets in 24 hours.",
             "Store in a cool and dry place.",
             "Keep out of reach of children."},

            {"Napa Extra is used for the treatment of fever and pain. It contains Paracetamol 500mg and Caffeine 65mg.",
             "Take 1-2 tablets every 4-6 hours as needed.",
             "Do not exceed 8 tablets in 24 hours.",
             "Store in a cool and dry place.",
             "Keep out of reach of children."},

            {"Napa Rapid is used for the treatment of fever and pain. It contains Paracetamol 500mg.",
             "Take 1-2 tablets every 4-6 hours as needed.",
             "Do not exceed 8 tablets in 24 hours.",
             "Store in a cool and dry place.",
             "Keep out of reach of children."},

            {"Napa Suspension is used for the treatment of fever and pain in children. It contains Paracetamol 120mg/5ml.",
             "Take as directed by the physician.",
             "Shake well before use.",
             "Store in a cool and dry place.",
             "Keep out of reach of children."},

            {"Napa Suppository is used for the treatment of fever and pain. It contains Paracetamol 125mg.",
             "For rectal use only.",
             "Use as directed by the physician.",
             "Store in a cool and dry place.",
             "Keep out of reach of children."},

            {"Napa Plus is used for the treatment of fever and pain. It contains Paracetamol 500mg and Caffeine 65mg.",
             "Take 1-2 tablets every 4-6 hours as needed.",
             "Do not exceed 8 tablets in 24 hours.",
             "Store in a cool and dry place.",
             "Keep out of reach of children."},

            {"Napa 650mg is used for the treatment of fever and pain. It contains Paracetamol 650mg.",
             "Take 1-2 tablets every 4-6 hours as needed.",
             "Do not exceed 8 tablets in 24 hours.",
             "Store in a cool and dry place.",
             "Keep out of reach of children."},

            {"Napa 1000mg is used for the treatment of fever and pain. It contains Paracetamol 1000mg.",
             "Take 1 tablet every 4-6 hours as needed.",
             "Do not exceed 4 tablets in 24 hours.",
             "Store in a cool and dry place.",
             "Keep out of reach of children."},

            {"Napa 500mg (Strip) contains 10 tablets of Paracetamol 500mg.",
             "Take 1-2 tablets every 4-6 hours as needed.",
             "Do not exceed 8 tablets in 24 hours.",
             "Store in a cool and dry place.",
             "Keep out of reach of children."},

            {"Napa 650mg (Strip) contains 10 tablets of Paracetamol 650mg.",
             "Take 1-2 tablets every 4-6 hours as needed.",
             "Do not exceed 8 tablets in 24 hours.",
             "Store in a cool and dry place.",
             "Keep out of reach of children."}
    };

    HashMap<String, String> item;
    ArrayList<HashMap<String, String>> list;
    SimpleAdapter sa;
    ListView lst;
    Button btnBack, btnGoToCart;
    TextView textViewODTitle;

    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_medicine);
        lst = findViewById(R.id.listViewBM);
        btnBack = findViewById(R.id.buttonBMBack);
        btnGoToCart = findViewById(R.id.buttonBMGoToCart);
        textViewODTitle = findViewById(R.id.textViewODTitle);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

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

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BuyMedicineActivity.this, HomeActivity.class));
            }
        });
        btnGoToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BuyMedicineActivity.this, CartBuyMedicineActivity.class));
            }
        });

        list = new ArrayList<>();
        for (int i = 0; i < medicines.length; i++) {
            item = new HashMap<>();
            item.put("line1", medicines[i][0]);
            item.put("line2", medicines[i][1]);
            item.put("line3", medicines[i][2]);
            item.put("line4", medicines[i][3]);
            item.put("line5", medicines[i][4]);
            list.add(item);
        }

        sa = new SimpleAdapter(
                this,
                list,
                R.layout.multi_lines_medicine,
                new String[]{"line1", "line2", "line3", "line4", "line5"},
                new int[]{R.id.line_a, R.id.line_b, R.id.line_c, R.id.line_d, R.id.line_e}
        );

        lst.setAdapter(sa);

        lst.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent it = new Intent(BuyMedicineActivity.this, BuyMedicineDetailsActivity.class);
            it.putExtra("text1", medicines[i][0]);
            it.putExtra("text2", medicine_details[i][0]);
            it.putExtra("text3", medicine_details[i][1]);
            it.putExtra("text4", medicine_details[i][2]);
            it.putExtra("text5", medicine_details[i][3]);
            it.putExtra("text6", medicine_details[i][4]);
            it.putExtra("text7", medicines[i][2]);
            startActivity(it);
        });
    }
}