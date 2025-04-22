package com.example.helthcare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.helthcare.Database.Database;

public class RegisterActivity extends AppCompatActivity {

    EditText edUsername, edPassword, edEmail, edConfirm;
    Button btn;
    TextView tv;

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

            Database db = new Database(getApplicationContext(), "healthcare", null, 1);
            db.register(username, email, password);
            Toast.makeText(getApplicationContext(), "Record Inserted", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        });
    }

    public static boolean isValid(String password) {
        int hasLetter = 0, hasDigit = 0, hasSpecial = 0;

        if (password.length() < 8) return false;

        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) hasLetter = 1;
            else if (Character.isDigit(c)) hasDigit = 1;
            else if ("!@#$%^&*()-_=+{}[]|:;'<>,.?/".contains(String.valueOf(c))) hasSpecial = 1;
        }

        return hasLetter == 1 && hasDigit == 1 && hasSpecial == 1;
    }
}
