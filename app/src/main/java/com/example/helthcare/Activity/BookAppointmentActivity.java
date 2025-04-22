package com.example.helthcare.Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.helthcare.Database.Database;
import com.example.helthcare.HomeActivity;
import com.example.helthcare.R;

import java.util.Calendar;

public class BookAppointmentActivity extends AppCompatActivity {

    EditText ed1, ed2, ed3, ed4;
    TextView tv;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private Button dateButton, timeButton, btnBook, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);

        tv = findViewById(R.id.textViewAppTitle);
        ed1 = findViewById(R.id.editTextAppFullname);
        ed2 = findViewById(R.id.editTextAppAddress);
        ed3 = findViewById(R.id.editTextAppContact);
        ed4 = findViewById(R.id.editTextAppPrice);
        dateButton = findViewById(R.id.buttonAppDate);
        timeButton = findViewById(R.id.buttonAppTime);
        btnBook = findViewById(R.id.buttonBookAppointment);
        btnBack = findViewById(R.id.buttonAppBack);

        ed1.setKeyListener(null);
        ed2.setKeyListener(null);
        ed3.setKeyListener(null);
        ed4.setKeyListener(null);

        Intent it = getIntent();
        String title = it.getStringExtra("Text1");
        String fullname = it.getStringExtra("Text2");
        String address = it.getStringExtra("Text3");
        String contact = it.getStringExtra("Text4");
        String fees = it.getStringExtra("Text5");

        Log.d("BookAppointment", "Title: " + title);
        Log.d("BookAppointment", "Doctor Name: " + fullname);
        Log.d("BookAppointment", "Address: " + address);
        Log.d("BookAppointment", "Mobile No: " + contact);
        Log.d("BookAppointment", "Fees: " + fees);


        tv.setText(title);
        ed1.setText(fullname);
        ed2.setText(address);
        ed3.setText(contact);
        ed4.setText("৳ "+ fees + "/-");

        initDatePicker();
        dateButton.setOnClickListener(v -> datePickerDialog.show());

        initTimePicker();
        timeButton.setOnClickListener(v -> timePickerDialog.show());

        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(BookAppointmentActivity.this, FindDoctorActivity.class));
        });

        btnBook.setOnClickListener(v -> {
            Database db = new Database(getApplicationContext(), "healthcare", null, 1);
            SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
            String username = sharedPreferences.getString("username", "");

            String date = dateButton.getText().toString();
            String time = timeButton.getText().toString();

            if (db.checkAppintmentExists(username, title + "=>" + fullname, address, contact, date, time) == 1) {
                Toast.makeText(getApplicationContext(), "Appointment already booked", Toast.LENGTH_LONG).show();
            } else {
                db.addOrder(username, title + "=>" + fullname, address, contact, 123456, date, time, Float.parseFloat(fees), "appointment");
                Toast.makeText(getApplicationContext(), "Your Appointment is booked Successfully", Toast.LENGTH_LONG).show();
                startActivity(new Intent(BookAppointmentActivity.this, HomeActivity.class));
                finish(); // Optional: closes this activity to prevent coming back on back press
            }

        });
    }

    private void initDatePicker(){
        DatePickerDialog.OnDateSetListener dateSetListener= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                i1 = i1+1;
                dateButton.setText(i2+"/"+i1+"/"+i);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year= cal.get(Calendar.YEAR);
        int month= cal.get(Calendar.MONTH);
        int day= cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_DARK;
        datePickerDialog = new DatePickerDialog(this,style,dateSetListener,year,month,day);
        datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis()+86400000);
    }
    private void initTimePicker(){
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                timeButton.setText(i+":"+i1);
            }
        };
        Calendar cal = Calendar.getInstance();
        int hrs= cal.get(Calendar.HOUR);
        int mins= cal.get(Calendar.MINUTE);

        int style = AlertDialog.THEME_HOLO_DARK;
        timePickerDialog = new TimePickerDialog(this,style,timeSetListener,hrs,mins,true);
    }
}