package com.example.helthcare.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.helthcare.R;

import java.util.ArrayList;
import java.util.HashMap;

public class DoctorDetailsActivity extends AppCompatActivity {

    // Doctor details for different categories
    private String[][] doctor_details1 = {
            {"Doctor Name : Ajit Saste", "Hospital Address : Pipri", "Exp : 5yrs", "Mobile No: 9090909090", "600"},
            {"Doctor Name : Prasad Pawar", "Hospital Address : Nigdi", "Exp : 15yrs", "Mobile No: 8080808080", "900"},
            {"Doctor Name : Swapnil Deshmukh", "Hospital Address : Pune", "Exp : 8yrs", "Mobile No: 4932052345", "400"},
            {"Doctor Name : Deepak Kale", "Hospital Address : Chinchwad", "Exp : 6yrs", "Mobile No: 7483748323", "500"},
            {"Doctor Name : Ashok Panda", "Hospital Address : Katraj", "Exp : 7yrs", "Mobile No: 7878787878", "800"}
    };
    private String[][] doctor_details2 = {
            {"Doctor Name : Rishavh Raut", "Hospital Address : Pipri", "Exp : 5yrs", "Mobile No: 9090909090", "600"},
            {"Doctor Name : Jayant Pawar", "Hospital Address : Nigdi", "Exp : 15yrs", "Mobile No: 8080808080", "900"},
            {"Doctor Name : Swapnil Kulkarni", "Hospital Address : Pune", "Exp : 8yrs", "Mobile No: 4932052345", "400"},
            {"Doctor Name : Pralhad Kale", "Hospital Address : Chinchwad", "Exp : 6yrs", "Mobile No: 7483748323", "500"},
            {"Doctor Name : Ashok Panda", "Hospital Address : Katraj", "Exp : 7yrs", "Mobile No: 7878787878", "800"}
    };
    private String[][] doctor_details3 = doctor_details1; // Same as doctor_details1
    private String[][] doctor_details4 = doctor_details1; // Same as doctor_details1
    private String[][] doctor_details5 = doctor_details1; // Same as doctor_details1

    TextView tv;
    Button btn;
    String[][] doctor_details = {};
    ArrayList<HashMap<String, String>> list;
    SimpleAdapter sa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_details);

        tv = findViewById(R.id.textViewODTitle);
        btn = findViewById(R.id.buttonBMBack);

        // Get the title/category passed from previous activity
        Intent it = getIntent();
        String title = it.getStringExtra("title");
        tv.setText(title);

        // Choose doctor list based on category
        if (title.compareTo("Family Physician") == 0) {
            doctor_details = doctor_details1;
        } else if (title.compareTo("Dietician") == 0) {
            doctor_details = doctor_details2;
        } else if (title.compareTo("Dentist") == 0) {
            doctor_details = doctor_details3;
        } else if (title.compareTo("Surgeon") == 0) {
            doctor_details = doctor_details4;
        } else {
            doctor_details = doctor_details5;
        }

        // Go back to FindDoctorActivity
        btn.setOnClickListener(view -> startActivity(new Intent(DoctorDetailsActivity.this, FindDoctorActivity.class)));

        // Populate the list
        list = new ArrayList<>();
        for (int i = 0; i < doctor_details.length; i++) {
            HashMap<String, String> item = new HashMap<>();
            item.put("line1", doctor_details[i][0]);
            item.put("line2", doctor_details[i][1]);
            item.put("line3", doctor_details[i][2]);
            item.put("line4", doctor_details[i][3]);
            item.put("line5", "৳ " + doctor_details[i][4] + "/-");
            list.add(item);
        }

        sa = new SimpleAdapter(
                this,
                list,
                R.layout.multi_lines,
                new String[]{"line1", "line2", "line3", "line4", "line5"},
                new int[]{R.id.line_a, R.id.line_b, R.id.line_c, R.id.line_d, R.id.line_e}
        );

        ListView lst = findViewById(R.id.listViewDD);
        lst.setAdapter(sa);

        // Handle list item click
        lst.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(DoctorDetailsActivity.this, BookAppointmentActivity.class);

            // Logging data to verify
            Log.d("DoctorDetailsActivity", title);
            Log.d("DoctorDetailsActivity",  doctor_details[i][0]);
            Log.d("DoctorDetailsActivity", doctor_details[i][1]);
            Log.d("DoctorDetailsActivity", doctor_details[i][3]);
            Log.d("DoctorDetailsActivity", doctor_details[i][4]);

            // Sending data through intent
            intent.putExtra("Text1", title);                    // Title
            intent.putExtra("Text2", doctor_details[i][0]);     // Doctor Name
            intent.putExtra("Text3", doctor_details[i][1]);     // Hospital Address
            intent.putExtra("Text4", doctor_details[i][3]);     // Mobile Number
            intent.putExtra("Text5", doctor_details[i][4]);     // Fees

            startActivity(intent);
        });

    }
}
