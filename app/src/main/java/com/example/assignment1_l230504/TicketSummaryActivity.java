package com.example.assignment1_l230504;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class TicketSummaryActivity extends AppCompatActivity {

    TextView tvMovieName, tvMovieDetails, tvTheater, tvHallDate, tvTime, tvTickets, tvSnacks, tvTotal;
    Button btnSendTicket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_summary);

        initViews();

        String movieName = getIntent().getStringExtra("MOVIE_NAME");
        ArrayList<Integer> seats = getIntent().getIntegerArrayListExtra("SEATS");
        double snacksTotal = getIntent().getDoubleExtra("SNACKS_TOTAL", 0);
        int pricePerSeat = getIntent().getIntExtra("TICKET_PRICE_PER_SEAT", 16);
        ArrayList<SnackItem> selectedSnacks = (ArrayList<SnackItem>) getIntent().getSerializableExtra("SELECTED_SNACKS");

        if (selectedSnacks == null) {
            selectedSnacks = new ArrayList<>();
        }

        String screenType = getIntent().getStringExtra("SCREEN_TYPE");
        if (screenType == null) screenType = "ScreenX • Dolby Atmos";

        String theater = getIntent().getStringExtra("THEATER");
        if (theater == null) theater = "Stars (90°Mall)";

        String hall = getIntent().getStringExtra("HALL");
        if (hall == null) hall = "1st";

        String date = getIntent().getStringExtra("DATE");
        if (date == null) date = "13.04.2025";

        String time = getIntent().getStringExtra("TIME");
        if (time == null) time = "22:15";

        int seatCount = seats != null ? seats.size() : 0;
        double ticketPriceTotal = seatCount * pricePerSeat;
        double finalTotal = ticketPriceTotal + snacksTotal;

        String seatDisplay = formatSeats(seats);
        String ticketsDisplay = formatTickets(seats, pricePerSeat);
        String snacksDisplay = formatSnacks(selectedSnacks);

        tvMovieName.setText(movieName != null ? movieName : "Oppenheimer");
        tvMovieDetails.setText(screenType);
        tvTheater.setText(theater);
        tvHallDate.setText("Hall: " + hall + "    Date: " + date);
        tvTime.setText("Time: " + time);
        tvTickets.setText(ticketsDisplay);
        tvSnacks.setText(snacksDisplay);
        tvTotal.setText(String.format("TOTAL: $%.2f", finalTotal));


        saveBookingToSharedPreferences(movieName, seatCount, finalTotal);

        final String finalMovieName = movieName;
        final ArrayList<Integer> finalSeats = seats;
        final double finalFinalTotal = finalTotal;
        final String finalSnacksDisplay = snacksDisplay;
        final String finalScreenType = screenType;
        final String finalTheater = theater;
        final String finalHall = hall;
        final String finalDate = date;
        final String finalTime = time;
        final int finalPricePerSeat = pricePerSeat;
        final ArrayList<SnackItem> finalSelectedSnacks = selectedSnacks;

        btnSendTicket.setOnClickListener(v -> {
            shareTicket(finalMovieName, finalSeats, finalFinalTotal,
                    finalSnacksDisplay, finalScreenType, finalTheater,
                    finalHall, finalDate, finalTime, finalPricePerSeat, finalSelectedSnacks);
        });
    }

    private void saveBookingToSharedPreferences(String movieName, int seatCount, double totalPrice) {
        SharedPreferences prefs = getSharedPreferences("BookingPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("LAST_MOVIE", movieName);
        editor.putInt("LAST_SEAT_COUNT", seatCount);
        editor.putFloat("LAST_TOTAL_PRICE", (float) totalPrice);
        editor.apply();

        Toast.makeText(this, "Booking Confirmed! Details saved.", Toast.LENGTH_SHORT).show();
    }

    private void initViews() {
        tvMovieName = findViewById(R.id.tvMovieName);
        tvMovieDetails = findViewById(R.id.tvMovieDetails);
        tvTheater = findViewById(R.id.tvTheater);
        tvHallDate = findViewById(R.id.tvHallDate);
        tvTime = findViewById(R.id.tvTime);
        tvTickets = findViewById(R.id.tvTickets);
        tvSnacks = findViewById(R.id.tvSnacks);
        tvTotal = findViewById(R.id.tvTotal);
        btnSendTicket = findViewById(R.id.btnSendTicket);
    }

    private String formatSeats(ArrayList<Integer> seats) {
        if (seats == null || seats.isEmpty()) {
            return "No seats selected";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < seats.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(seats.get(i));
        }
        return sb.toString();
    }

    private String formatTickets(ArrayList<Integer> seats, int pricePerSeat) {
        if (seats == null || seats.isEmpty()) {
            return "• No tickets selected";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < seats.size(); i++) {
            sb.append("• Row E, Seat ").append(seats.get(i))
                    .append(": **$").append(String.format("%.2f", (double)pricePerSeat)).append("**");
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
                    .append(" (").append(snack.getDescription()).append(")")
                    .append(": **$").append(String.format("%.2f", snack.getTotalPrice())).append("**");
            if (i < snacks.size() - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    private void shareTicket(String movie, ArrayList<Integer> seats, double total,
                             String snacksDisplay, String screenType, String theater,
                             String hall, String date, String time, int pricePerSeat,
                             ArrayList<SnackItem> selectedSnacks) {

        String ticketsDetail = formatTickets(seats, pricePerSeat);
        String snacksDetail = formatSnacks(selectedSnacks);

        String message =
                " *BOOKING DETAILS*\n\n" +
                        "*" + (movie != null ? movie : "Oppenheimer") + "*\n" +
                        "• " + screenType + "\n\n" +
                        "**Theater**\n" +
                        theater + "\n\n" +
                        "Hall: " + hall + "    Date: " + date + "\n" +
                        "Time: " + time + "\n\n" +
                        "**Tickets**\n" +
                        ticketsDetail + "\n\n" +
                        "**Snacks**\n" +
                        snacksDetail + "\n\n" +
                        "**TOTAL**\n" +
                        String.format("**$%.2f**\n\n", total) +
                        "Enjoy your movie!";

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, message);
        intent.putExtra(Intent.EXTRA_SUBJECT, "My Movie Ticket - " + (movie != null ? movie : "Movie"));

        Intent chooser = Intent.createChooser(intent, "Share Ticket via");

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(chooser);
        } else {
            Toast.makeText(this, "No apps available to share", Toast.LENGTH_SHORT).show();
        }
    }
}