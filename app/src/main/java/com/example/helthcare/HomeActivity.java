package com.example.helthcare;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.helthcare.Activity.BuyMedicineActivity;
import com.example.helthcare.Activity.FindDoctorActivity;
import com.example.helthcare.Activity.HealthArticlesActivity;
import com.example.helthcare.Activity.LabTestActivity;
import com.example.helthcare.Activity.OrderDetailsActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        CardView exit = findViewById(R.id.cardExit);
        CardView labTest = findViewById(R.id.cardLabTest);
        CardView findDoctor = findViewById(R.id.cardFindDoctor);
        CardView orderDetails = findViewById(R.id.cardOrderDetails);
        CardView buyMedicine = findViewById(R.id.cardBuyMedicine);
        CardView health = findViewById(R.id.cardHealthDoctor);

        SharedPreferences sharedpreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
        String username = sharedpreferences.getString("username","").toString();
        Toast.makeText(getApplicationContext(), "Welcome "+username, Toast.LENGTH_SHORT).show();


        exit.setOnClickListener(view -> {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.apply();
            startActivity(new Intent(HomeActivity.this,LoginActivity.class));
        });

        findDoctor.setOnClickListener(view -> startActivity(new Intent(HomeActivity.this, FindDoctorActivity.class)));
        labTest.setOnClickListener(view -> startActivity(new Intent(HomeActivity.this, LabTestActivity.class)));
        orderDetails.setOnClickListener(view -> startActivity(new Intent(HomeActivity.this, OrderDetailsActivity.class)));
        buyMedicine.setOnClickListener(view -> startActivity(new Intent(HomeActivity.this, BuyMedicineActivity.class)));
        health.setOnClickListener(view -> startActivity(new Intent(HomeActivity.this, HealthArticlesActivity.class)));

    }
}