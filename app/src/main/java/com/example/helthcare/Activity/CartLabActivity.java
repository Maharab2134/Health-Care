package com.example.helthcare.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class CartLabActivity extends AppCompatActivity {

    HashMap<String,String> item;
    ArrayList list;
    ListView lst;
    SimpleAdapter sa;
    TextView tvTotal,textView_logo;
    private String[][] packages = {};
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    Button dateButton, timeButton, btnCheckout, btnBack;
    private FirebaseHelper firebaseHelper;
    private FusedLocationProviderClient fusedLocationClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_lab);

        dateButton = findViewById(R.id.buttonCartDates);
        timeButton = findViewById(R.id.buttonCartTime);
        btnCheckout = findViewById(R.id.buttonCartCheckout);
        btnBack = findViewById(R.id.buttonCartBack);
        tvTotal = findViewById(R.id.textViewCartotalPrice);
        lst = findViewById(R.id.listViewCart);
        textView_logo = findViewById(R.id.textView_logo);
        firebaseHelper = new FirebaseHelper(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

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
                                textView_logo.setText(district);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

        firebaseHelper.getCartData(username, "lab", task -> {
            if (task.isSuccessful()) {
                ArrayList<String> dbData = new ArrayList<>();
                float totalAmount = 0;

                for (QueryDocumentSnapshot document : task.getResult()) {
                    String product = document.getString("product");
                    float price = document.getDouble("price").floatValue();
                    dbData.add(product + "$" + price);
                    totalAmount += price;
                }

                packages = new String[dbData.size()][];
                for (int i = 0; i < packages.length; i++) {
                    packages[i] = new String[5];
                }

                for (int i = 0; i < dbData.size(); i++) {
                    String arrData = dbData.get(i);
                    String[] strData = arrData.split(java.util.regex.Pattern.quote("$"));
                    packages[i][0] = strData[0];
                    packages[i][4] = "Cost : " + strData[1] + "/-";
                }

                tvTotal.setText("Total Cost : " + totalAmount);

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
            } else {
                Toast.makeText(getApplicationContext(), "Error loading cart: " + task.getException().getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        btnBack.setOnClickListener(view -> startActivity(new Intent(CartLabActivity.this, LabTestActivity.class)));

        btnCheckout.setOnClickListener(view -> {
            Intent it = new Intent(CartLabActivity.this, LabTestBookActivity.class);
            it.putExtra("price", tvTotal.getText());
            it.putExtra("date", dateButton.getText());
            it.putExtra("time", timeButton.getText());
            startActivity(it);
        });

        initDatePicker();
        dateButton.setOnClickListener(view -> datePickerDialog.show());

        initTimePicker();
        timeButton.setOnClickListener(view -> timePickerDialog.show());
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, day) -> {
            month = month + 1;
            dateButton.setText(day + "/" + month + "/" + year);
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_DARK;
        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis() + 86400000);
    }

    private void initTimePicker() {
        TimePickerDialog.OnTimeSetListener timeSetListener = (timePicker, hour, minute) -> {
            timeButton.setText(String.format("%02d:%02d", hour, minute));
        };

        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);

        int style = AlertDialog.THEME_HOLO_DARK;
        timePickerDialog = new TimePickerDialog(this, style, timeSetListener, hour, minute, true);
    }
}