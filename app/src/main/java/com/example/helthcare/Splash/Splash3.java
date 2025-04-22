package com.example.helthcare.Splash;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.helthcare.LoginActivity;
import com.example.helthcare.R;
import com.example.helthcare.SplashActivity;

public class Splash3 extends AppCompatActivity {

    Button btn;
    ImageView skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash3);


        btn = findViewById(R.id.btn);
        skip = findViewById(R.id.skip);

        skip.setOnClickListener(v -> {
            Intent intent = new Intent(Splash3.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        btn.setOnClickListener(v -> {
            Intent intent = new Intent(Splash3.this, Splash4.class);
            startActivity(intent);
            finish();
        });
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Splash3.this, Splash2.class);
        startActivity(intent);
        super.onBackPressed();
    }
}