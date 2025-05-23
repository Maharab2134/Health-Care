package com.example.helthcare.Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.helthcare.Database.FirebaseHelper;
import com.example.helthcare.HomeActivity;
import com.example.helthcare.R;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class BookAppointmentActivity extends AppCompatActivity {

    EditText ed1, ed2, ed3, ed4;
    TextView tv;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private Button dateButton, timeButton, btnBook, btnBack;
    private FirebaseHelper firebaseHelper;
    HashMap<String, String> item;
    ArrayList<HashMap<String, String>> list;
    SimpleAdapter sa;
    ListView lst;
    private String[][] packages = {};

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
        lst = findViewById(R.id.listViewApp);
        firebaseHelper = new FirebaseHelper(this);

        // Set up date and time pickers
        initDatePicker();
        initTimePicker();

        // Get data from intent
        Intent intent = getIntent();
        String title = intent.getStringExtra("text1");
        String fullname = intent.getStringExtra("text2");
        String address = intent.getStringExtra("text3");
        String contact = intent.getStringExtra("text4");
        String fees = intent.getStringExtra("text5");

        tv.setText(title);
        ed1.setText(fullname);
        ed2.setText(address);
        ed3.setText(contact);
        ed4.setText("Cons Fees:" + fees + "/-");

        dateButton.setOnClickListener(v -> datePickerDialog.show());
        timeButton.setOnClickListener(v -> timePickerDialog.show());

        btnBack.setOnClickListener(v -> startActivity(new Intent(BookAppointmentActivity.this, FindDoctorActivity.class)));

        list = new ArrayList<>();
        item = new HashMap<>();
        item.put("line1", fullname);
        item.put("line2", address);
        item.put("line3", contact);
        item.put("line4", fees);
        list.add(item);

        sa = new SimpleAdapter(
                this,
                list,
                R.layout.multi_lines_appointment,
                new String[]{"line1", "line2", "line3", "line4"},
                new int[]{R.id.line_a, R.id.line_b, R.id.line_c, R.id.line_e}
        );
        lst.setAdapter(sa);

        btnBook.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
            String username = sharedPreferences.getString("username", "");

            if (username.length() == 0) {
                Toast.makeText(getApplicationContext(), "Please login first", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dateButton.getText().toString().compareTo("Select Date") == 0) {
                Toast.makeText(getApplicationContext(), "Please select date", Toast.LENGTH_SHORT).show();
                return;
            }

            if (timeButton.getText().toString().compareTo("Select Time") == 0) {
                Toast.makeText(getApplicationContext(), "Please select time", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if there's already a booking for this doctor at the selected time
            firebaseHelper.getAppointments().whereEqualTo("doctorName", fullname)
                    .whereEqualTo("appointmentDate", dateButton.getText().toString())
                    .whereEqualTo("appointmentTime", timeButton.getText().toString())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            if (!task.getResult().isEmpty()) {
                                Toast.makeText(getApplicationContext(), "This time slot is already booked. Please select another time.", Toast.LENGTH_SHORT).show();
                            } else {
                                // No existing booking, proceed with new booking
                                firebaseHelper.addAppointment(
                                        username,
                                        fullname,
                                        address,
                                        contact,
                                        fees,
                                        dateButton.getText().toString(),
                                        timeButton.getText().toString(),
                                        new FirebaseHelper.OnTaskCompleteListener() {
                                            @Override
                                            public void onSuccess() {
                                                Toast.makeText(getApplicationContext(), "Your appointment booked successfully", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(BookAppointmentActivity.this, HomeActivity.class));
                                            }

                                            @Override
                                            public void onFailure(Exception e) {
                                                Toast.makeText(getApplicationContext(), "Failed to book appointment", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                );
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Error checking appointment availability", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                dateButton.setText(day + "/" + month + "/" + year);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_DARK;
        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis() + 86400000);
    }

    private void initTimePicker() {
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                timeButton.setText(String.format("%02d:%02d", hour, minute));
            }
        };

        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);

        int style = AlertDialog.THEME_HOLO_DARK;
        timePickerDialog = new TimePickerDialog(this, style, timeSetListener, hour, minute, true);
    }
}