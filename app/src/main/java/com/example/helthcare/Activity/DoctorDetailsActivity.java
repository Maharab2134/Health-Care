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
            {"Dr. Ayesha Rahman", "Apollo Hospital, Dhaka", "Exp : 15yrs", "Mobile No: 01712345678", "1500"},
            {"Doctor Name : Dr. Farhan Ahmed", "Hospital Address : Square Hospital, Dhaka", "Exp : 12yrs", "Mobile No: 01812345678", "2000"},
            {"Doctor Name : Dr. Nasreen Akter", "Hospital Address : United Hospital, Dhaka", "Exp : 10yrs", "Mobile No: 01912345678", "1800"},
            {"Doctor Name : Dr. Imran Hossain", "Hospital Address : Popular Hospital, Dhaka", "Exp : 8yrs", "Mobile No: 01612345678", "1200"},
            {"Doctor Name : Dr. Sabrina Khan", "Hospital Address : Labaid Hospital, Dhaka", "Exp : 14yrs", "Mobile No: 01512345678", "2500"}
    };
    private String[][] doctor_details2 = {
            {"Doctor Name : Dr. Mahmud Rahman", "Hospital Address : BIRDEM Hospital, Dhaka", "Exp : 20yrs", "Mobile No: 01722345678", "2200"},
            {"Doctor Name : Dr. Tahmina Begum", "Hospital Address : Evercare Hospital, Dhaka", "Exp : 15yrs", "Mobile No: 01822345678", "2800"},
            {"Doctor Name : Dr. Kamrul Islam", "Hospital Address : Ibn Sina Hospital, Dhaka", "Exp : 12yrs", "Mobile No: 01922345678", "2000"},
            {"Doctor Name : Dr. Nasreen Sultana", "Hospital Address : Central Hospital, Dhaka", "Exp : 10yrs", "Mobile No: 01622345678", "1800"},
            {"Doctor Name : Dr. Shahidul Islam", "Hospital Address : Medinova Hospital, Dhaka", "Exp : 18yrs", "Mobile No: 01522345678", "2500"}
    };
    private String[][] doctor_details3 = {
            {"Doctor Name : Dr. Farhana Rahman", "Hospital Address : Dental Unit, DMCH", "Exp : 8yrs", "Mobile No: 01732345678", "1000"},
            {"Doctor Name : Dr. Ashraful Islam", "Hospital Address : Dental Care Hospital", "Exp : 10yrs", "Mobile No: 01832345678", "1200"},
            {"Doctor Name : Dr. Nasreen Jahan", "Hospital Address : Dental Solutions", "Exp : 7yrs", "Mobile No: 01932345678", "900"},
            {"Doctor Name : Dr. Kamrul Hasan", "Hospital Address : Smile Dental Care", "Exp : 12yrs", "Mobile No: 01632345678", "1500"},
            {"Doctor Name : Dr. Sabina Yasmin", "Hospital Address : Dental Excellence", "Exp : 9yrs", "Mobile No: 01532345678", "1100"}
    };
    private String[][] doctor_details4 = {
            {"Doctor Name : Dr. Mahbubur Rahman", "Hospital Address : CMH, Dhaka", "Exp : 25yrs", "Mobile No: 01742345678", "3000"},
            {"Doctor Name : Dr. Nasreen Akter", "Hospital Address : CMH, Dhaka", "Exp : 20yrs", "Mobile No: 01842345678", "2800"},
            {"Doctor Name : Dr. Imran Hossain", "Hospital Address : CMH, Dhaka", "Exp : 18yrs", "Mobile No: 01942345678", "2500"},
            {"Doctor Name : Dr. Farhana Begum", "Hospital Address : CMH, Dhaka", "Exp : 15yrs", "Mobile No: 01642345678", "2200"},
            {"Doctor Name : Dr. Shahidul Islam", "Hospital Address : CMH, Dhaka", "Exp : 22yrs", "Mobile No: 01542345678", "2700"}
    };
    private String[][] doctor_details5 = {
            {"Doctor Name : Dr. Ayesha Khatun", "Hospital Address : NITOR, Dhaka", "Exp : 15yrs", "Mobile No: 01752345678", "2000"},
            {"Doctor Name : Dr. Farhan Ahmed", "Hospital Address : NITOR, Dhaka", "Exp : 12yrs", "Mobile No: 01852345678", "1800"},
            {"Doctor Name : Dr. Nasreen Sultana", "Hospital Address : NITOR, Dhaka", "Exp : 10yrs", "Mobile No: 01952345678", "1600"},
            {"Doctor Name : Dr. Imran Khan", "Hospital Address : NITOR, Dhaka", "Exp : 8yrs", "Mobile No: 01652345678", "1400"},
            {"Doctor Name : Dr. Sabrina Islam", "Hospital Address : NITOR, Dhaka", "Exp : 14yrs", "Mobile No: 01552345678", "1900"}
    };

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
                R.layout.multi_lines_doctor,
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
            Log.d("DoctorDetailsActivity", doctor_details[i][0]);
            Log.d("DoctorDetailsActivity", doctor_details[i][1]);
            Log.d("DoctorDetailsActivity", doctor_details[i][3]);
            Log.d("DoctorDetailsActivity", doctor_details[i][4]);

            // Sending data through intent with correct keys
            intent.putExtra("text1", title);                    // Title
            intent.putExtra("text2", doctor_details[i][0]);     // Doctor Name
            intent.putExtra("text3", doctor_details[i][1]);     // Hospital Address
            intent.putExtra("text4", doctor_details[i][3]);     // Mobile Number
            intent.putExtra("text5", doctor_details[i][4]);     // Fees

            startActivity(intent);
        });
    }
}
