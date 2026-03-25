package com.example.assignment1_l230504;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SnacksActivity extends AppCompatActivity {

    ListView listViewSnacks;
    Button btnConfirm;
    SnackAdapter snackAdapter;
    ArrayList<Snack> snacks;
    ArrayList<Integer> seats;
    String movieName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snack);

        listViewSnacks = findViewById(R.id.listViewSnacks);
        btnConfirm = findViewById(R.id.confirm);

        // Get data from intent
        seats = getIntent().getIntegerArrayListExtra("SEATS");
        movieName = getIntent().getStringExtra("MOVIE_NAME");

        if (seats == null || seats.isEmpty()) {
            Toast.makeText(this, "No seats received!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        if (movieName == null || movieName.isEmpty()) {
            Toast.makeText(this, "No movie name received!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Initialize snacks with images, names, descriptions, and prices
        snacks = new ArrayList<>();
        snacks.add(new Snack(R.drawable.popcorn, "Popcorn", "Large/Buttered", 8.99));
        snacks.add(new Snack(R.drawable.nachos, "Nachos", "with Cheese dip", 7.99));
        snacks.add(new Snack(R.drawable.softdrink, "Soft Drink", "Large/Any Flavor", 5.99));
        snacks.add(new Snack(R.drawable.hotdog, "Hot Dog", "with Ketchup & Mustard", 6.99)); // 4th snack item

        // Setup adapter
        snackAdapter = new SnackAdapter(this, snacks, this::updateTotal);
        listViewSnacks.setAdapter(snackAdapter);

        // Confirm button click listener
        btnConfirm.setOnClickListener(v -> {
            try {
                double totalSnackPrice = calculateTotalSnackPrice();
                ArrayList<SnackItem> selectedSnacks = getSelectedSnacks();

                android.util.Log.d("SnacksActivity", "Movie: " + movieName);
                android.util.Log.d("SnacksActivity", "Seats: " + seats.toString());
                android.util.Log.d("SnacksActivity", "Snacks Total: $" + totalSnackPrice);
                android.util.Log.d("SnacksActivity", "Selected Snacks: " + selectedSnacks.size());

                Intent intent = new Intent(SnacksActivity.this, TicketSummaryActivity.class);
                intent.putExtra("MOVIE_NAME", movieName);
                intent.putIntegerArrayListExtra("SEATS", seats);
                intent.putExtra("SNACKS_TOTAL", totalSnackPrice);
                intent.putExtra("TICKET_PRICE_PER_SEAT", 16);
                intent.putExtra("SCREEN_TYPE", "ScreenX • Dolby Atmos");
                intent.putExtra("THEATER", "Stars (90°Mall)");
                intent.putExtra("HALL", "1st");
                intent.putExtra("DATE", "13.04.2025");
                intent.putExtra("TIME", "22:15");
                intent.putExtra("SELECTED_SNACKS", selectedSnacks);

                startActivity(intent);

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private double calculateTotalSnackPrice() {
        double total = 0;
        for (Snack snack : snacks) {
            total += snack.getTotalPrice();
        }
        return total;
    }

    private ArrayList<SnackItem> getSelectedSnacks() {
        ArrayList<SnackItem> selectedSnacks = new ArrayList<>();
        for (Snack snack : snacks) {
            if (snack.getQuantity() > 0) {
                selectedSnacks.add(new SnackItem(
                        snack.getName(),
                        snack.getDescription(),
                        snack.getPrice(),
                        snack.getQuantity()
                ));
            }
        }
        return selectedSnacks;
    }

    private void updateTotal() {
        double total = calculateTotalSnackPrice();
        Toast.makeText(this, "Total: $" + String.format("%.2f", total), Toast.LENGTH_SHORT).show();
    }
}