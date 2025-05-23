package com.example.helthcare.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.helthcare.HomeActivity;
import com.example.helthcare.R;

public class OrderConfirmationActivity extends AppCompatActivity {
    private TextView tvOrderSuccess, tvPaymentMethod, tvAmount;
    private Button btnBackToHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        // Initialize views
        tvOrderSuccess = findViewById(R.id.tvOrderSuccess);
        tvPaymentMethod = findViewById(R.id.tvPaymentMethod);
        tvAmount = findViewById(R.id.tvAmount);
        btnBackToHome = findViewById(R.id.btnBackToHome);

        // Get data from intent
        float amount = getIntent().getFloatExtra("amount", 0.0f);
        String paymentMethod = getIntent().getStringExtra("payment_method");

        // Set text
        tvAmount.setText(String.format("Total Amount: ৳ %.2f", amount));
        tvPaymentMethod.setText("Payment Method: " + paymentMethod);

        // Handle back to home button
        btnBackToHome.setOnClickListener(v -> {
            finish();
            startActivity(new Intent(OrderConfirmationActivity.this, HomeActivity.class));
        });
    }
} 