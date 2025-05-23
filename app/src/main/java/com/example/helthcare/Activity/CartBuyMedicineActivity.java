package com.example.helthcare.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class CartBuyMedicineActivity extends AppCompatActivity {

    private String[][] packages = {};
    private HashMap<String, String> item;
    private ArrayList list;
    private SimpleAdapter sa;
    private ListView lst;
    TextView textViewODTitle3;
    private Button btnBack, btnCheckout, btnDate;
    private TextView tvTotalCost;
    private FirebaseHelper firebaseHelper;
    private FusedLocationProviderClient fusedLocationClient;
    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_buy_medicine);

        btnCheckout = findViewById(R.id.buttonBMCartCheckout);
        btnBack = findViewById(R.id.buttonBMCartBack);
        btnDate = findViewById(R.id.buttonBMCartDate);
        tvTotalCost = findViewById(R.id.textViewBMCartTotalPrice);
        textViewODTitle3 = findViewById(R.id.textViewODTitle3);
        lst = findViewById(R.id.listViewBMCart);
        firebaseHelper = new FirebaseHelper(this);
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
                                textViewODTitle3.setText(district);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

        btnBack.setOnClickListener(view -> startActivity(new Intent(CartBuyMedicineActivity.this, BuyMedicineActivity.class)));

        btnCheckout.setOnClickListener(view -> {
            SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
            String username = sharedPreferences.getString("username", "");
            String date = btnDate.getText().toString();

            if (date.equals("Select Date")) {
                Toast.makeText(getApplicationContext(), "Please select delivery date", Toast.LENGTH_SHORT).show();
            } else {
                firebaseHelper.getCartData(username, "medicine", task -> {
                    if (task.isSuccessful()) {
                        float totalAmount = 0;
                        for (DocumentSnapshot document : task.getResult().getDocuments()) {
                            totalAmount += document.getDouble("price");
                        }
                        if (totalAmount > 0) {
                            Intent it = new Intent(CartBuyMedicineActivity.this, PaymentActivity.class);
                            it.putExtra("amount", totalAmount);
                            it.putExtra("date", date);
                            startActivity(it);
                        }
                    }
                });
            }
        });

        btnDate.setOnClickListener(view -> {
            initDatePicker();
            datePickerDialog.show();
        });

        SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

        firebaseHelper.getCartData(username, "medicine", task -> {
            if (task.isSuccessful()) {
                float totalAmount = 0;
                for (DocumentSnapshot document : task.getResult()) {
                    totalAmount += document.getDouble("price");
                }

                tvTotalCost.setText("Total Cost : " + totalAmount);

                list = new ArrayList();
                for (DocumentSnapshot document : task.getResult()) {
                    item = new HashMap<>();
                    item.put("line1", document.getString("product"));
                    item.put("line2", "Price: " + document.getDouble("price"));
                    list.add(item);
                }

                sa = new SimpleAdapter(this, list, R.layout.multi_lines_medicine,
                        new String[]{"line1", "line2"},
                        new int[]{R.id.line_a, R.id.line_b});
                lst.setAdapter(sa);
            }
        });
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, day) -> {
            month = month + 1;
            String date = makeDateString(day, month, year);
            btnDate.setText(date);
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(this, dateSetListener, year, month, day);
    }

    private String makeDateString(int day, int month, int year) {
        return day + "/" + month + "/" + year;
    }

}