package com.example.helthcare.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.helthcare.Database.FirebaseHelper;
import com.example.helthcare.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseHelper firebaseHelper;
    private TextInputEditText etFullName, etEmail, etPhone, etAddress;
    private MaterialButton btnUpdate;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize Firebase Helper
        firebaseHelper = new FirebaseHelper(this);

        // Initialize views
        initializeViews();

        // Setup toolbar
        setupToolbar();

        // Load user data
        loadUserData();

        // Setup click listeners
        setupClickListeners();
    }

    private void initializeViews() {
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnBack = findViewById(R.id.btnBack);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void loadUserData() {
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        if (currentUser != null) {
            firebaseHelper.getUserData(currentUser, task -> {
                if (task.isSuccessful() && !task.getResult().isEmpty()) {
                    String name = task.getResult().getDocuments().get(0).getString("name");
                    String phone = task.getResult().getDocuments().get(0).getString("phone");
                    String address = task.getResult().getDocuments().get(0).getString("address");

                    etFullName.setText(name);
                    etEmail.setText(currentUser);
                    etPhone.setText(phone);
                    etAddress.setText(address);
                }
            });
        }
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnUpdate.setOnClickListener(v -> {
            String name = etFullName.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String address = etAddress.getText().toString().trim();

            if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            String currentUser = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            if (currentUser != null) {
                firebaseHelper.updateUserProfile(currentUser, name, phone, address, success -> {
                    if (success) {
                        Toast.makeText(ProfileActivity.this, "Profile updated successfully", 
                            Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ProfileActivity.this, "Failed to update profile", 
                            Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
} 