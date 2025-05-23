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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.helthcare.Database.FirebaseHelper;
import com.example.helthcare.HomeActivity;
import com.example.helthcare.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class OrderDetailsActivity extends AppCompatActivity {

    private String[][] order_details = {};
    private HashMap<String, String> item;
    private ArrayList list;
    private SimpleAdapter sa;
    private ListView lst;
    private Button btn;
    private FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        btn = findViewById(R.id.buttonBMBack);
        lst = findViewById(R.id.listViewOD);
        firebaseHelper = new FirebaseHelper(this);

        btn.setOnClickListener(view -> startActivity(new Intent(OrderDetailsActivity.this, HomeActivity.class)));

        SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

        // First get orders
        firebaseHelper.getOrderData(username, task -> {
            if (task.isSuccessful()) {
                ArrayList<String> dbData = new ArrayList<>();
                for (DocumentSnapshot document : task.getResult().getDocuments()) {
                    String fullname = document.getString("fullname");
                    String address = document.getString("address");
                    String contact = document.getString("contact");
                    String pincode = String.valueOf(document.getLong("pincode"));
                    String date = document.getString("date");
                    String time = document.getString("time");
                    String price = String.valueOf(document.getDouble("price"));
                    String otype = document.getString("otype");

                    dbData.add(fullname + "$" + address + "$" + contact + "$" + pincode + "$" + date + "$" + time + "$" + price + "$" + otype);
                }

                // Then get appointments
                firebaseHelper.getAppointments()
                    .whereEqualTo("username", username)
                    .get()
                    .addOnCompleteListener(appointmentTask -> {
                        if (appointmentTask.isSuccessful()) {
                            for (DocumentSnapshot document : appointmentTask.getResult().getDocuments()) {
                                String doctorName = document.getString("doctorName");
                                String address = document.getString("address");
                                String contact = document.getString("contact");
                                String fees = document.getString("fees");
                                String date = document.getString("appointmentDate");
                                String time = document.getString("appointmentTime");

                                dbData.add(doctorName + "$" + address + "$" + contact + "$0$" + date + "$" + time + "$" + fees + "$appointment");
                            }

                            // Process all data (orders + appointments)
                            order_details = new String[dbData.size()][];
                            for (int i = 0; i < order_details.length; i++) {
                                order_details[i] = new String[5];
                                String arrData = dbData.get(i);
                                String[] strData = arrData.split(java.util.regex.Pattern.quote("$"));
                                order_details[i][0] = strData[0];
                                order_details[i][1] = strData[1];
                                if (strData[7].compareTo("medicine") == 0) {
                                    order_details[i][3] = "Del:" + strData[4];
                                } else if (strData[7].compareTo("appointment") == 0) {
                                    order_details[i][3] = "Appt:" + strData[4] + " " + strData[5];
                                } else {
                                    order_details[i][3] = "Del:" + strData[4] + " " + strData[5];
                                }
                                order_details[i][2] = "৳" + strData[6];
                                order_details[i][4] = strData[7];
                            }

                            list = new ArrayList<>();
                            for (int i = 0; i < order_details.length; i++) {
                                item = new HashMap<>();
                                item.put("line1", order_details[i][0]);
                                item.put("line2", order_details[i][1]);
                                item.put("line3", order_details[i][2]);
                                item.put("line4", order_details[i][3]);
                                item.put("line5", order_details[i][4]);
                                list.add(item);
                            }

                            // Create adapter with appropriate layout based on item type
                            int layoutId;
                            if (order_details[0][4].equals("appointment")) {
                                layoutId = R.layout.multi_lines_appointment;
                            } else if (order_details[0][4].equals("medicine")) {
                                layoutId = R.layout.multi_lines_medicine;
                            } else {
                                layoutId = R.layout.multi_lines_lab;
                            }

                            sa = new SimpleAdapter(this, list,
                                    layoutId,
                                    new String[]{"line1", "line2", "line3", "line4", "line5"},
                                    new int[]{R.id.line_a, R.id.line_b, R.id.line_c, R.id.line_d, R.id.line_e});
                            lst.setAdapter(sa);
                        } else {
                            Toast.makeText(getApplicationContext(), "Error loading appointments: " + appointmentTask.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
            } else {
                Toast.makeText(getApplicationContext(), "Error loading orders: " + task.getException().getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}