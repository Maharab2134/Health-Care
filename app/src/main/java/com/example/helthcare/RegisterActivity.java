package com.example.helthcare;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.helthcare.Database.FirebaseHelper;
import com.google.firebase.auth.AuthResult;

public class RegisterActivity extends AppCompatActivity {

    EditText edUsername, edPassword, edEmail, edConfirm;
    Button btn;
    TextView tv;
    private FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edUsername = findViewById(R.id.editTextLTDFullname);
        edPassword = findViewById(R.id.editTextLTDPincode);
        edEmail = findViewById(R.id.editTextLTDAddress);
        edConfirm = findViewById(R.id.editTextLTDContact);
        btn = findViewById(R.id.buttonLTDBooking);
        tv = findViewById(R.id.textViewExistingUser);
        firebaseHelper = new FirebaseHelper(this);

        tv.setOnClickListener(view -> startActivity(new Intent(RegisterActivity.this, LoginActivity.class)));

        btn.setOnClickListener(view -> {
            String username = edUsername.getText() != null ? edUsername.getText().toString().trim() : "";
            String email = edEmail.getText() != null ? edEmail.getText().toString().trim() : "";
            String password = edPassword.getText() != null ? edPassword.getText().toString().trim() : "";
            String confirm = edConfirm.getText() != null ? edConfirm.getText().toString().trim() : "";

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please fill all details", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirm)) {
                Toast.makeText(getApplicationContext(), "Password and Confirm Password didn't match", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isValid(password)) {
                Toast.makeText(getApplicationContext(),
                        "Password must contain at least 8 characters, including a letter, digit, and special symbol",
                        Toast.LENGTH_LONG).show();
                return;
            }

            firebaseHelper.register(username, email, password, task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Registration successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Registration failed: " + task.getException().getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private boolean isValid(String password) {
        if (password.length() < 8) return false;
        boolean hasLetter = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) hasLetter = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else hasSpecial = true;
        }

        return hasLetter && hasDigit && hasSpecial;
    }
}
