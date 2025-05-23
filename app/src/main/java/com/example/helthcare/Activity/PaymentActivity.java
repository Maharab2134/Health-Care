package com.example.helthcare.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.helthcare.Database.FirebaseHelper;
import com.example.helthcare.R;

public class PaymentActivity extends AppCompatActivity {
    private TextView tvTotalAmount;
    private EditText etCardNumber, etCardHolder, etExpiry, etCVV;
    private RadioGroup rgPaymentMethod;
    private RadioButton rbCard, rbCashOnDelivery;
    private Button btnPay;
    private FirebaseHelper firebaseHelper;
    private float totalAmount;
    private String deliveryDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // Initialize views
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        etCardNumber = findViewById(R.id.etCardNumber);
        etCardHolder = findViewById(R.id.etCardHolder);
        etExpiry = findViewById(R.id.etExpiry);
        etCVV = findViewById(R.id.etCVV);
        rgPaymentMethod = findViewById(R.id.rgPaymentMethod);
        rbCard = findViewById(R.id.rbCard);
        rbCashOnDelivery = findViewById(R.id.rbCashOnDelivery);
        btnPay = findViewById(R.id.btnPay);
        firebaseHelper = new FirebaseHelper(this);

        // Get data from intent
        totalAmount = getIntent().getFloatExtra("amount", 0.0f);
        deliveryDate = getIntent().getStringExtra("date");
        
        tvTotalAmount.setText(String.format("Total Amount: ৳ %.2f", totalAmount));

        // Handle payment method selection
        rgPaymentMethod.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbCard) {
                etCardNumber.setEnabled(true);
                etCardHolder.setEnabled(true);
                etExpiry.setEnabled(true);
                etCVV.setEnabled(true);
            } else {
                etCardNumber.setEnabled(false);
                etCardHolder.setEnabled(false);
                etExpiry.setEnabled(false);
                etCVV.setEnabled(false);
            }
        });

        // Handle payment button click
        btnPay.setOnClickListener(v -> processPayment());
    }

    private void processPayment() {
        if (rbCard.isChecked()) {
            // Validate card details
            if (!validateCardDetails()) {
                return;
            }
            // Simulate card payment
            simulateCardPayment();
        } else {
            // Cash on delivery
            processCashOnDelivery();
        }
    }

    private boolean validateCardDetails() {
        String cardNumber = etCardNumber.getText().toString();
        String cardHolder = etCardHolder.getText().toString();
        String expiry = etExpiry.getText().toString();
        String cvv = etCVV.getText().toString();

        if (cardNumber.length() != 16) {
            etCardNumber.setError("Invalid card number");
            return false;
        }
        if (cardHolder.isEmpty()) {
            etCardHolder.setError("Please enter card holder name");
            return false;
        }
        if (!expiry.matches("\\d{2}/\\d{2}")) {
            etExpiry.setError("Invalid expiry date (MM/YY)");
            return false;
        }
        if (cvv.length() != 3) {
            etCVV.setError("Invalid CVV");
            return false;
        }
        return true;
    }

    private void simulateCardPayment() {
        // Simulate payment processing
        Toast.makeText(this, "Processing payment...", Toast.LENGTH_SHORT).show();
        
        // Simulate successful payment after 2 seconds
        new android.os.Handler().postDelayed(() -> {
            completeOrder("Card Payment");
        }, 2000);
    }

    private void processCashOnDelivery() {
        completeOrder("Cash on Delivery");
    }

    private void completeOrder(String paymentMethod) {
        SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

        // Create order in Firebase
        firebaseHelper.addOrder(
            username,                    // username
            "Home Delivery",            // address
            deliveryDate,               // date
            "Pending",                  // time
            (int)totalAmount,           // amount as int
            "medicine",                 // type
            paymentMethod,              // payment method
            totalAmount,                // price as float
            "Pending"                   // status
        );

        // Remove items from cart
        firebaseHelper.removeCart(username, "medicine");
        
        Toast.makeText(PaymentActivity.this,
            "Order placed successfully!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(PaymentActivity.this, OrderConfirmationActivity.class);
        intent.putExtra("amount", totalAmount);
        intent.putExtra("payment_method", paymentMethod);
        startActivity(intent);
        finish();
    }
} 