package com.example.helthcare.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.helthcare.Database.FirebaseHelper;
import com.example.helthcare.HomeActivity;
import com.example.helthcare.R;

public class LabTestBookActivity extends AppCompatActivity {
    EditText edname, edaddress, edcontact, edpincode;
    Button btnBooking;
    private FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_test_book);
        edname = findViewById(R.id.editTextLTDFullname);
        edaddress = findViewById(R.id.editTextLTDAddress);
        edcontact = findViewById(R.id.editTextLTDContact);
        edpincode = findViewById(R.id.editTextLTDPincode);
        btnBooking = findViewById(R.id.buttonLTDBooking);
        firebaseHelper = new FirebaseHelper(this);

        Intent intent = getIntent();
        String[] price = intent.getStringExtra("price").split(java.util.regex.Pattern.quote(":"));
        String date = intent.getStringExtra("date");
        String time = intent.getStringExtra("time");

        btnBooking.setOnClickListener(view -> {
            SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
            String username = sharedPreferences.getString("username", "");

            firebaseHelper.addOrder(username, edname.getText().toString(), edaddress.getText().toString(),
                    edcontact.getText().toString(), Integer.parseInt(edpincode.getText().toString()),
                    date, time, Float.parseFloat(price[1]), "lab");
            firebaseHelper.removeCart(username, "lab");
            Toast.makeText(getApplicationContext(), "Your Booking is Done Successfully.", Toast.LENGTH_LONG).show();
            startActivity(new Intent(LabTestBookActivity.this, HomeActivity.class));
        });
    }
}