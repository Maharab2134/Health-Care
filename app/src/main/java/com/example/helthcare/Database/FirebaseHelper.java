package com.example.helthcare.Database;

import android.content.Context;
import android.widget.Toast;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

import com.example.helthcare.Model.Order;

public class FirebaseHelper {
    private static final String TAG = "FirebaseHelper";
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Context context;

    public FirebaseHelper(Context context) {
        this.context = context;
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public void register(String username, String email, String password, OnCompleteListener<AuthResult> listener) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Create user document in Firestore
                        Map<String, Object> user = new HashMap<>();
                        user.put("username", username);
                        user.put("email", email);
                        
                        db.collection("users")
                                .document(mAuth.getCurrentUser().getUid())
                                .set(user)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        Toast.makeText(context, "Registration successful", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, "Failed to create user profile", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                    listener.onComplete(task);
                });
    }

    public void login(String email, String password, OnCompleteListener<AuthResult> listener) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(listener);
    }

    public void addCart(String username, String product, float price, String otype) {
        Map<String, Object> cartItem = new HashMap<>();
        cartItem.put("username", username);
        cartItem.put("product", product);
        cartItem.put("price", price);
        cartItem.put("otype", otype);

        db.collection("cart")
                .add(cartItem)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Failed to add to cart", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void checkCart(String username, String product, OnCompleteListener<QuerySnapshot> listener) {
        db.collection("cart")
                .whereEqualTo("username", username)
                .whereEqualTo("product", product)
                .get()
                .addOnCompleteListener(listener);
    }

    public void removeCart(String username, String otype) {
        db.collection("cart")
                .whereEqualTo("username", username)
                .whereEqualTo("otype", otype)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            document.getReference().delete();
                        }
                    }
                });
    }

    public void getCartData(String username, String otype, OnCompleteListener<QuerySnapshot> listener) {
        db.collection("cart")
                .whereEqualTo("username", username)
                .whereEqualTo("otype", otype)
                .get()
                .addOnCompleteListener(listener);
    }

    public void addOrder(String username, String fullname, String address, String contact, int pincode,
                        String date, String time, float price, String otype) {
        Map<String, Object> order = new HashMap<>();
        order.put("username", username);
        order.put("fullname", fullname);
        order.put("address", address);
        order.put("contact", contact);
        order.put("pincode", pincode);
        order.put("date", date);
        order.put("time", time);
        order.put("price", price);
        order.put("otype", otype);

        db.collection("orders")
                .add(order)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(context, "Order placed successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Failed to place order", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void getOrderData(String username, OnCompleteListener<QuerySnapshot> listener) {
        db.collection("orders")
                .whereEqualTo("username", username)
                .get()
                .addOnCompleteListener(listener);
    }

    public void checkAppointmentExists(String username, String fullname, String address,
                                     String contact, String date, String time,
                                     OnCompleteListener<QuerySnapshot> listener) {
        db.collection("orders")
                .whereEqualTo("username", username)
                .whereEqualTo("fullname", fullname)
                .whereEqualTo("address", address)
                .whereEqualTo("contact", contact)
                .whereEqualTo("date", date)
                .whereEqualTo("time", time)
                .whereEqualTo("otype", "appointment")
                .get()
                .addOnCompleteListener(listener);
    }

    // Get appointments collection reference
    public CollectionReference getAppointments() {
        return db.collection("appointments");
    }

    // Add appointment to Firestore
    public void addAppointment(String username, String doctorName, String address, String contact, 
                             String fees, String date, String time, OnTaskCompleteListener listener) {
        Appointment appointment = new Appointment(username, doctorName, address, contact, fees, date, time);
        
        db.collection("appointments")
            .add(appointment)
            .addOnSuccessListener(documentReference -> {
                if (listener != null) {
                    listener.onSuccess();
                }
            })
            .addOnFailureListener(e -> {
                if (listener != null) {
                    listener.onFailure(e);
                }
            });
    }

    // Interface for task completion callbacks
    public interface OnTaskCompleteListener {
        void onSuccess();
        void onFailure(Exception e);
    }

    // Appointment data class
    public static class Appointment {
        private String username;
        private String doctorName;
        private String address;
        private String contact;
        private String fees;
        private String appointmentDate;
        private String appointmentTime;

        public Appointment() {
            // Required empty constructor for Firestorm
        }

        public Appointment(String username, String doctorName, String address, String contact, 
                         String fees, String appointmentDate, String appointmentTime) {
            this.username = username;
            this.doctorName = doctorName;
            this.address = address;
            this.contact = contact;
            this.fees = fees;
            this.appointmentDate = appointmentDate;
            this.appointmentTime = appointmentTime;
        }

        // Getters and setters
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getDoctorName() { return doctorName; }
        public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }

        public String getContact() { return contact; }
        public void setContact(String contact) { this.contact = contact; }

        public String getFees() { return fees; }
        public void setFees(String fees) { this.fees = fees; }

        public String getAppointmentDate() { return appointmentDate; }
        public void setAppointmentDate(String appointmentDate) { this.appointmentDate = appointmentDate; }

        public String getAppointmentTime() { return appointmentTime; }
        public void setAppointmentTime(String appointmentTime) { this.appointmentTime = appointmentTime; }
    }

    public void getUserData(String email, OnDataLoadedListener listener) {
        db.collection("users")
            .whereEqualTo("email", email)
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    listener.onDataLoaded(task);
                } else {
                    Log.e(TAG, "Error getting user data: ", task.getException());
                    listener.onDataLoaded(null);
                }
            });
    }

    public void getRecentOrders(String userId, OnOrdersFetchedListener listener) {
        db.collection("orders")
            .whereEqualTo("userId", userId)
            .orderBy("orderDate", Query.Direction.DESCENDING)
            .limit(5)  // Get only the 5 most recent orders
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    List<Order> orders = new ArrayList<>();
                    task.getResult().getDocuments().forEach(document -> {
                        Order order = new Order();
                        order.setOrderId(document.getId());
                        order.setOrderDate(document.getDate("orderDate"));
                        order.setTotalAmount(document.getDouble("totalAmount"));
                        order.setStatus(document.getString("status"));
                        order.setUserId(document.getString("userId"));
                        orders.add(order);
                    });
                    if (listener != null) {
                        listener.onOrdersFetched(orders);
                    }
                } else {
                    Toast.makeText(context, "Error loading orders: " + task.getException().getMessage(), 
                        Toast.LENGTH_SHORT).show();
                }
            });
    }

    public interface OnDataLoadedListener {
        void onDataLoaded(com.google.android.gms.tasks.Task<QuerySnapshot> task);
    }

    public interface OnOrdersFetchedListener {
        void onOrdersFetched(List<Order> orders);
    }

    public void updateUserProfile(String email, String name, String phone, String address, OnProfileUpdateListener listener) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("name", name);
        updates.put("phone", phone);
        updates.put("address", address);

        db.collection("users")
            .whereEqualTo("email", email)
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful() && !task.getResult().isEmpty()) {
                    String docId = task.getResult().getDocuments().get(0).getId();
                    db.collection("users")
                        .document(docId)
                        .update(updates)
                        .addOnSuccessListener(aVoid -> {
                            Log.d(TAG, "Profile updated successfully");
                            listener.onProfileUpdate(true);
                        })
                        .addOnFailureListener(e -> {
                            Log.e(TAG, "Error updating profile: ", e);
                            listener.onProfileUpdate(false);
                        });
                } else {
                    Log.e(TAG, "Error finding user document: ", task.getException());
                    listener.onProfileUpdate(false);
                }
            });
    }

    public interface OnProfileUpdateListener {
        void onProfileUpdate(boolean success);
    }
} 