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

public class BuyMedicineBookActivity extends AppCompatActivity {
    EditText edname, edaddress, edcontact, edpincode;
    Button btnBooking;
    private FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_medicine_book);
        edname = findViewById(R.id.editTextBMBFullname);
        edaddress = findViewById(R.id.editTextBMBAddress);
        edcontact = findViewById(R.id.editTextBMBContact);
        edpincode = findViewById(R.id.editTextBMBPincode);
        btnBooking = findViewById(R.id.buttonBMBBooking);
        firebaseHelper = new FirebaseHelper(this);

        Intent intent = getIntent();
        String[] price = intent.getStringExtra("price").split(java.util.regex.Pattern.quote(":"));
        String date = intent.getStringExtra("date");

        btnBooking.setOnClickListener(view -> {
            SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
            String username = sharedPreferences.getString("username", "");

            firebaseHelper.addOrder(username, edname.getText().toString(), edaddress.getText().toString(),
                    edcontact.getText().toString(), Integer.parseInt(edpincode.getText().toString()),
                    date, "", Float.parseFloat(price[1]), "medicine");
            firebaseHelper.removeCart(username, "medicine");
            Toast.makeText(getApplicationContext(), "Your Booking is Done Successfully.", Toast.LENGTH_LONG).show();
            startActivity(new Intent(BuyMedicineBookActivity.this, HomeActivity.class));
        });
    }
}