package com.example.assignment1_l230504;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MyBookingsFragment extends Fragment {

    private static final String TAG = "MyBookingsFragment";

    private RecyclerView recyclerViewBookings;
    private ProgressBar progressBar;
    private TextView tvNoBookings;

    private BookingAdapter bookingAdapter;
    private ArrayList<BookingItem> bookingList;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private ValueEventListener bookingListener; // Store reference to remove listener

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_bookings, container, false);

        recyclerViewBookings = view.findViewById(R.id.recyclerViewBookings);
        progressBar = view.findViewById(R.id.progressBar);
        tvNoBookings = view.findViewById(R.id.tvNoBookings);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        setupRecyclerView();
        fetchBookingsFromFirebase();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Remove listener to prevent memory leaks
        if (bookingListener != null && databaseReference != null) {
            databaseReference.child("bookings").child(getUserId()).removeEventListener(bookingListener);
        }
    }

    private String getUserId() {
        FirebaseUser user = mAuth.getCurrentUser();
        return user != null ? user.getUid() : "";
    }

    private void setupRecyclerView() {
        bookingList = new ArrayList<>();
        bookingAdapter = new BookingAdapter(getContext(), bookingList);
        bookingAdapter.setOnCancelClickListener(this::cancelBooking);
        recyclerViewBookings.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewBookings.setAdapter(bookingAdapter);
    }

    private void fetchBookingsFromFirebase() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(getContext(), "Please login to view bookings", Toast.LENGTH_SHORT).show();
            showNoBookings(true);
            return;
        }

        String userId = currentUser.getUid();
        Log.d(TAG, "Fetching bookings for user: " + userId);

        progressBar.setVisibility(View.VISIBLE);
        recyclerViewBookings.setVisibility(View.GONE);
        tvNoBookings.setVisibility(View.GONE);

        // IMPORTANT: Use addListenerForSingleValueEvent instead of addValueEventListener
        bookingListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange called, children count: " + dataSnapshot.getChildrenCount());
                bookingList.clear();

                if (dataSnapshot.exists()) {
                    for (DataSnapshot bookingSnapshot : dataSnapshot.getChildren()) {
                        String bookingId = bookingSnapshot.getKey();
                        Log.d(TAG, "Processing booking ID: " + bookingId);

                        BookingItem booking = new BookingItem();

                        booking.setBookingId(bookingId);
                        booking.setMovieName(bookingSnapshot.child("movieName").getValue(String.class));
                        booking.setDate(bookingSnapshot.child("date").getValue(String.class));
                        booking.setTime(bookingSnapshot.child("time").getValue(String.class));

                        Double totalPrice = bookingSnapshot.child("totalPrice").getValue(Double.class);
                        booking.setTotalPrice(totalPrice != null ? totalPrice : 0.0);

                        // Get seats count
                        DataSnapshot seatsSnapshot = bookingSnapshot.child("seats");
                        int seatCount = 0;
                        if (seatsSnapshot.exists()) {
                            seatCount = (int) seatsSnapshot.getChildrenCount();
                        }
                        booking.setTicketCount(seatCount);

                        booking.setImageResId(getImageForMovie(booking.getMovieName()));
                        booking.setCancellable(isBookingCancellable(booking.getDate(), booking.getTime()));

                        // Only add if not already in list (check by bookingId)
                        boolean exists = false;
                        for (BookingItem existing : bookingList) {
                            if (existing.getBookingId() != null && existing.getBookingId().equals(bookingId)) {
                                exists = true;
                                break;
                            }
                        }
                        if (!exists) {
                            bookingList.add(booking);
                            Log.d(TAG, "Added booking: " + booking.getMovieName() + " (ID: " + bookingId + ")");
                        }
                    }
                }

                updateUI();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled: " + error.getMessage());
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Failed to load bookings: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        };

        databaseReference.child("bookings").child(userId).addListenerForSingleValueEvent(bookingListener);
    }

    private void updateUI() {
        Log.d(TAG, "updateUI called, bookingList size: " + bookingList.size());
        if (bookingList.isEmpty()) {
            showNoBookings(true);
            recyclerViewBookings.setVisibility(View.GONE);
        } else {
            showNoBookings(false);
            recyclerViewBookings.setVisibility(View.VISIBLE);
            bookingAdapter.notifyDataSetChanged();
        }
    }

    private void showNoBookings(boolean show) {
        if (show) {
            tvNoBookings.setVisibility(View.VISIBLE);
            recyclerViewBookings.setVisibility(View.GONE);
        } else {
            tvNoBookings.setVisibility(View.GONE);
            recyclerViewBookings.setVisibility(View.VISIBLE);
        }
    }

    private int getImageForMovie(String movieName) {
        if (movieName == null) return R.drawable.interstellar;

        switch (movieName.toLowerCase()) {
            case "the dark knight":
                return R.drawable.dark;
            case "inception":
                return R.drawable.inception;
            case "interstellar":
                return R.drawable.interstellar;
            case "dune: part two":
                return R.drawable.dune;
            case "avatar 3":
                return R.drawable.avatar;
            case "deadpool 3":
                return R.drawable.deadpool;
            default:
                return R.drawable.interstellar;
        }
    }

    private boolean isBookingCancellable(String date, String time) {
        if (date == null || time == null) return false;

        try {
            String dateTimeString = date + " " + time;
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
            Date bookingDateTime = sdf.parse(dateTimeString);
            Date currentDateTime = new Date();
            return bookingDateTime != null && bookingDateTime.after(currentDateTime);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void cancelBooking(BookingItem booking, int position) {
        Log.d(TAG, "cancelBooking called for: " + booking.getMovieName() + " at position: " + position);

        if (!isBookingCancellable(booking.getDate(), booking.getTime())) {
            Toast.makeText(getContext(), "Cannot cancel past bookings!", Toast.LENGTH_LONG).show();
            return;
        }

        new AlertDialog.Builder(getContext())
                .setTitle("Cancel Booking")
                .setMessage("Are you sure you want to cancel this booking for " + booking.getMovieName() + "?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    deleteBookingFromFirebase(booking, position);
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void deleteBookingFromFirebase(BookingItem booking, int position) {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = currentUser.getUid();
        String bookingId = booking.getBookingId();

        Log.d(TAG, "Deleting booking - UserID: " + userId + ", BookingID: " + bookingId);

        progressBar.setVisibility(View.VISIBLE);

        // Delete specific booking by its ID
        databaseReference.child("bookings").child(userId).child(bookingId)
                .removeValue()
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Successfully deleted booking: " + bookingId);

                    // Remove from local list
                    bookingList.remove(position);
                    bookingAdapter.notifyItemRemoved(position);
                    updateUI();

                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Booking Cancelled Successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to delete: " + e.getMessage());
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Failed to cancel: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    public static class BookingItem {
        private String bookingId;
        private String movieName;
        private String date;
        private String time;
        private int ticketCount;
        private double totalPrice;
        private int imageResId;
        private boolean isCancellable;

        public String getBookingId() { return bookingId; }
        public void setBookingId(String bookingId) { this.bookingId = bookingId; }
        public String getMovieName() { return movieName; }
        public void setMovieName(String movieName) { this.movieName = movieName; }
        public String getDate() { return date; }
        public void setDate(String date) { this.date = date; }
        public String getTime() { return time; }
        public void setTime(String time) { this.time = time; }
        public int getTicketCount() { return ticketCount; }
        public void setTicketCount(int ticketCount) { this.ticketCount = ticketCount; }
        public double getTotalPrice() { return totalPrice; }
        public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
        public int getImageResId() { return imageResId; }
        public void setImageResId(int imageResId) { this.imageResId = imageResId; }
        public boolean isCancellable() { return isCancellable; }
        public void setCancellable(boolean cancellable) { isCancellable = cancellable; }
    }
}