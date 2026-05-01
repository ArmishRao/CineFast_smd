package com.example.assignment1_l230504;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class TicketSummaryFragment extends Fragment {

    private TextView tvMovieName, tvMovieDetails, tvTheater, tvHallDate, tvTime, tvTickets, tvSnacks, tvTotal;
    private Button btnSendTicket;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    // Booking data
    private String movieName;
    private ArrayList<Integer> seats;
    private double snacksTotal;
    private int pricePerSeat;
    private ArrayList<SnackItem> selectedSnacks;
    private String screenType, theater, hall, date, time;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ticket_summary, container, false);

        initViews(view);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Get data from arguments
        getBookingData();

        // Display booking details
        displayBookingDetails();

        btnSendTicket.setOnClickListener(v -> {
            saveBookingToFirebase();
            shareTicket();
        });

        return view;
    }

    private void initViews(View view) {
        tvMovieName = view.findViewById(R.id.tvMovieName);
        tvMovieDetails = view.findViewById(R.id.tvMovieDetails);
        tvTheater = view.findViewById(R.id.tvTheater);
        tvHallDate = view.findViewById(R.id.tvHallDate);
        tvTime = view.findViewById(R.id.tvTime);
        tvTickets = view.findViewById(R.id.tvTickets);
        tvSnacks = view.findViewById(R.id.tvSnacks);
        tvTotal = view.findViewById(R.id.tvTotal);
        btnSendTicket = view.findViewById(R.id.btnSendTicket);
    }

    private void getBookingData() {
        if (getArguments() != null) {
            movieName = getArguments().getString("MOVIE_NAME");
            seats = getArguments().getIntegerArrayList("SEATS");
            snacksTotal = getArguments().getDouble("SNACKS_TOTAL", 0);
            pricePerSeat = getArguments().getInt("TICKET_PRICE_PER_SEAT", 16);
            selectedSnacks = (ArrayList<SnackItem>) getArguments().getSerializable("SELECTED_SNACKS");
            screenType = getArguments().getString("SCREEN_TYPE");
            theater = getArguments().getString("THEATER");
            hall = getArguments().getString("HALL");
            date = getArguments().getString("DATE");
            time = getArguments().getString("TIME");
        }

        // Set default values if null
        if (selectedSnacks == null) selectedSnacks = new ArrayList<>();
        if (screenType == null) screenType = "ScreenX • Dolby Atmos";
        if (theater == null) theater = "Stars (90°Mall)";
        if (hall == null) hall = "1st";
        if (date == null) date = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date());
        if (time == null) time = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
    }

    private void displayBookingDetails() {
        int seatCount = seats != null ? seats.size() : 0;
        double ticketPriceTotal = seatCount * pricePerSeat;
        double finalTotal = ticketPriceTotal + snacksTotal;

        tvMovieName.setText(movieName != null ? movieName : "Unknown Movie");
        tvMovieDetails.setText(screenType);
        tvTheater.setText(theater);
        tvHallDate.setText("Hall: " + hall + "    Date: " + date);
        tvTime.setText("Time: " + time);
        tvTickets.setText(formatTickets(seats, pricePerSeat));
        tvSnacks.setText(formatSnacks(selectedSnacks));
        tvTotal.setText(String.format("TOTAL: $%.2f", finalTotal));
    }

    private String formatTickets(ArrayList<Integer> seats, int pricePerSeat) {
        if (seats == null || seats.isEmpty()) {
            return "• No tickets selected";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < seats.size(); i++) {
            sb.append("• Row E, Seat ").append(seats.get(i))
                    .append(": $").append(String.format("%.2f", (double)pricePerSeat));
            if (i < seats.size() - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    private String formatSnacks(ArrayList<SnackItem> snacks) {
        if (snacks == null || snacks.isEmpty()) {
            return "• No snacks selected";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < snacks.size(); i++) {
            SnackItem snack = snacks.get(i);
            sb.append("• X").append(snack.getQuantity())
                    .append(" ").append(snack.getName())
                    .append(": $").append(String.format("%.2f", snack.getTotalPrice()));
            if (i < snacks.size() - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    private void saveBookingToFirebase() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(getContext(), "User not logged in! Booking not saved.", Toast.LENGTH_LONG).show();
            return;
        }

        String userId = currentUser.getUid();
        String bookingId = UUID.randomUUID().toString();

        int seatCount = seats != null ? seats.size() : 0;
        double ticketPriceTotal = seatCount * pricePerSeat;
        double finalTotal = ticketPriceTotal + snacksTotal;

        // Create booking object
        booking booking = new booking(
                bookingId, userId, movieName, seats,
                finalTotal, date, time, theater,
                hall, screenType, selectedSnacks
        );

        // Save to Firebase under "bookings/{userId}/{bookingId}"
        databaseReference.child("bookings").child(userId).child(bookingId)
                .setValue(booking.toMap())
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Booking saved to cloud!", Toast.LENGTH_SHORT).show();
                    saveToSharedPreferences();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to save: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    private void saveToSharedPreferences() {
        SharedPreferences prefs = getActivity().getSharedPreferences("BookingPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("LAST_MOVIE", movieName);
        editor.putInt("LAST_SEAT_COUNT", seats != null ? seats.size() : 0);

        int seatCount = seats != null ? seats.size() : 0;
        double ticketPriceTotal = seatCount * pricePerSeat;
        double finalTotal = ticketPriceTotal + snacksTotal;
        editor.putFloat("LAST_TOTAL_PRICE", (float) finalTotal);
        editor.apply();
    }

    private void shareTicket() {
        String ticketsDetail = formatTickets(seats, pricePerSeat);
        String snacksDetail = formatSnacks(selectedSnacks);

        int seatCount = seats != null ? seats.size() : 0;
        double ticketPriceTotal = seatCount * pricePerSeat;
        double finalTotal = ticketPriceTotal + snacksTotal;

        String message =
                "🎬 *BOOKING DETAILS* 🎬\n\n" +
                        "*" + (movieName != null ? movieName : "Movie") + "*\n" +
                        "• " + screenType + "\n\n" +
                        "🏢 *Theater*\n" +
                        theater + "\n\n" +
                        "Hall: " + hall + "    Date: " + date + "\n" +
                        "Time: " + time + "\n\n" +
                        "🎟️ *Tickets*\n" +
                        ticketsDetail + "\n\n" +
                        "🍿 *Snacks*\n" +
                        snacksDetail + "\n\n" +
                        "💰 *TOTAL*\n" +
                        String.format("**$%.2f**\n\n", finalTotal) +
                        "✨ Enjoy your movie! ✨";

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, message);
        intent.putExtra(Intent.EXTRA_SUBJECT, "My Movie Ticket - " + (movieName != null ? movieName : "Movie"));

        Intent chooser = Intent.createChooser(intent, "Share Ticket via");

        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(chooser);
        } else {
            Toast.makeText(getContext(), "No apps available to share", Toast.LENGTH_SHORT).show();
        }
    }
}