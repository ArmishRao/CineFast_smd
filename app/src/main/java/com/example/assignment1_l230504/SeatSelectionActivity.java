package com.example.assignment1_l230504;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class SeatSelectionActivity extends AppCompatActivity {

    GridLayout gridLeft, gridRight;
    Button btnBookSeats, btnProceedSnacks, btnComingSoon, btnWatchTrailer;
    TextView tvMovieName, tvTotal;
    LinearLayout buttonsContainer;

    ArrayList<Integer> selectedSeats = new ArrayList<>();
    ArrayList<Integer> bookedSeats = new ArrayList<>();
    String movie;
    boolean isComingSoon = false;
    String trailerURL = "";

    int pricePerSeat = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_selection);

        gridLeft = findViewById(R.id.gridLeft);
        gridRight = findViewById(R.id.gridRight);
        btnBookSeats = findViewById(R.id.btnBookSeats);
        btnProceedSnacks = findViewById(R.id.btnProceedSnacks);
        tvMovieName = findViewById(R.id.tvMovieName);
        tvTotal = findViewById(R.id.tvTotal);
        buttonsContainer = findViewById(R.id.buttonsContainer);

        movie = getIntent().getStringExtra("MOVIE_NAME");
        isComingSoon = getIntent().getBooleanExtra("IS_COMING_SOON", false);
        trailerURL = getIntent().getStringExtra("TRAILER_URL");

        tvMovieName.setText(movie);

        if (isComingSoon) {
            setupComingSoonMode();
        } else {
            setupNowShowingMode();
        }

        updateTotal();
    }

    private void setupNowShowingMode() {
        btnBookSeats.setVisibility(View.VISIBLE);
        btnProceedSnacks.setVisibility(View.VISIBLE);

        if (btnComingSoon != null) btnComingSoon.setVisibility(View.GONE);
        if (btnWatchTrailer != null) btnWatchTrailer.setVisibility(View.GONE);

        bookedSeats.add(2);
        bookedSeats.add(5);
        bookedSeats.add(9);
        bookedSeats.add(14);

        createSeats(gridLeft, 16, 0);
        createSeats(gridRight, 16, 100);

        btnBookSeats.setOnClickListener(v -> {
            if (selectedSeats.isEmpty()) {
                Toast.makeText(this, "Please select at least one seat", Toast.LENGTH_SHORT).show();
            } else {
                showBookingConfirmation();
            }
        });

        btnProceedSnacks.setOnClickListener(v -> {
            if (selectedSeats.isEmpty()) {
                Toast.makeText(this, "Please select at least one seat", Toast.LENGTH_SHORT).show();
            } else {
                goToSnacks();
            }
        });
    }

    private void setupComingSoonMode() {

        btnBookSeats.setVisibility(View.GONE);
        btnProceedSnacks.setVisibility(View.GONE);

        if (btnComingSoon == null) {
            btnComingSoon = new Button(this);
            btnWatchTrailer = new Button(this);

            btnComingSoon.setText("Coming Soon");
            btnComingSoon.setEnabled(false);
            btnComingSoon.setBackgroundColor(ContextCompat.getColor(this, android.R.color.darker_gray));
            btnComingSoon.setTextColor(Color.WHITE);

            btnWatchTrailer.setText("Watch Trailer");
            btnWatchTrailer.setBackgroundColor(ContextCompat.getColor(this, R.color.accent_red));
            btnWatchTrailer.setTextColor(Color.WHITE);

            buttonsContainer.removeAllViews();

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1
            );
            params.setMargins(0, 0, 16, 0);

            btnComingSoon.setLayoutParams(params);
            btnWatchTrailer.setLayoutParams(params);

            buttonsContainer.addView(btnComingSoon);
            buttonsContainer.addView(btnWatchTrailer);
        }

        btnComingSoon.setVisibility(View.VISIBLE);
        btnWatchTrailer.setVisibility(View.VISIBLE);

        createDisabledSeats(gridLeft, 16, 0);
        createDisabledSeats(gridRight, 16, 100);

        tvTotal.setText("Coming Soon! Seats not available yet");
        tvTotal.setTextColor(Color.YELLOW);

        btnWatchTrailer.setOnClickListener(v -> {
            if (trailerURL != null && !trailerURL.isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailerURL));
                startActivity(intent);
            } else {
                Toast.makeText(this, "Trailer not available", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createSeats(GridLayout grid, int count, int offset) {
        grid.removeAllViews();
        for (int i = 0; i < count; i++) {
            Button seat = new Button(this);
            int seatId = i + offset;
            seat.setTag(seatId);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 90;
            params.height = 90;
            params.setMargins(10, 10, 10, 10);
            seat.setLayoutParams(params);

            seat.setText("");

            if (bookedSeats.contains(seatId)) {
                seat.setBackgroundColor(Color.RED);
                seat.setEnabled(false);
            } else {
                seat.setBackgroundColor(Color.DKGRAY);
                seat.setOnClickListener(this::toggleSeat);
            }

            grid.addView(seat);
        }
    }

    private void createDisabledSeats(GridLayout grid, int count, int offset) {
        grid.removeAllViews();
        for (int i = 0; i < count; i++) {
            Button seat = new Button(this);
            int seatId = i + offset;
            seat.setTag(seatId);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 90;
            params.height = 90;
            params.setMargins(10, 10, 10, 10);
            seat.setLayoutParams(params);

            seat.setText("");
            seat.setBackgroundColor(Color.DKGRAY);
            seat.setEnabled(false);
            seat.setAlpha(0.5f);

            grid.addView(seat);
        }
    }

    private void toggleSeat(View view) {
        Button seat = (Button) view;
        int id = (int) seat.getTag();

        if (selectedSeats.contains(id)) {
            selectedSeats.remove(Integer.valueOf(id));
            seat.setBackgroundColor(Color.DKGRAY);
        } else {
            selectedSeats.add(id);
            seat.setBackgroundColor(Color.GREEN);
        }

        btnProceedSnacks.setEnabled(!selectedSeats.isEmpty());
        btnBookSeats.setEnabled(!selectedSeats.isEmpty());
        updateTotal();
    }

    private void updateTotal() {
        int count = selectedSeats.size();
        int total = count * pricePerSeat;
        if (!isComingSoon) {
            tvTotal.setText("Selected: " + count + " | Total: Rs " + total);
        }
    }

    private void showBookingConfirmation() {
        Toast.makeText(this, "Booking Confirmed!", Toast.LENGTH_SHORT).show();

        bookedSeats.addAll(selectedSeats);

        Intent i = new Intent(this, TicketSummaryActivity.class);
        i.putIntegerArrayListExtra("SEATS", selectedSeats);
        i.putExtra("TOTAL", selectedSeats.size() * pricePerSeat);
        i.putExtra("MOVIE_NAME", movie);
        startActivity(i);
        finish();
    }

    private void goToSnacks() {
        bookedSeats.addAll(selectedSeats);

        Intent i = new Intent(this, SnacksActivity.class);
        i.putIntegerArrayListExtra("SEATS", selectedSeats);
        i.putExtra("MOVIE_NAME", movie);
        startActivity(i);
    }
}