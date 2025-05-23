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

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.helthcare.HomeActivity;
import com.example.helthcare.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class LabTestActivity extends AppCompatActivity {
    private String[][] packages = {
            {"Package 1 : Full Body Checkup", "", "", "", "৳ 5000"},
            {"Package 2 : Blood Glucose Fasting", "", "", "", "৳ 300"},
            {"Package 3 : COVID-19 Antibody - IgG", "", "", "", "৳ 1200"},
            {"Package 4 : Thyroid Check", "", "", "", "৳ 800"},
            {"Package 5 : Immunity Check", "", "", "", "৳ 1500"},
            {"Package 6 : Complete Blood Count", "", "", "", "৳ 400"},
            {"Package 7 : Kidney Function Test", "", "", "", "৳ 1000"},
            {"Package 8 : Liver Function Test", "", "", "", "৳ 1000"},
            {"Package 9 : Lipid Profile", "", "", "", "৳ 800"},
            {"Package 10 : Diabetes Check", "", "", "", "৳ 600"}
    };

    private String[] package_details = {
            "Blood Glucose Fasting\n" +
                    "Complete Hemogram\n" +
                    "HbA1c\n" +
                    "Iron Studies\n" +
                    "Kidney Function Test\n" +
                    "LDH Lactate Dehydrogenase\n" +
                    "Lipid Profile\n" +
                    "Liver Function Test\n" +
                    "Thyroid Profile-Total (T3, T4 & TSH Ultra-sensitive)\n" +
                    "Urine Routine & Microscopy",

            "Blood Glucose Fasting\n" +
                    "Blood Glucose Post Prandial (PP)\n" +
                    "HbA1c (Glycosylated Hemoglobin)",

            "COVID-19 Antibody - IgG\n" +
                    "COVID-19 Antibody - IgM\n" +
                    "COVID-19 RT-PCR Test",

            "Thyroid Profile-Total (T3, T4 & TSH Ultra-sensitive)\n" +
                    "Thyroid Profile-Free (FT3, FT4 & TSH Ultra-sensitive)",

            "Complete Blood Count\n" +
                    "CRP (C Reactive Protein) Quantitative\n" +
                    "Iron Studies\n" +
                    "Kidney Function Test\n" +
                    "Liver Function Test\n" +
                    "Vitamin D Total-25 Hydroxy",

            "Complete Blood Count\n" +
                    "Hemoglobin (Hb)\n" +
                    "Platelet Count\n" +
                    "Total Leukocyte Count\n" +
                    "Differential Leukocyte Count",

            "Blood Urea Nitrogen (BUN)\n" +
                    "Creatinine\n" +
                    "Uric Acid\n" +
                    "Electrolytes (Na, K, Cl)\n" +
                    "eGFR (Estimated Glomerular Filtration Rate)",

            "Bilirubin (Total, Direct & Indirect)\n" +
                    "SGOT (AST)\n" +
                    "SGPT (ALT)\n" +
                    "Alkaline Phosphatase\n" +
                    "Total Protein\n" +
                    "Albumin\n" +
                    "Globulin\n" +
                    "A/G Ratio",

            "Total Cholesterol\n" +
                    "HDL Cholesterol\n" +
                    "LDL Cholesterol\n" +
                    "VLDL Cholesterol\n" +
                    "Triglycerides\n" +
                    "Total Cholesterol/HDL Ratio",

            "Blood Glucose Fasting\n" +
                    "Blood Glucose Post Prandial (PP)\n" +
                    "HbA1c (Glycosylated Hemoglobin)\n" +
                    "Insulin Fasting\n" +
                    "C-Peptide"
    };

    HashMap<String, String> item;
    ArrayList<HashMap<String, String>> list;
    SimpleAdapter sa;
    Button btnGoToCart, btnBack;
    ListView listView;
    TextView textViewODTitle;
    private FusedLocationProviderClient fusedLocationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_test);

        btnGoToCart = findViewById(R.id.buttonLTGoToCart);
        btnBack = findViewById(R.id.buttonLTBack);
        listView = findViewById(R.id.listViewLT);
        textViewODTitle = findViewById(R.id.textViewODTitle);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        // ✅ Safe to access location here
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

        btnBack.setOnClickListener(view -> startActivity(new Intent(LabTestActivity.this, HomeActivity.class)));

        list = new ArrayList<>();
        for (int i = 0; i < packages.length; i++) {
            item = new HashMap<>();
            item.put("line1", packages[i][0]);
            item.put("line2", packages[i][1]);
            item.put("line3", packages[i][2]);
            item.put("line4", packages[i][3]);
            item.put("line5", packages[i][4]);
            list.add(item);
        }

        sa = new SimpleAdapter(
                this,
                list,
                R.layout.multi_lines_lab,
                new String[]{"line1", "line2", "line3", "line4", "line5"},
                new int[]{R.id.line_a, R.id.line_b, R.id.line_c, R.id.line_d, R.id.line_e}
        );

        listView.setAdapter(sa);

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent it = new Intent(LabTestActivity.this, LabTestDetailsActivity.class);
            it.putExtra("text1", packages[i][0]);
            it.putExtra("text2", package_details[i]);
            it.putExtra("text3", packages[i][4]);
            startActivity(it);
        });

        btnGoToCart.setOnClickListener(view -> {
            startActivity(new Intent(LabTestActivity.this, CartLabActivity.class));
        });
    }


    // Helper method to ensure strings are not null
    private String safe(String value) {
        return value != null ? value : "";
    }
}
