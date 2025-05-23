package com.example.helthcare.Splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.helthcare.LoginActivity;
import com.example.helthcare.R;
import com.example.helthcare.SplashActivity;

public class Splash2 extends AppCompatActivity {

    Button btn;
    ImageView skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash2);
        btn = findViewById(R.id.btn);
        skip = findViewById(R.id.skip);

        skip.setOnClickListener(v -> {
            Intent intent = new Intent(Splash2.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        btn.setOnClickListener(v -> {
            Intent intent = new Intent(Splash2.this, Splash3.class);
            startActivity(intent);
            finish();
        });
    }

}