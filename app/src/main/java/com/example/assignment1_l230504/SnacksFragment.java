package com.example.assignment1_l230504;

import android.content.Intent;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SnacksFragment extends Fragment {

    private RecyclerView recyclerViewSnacks;
    private Button btnConfirm;
    private TextView tvTotalAmount;
    private SnackAdapter snackAdapter;
    private ArrayList<Snack> snacks;
    private ArrayList<Integer> seats;
    private String movieName;
    private DatabaseHelper databaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_snacks, container, false);

        recyclerViewSnacks = view.findViewById(R.id.recyclerViewSnacks);
        btnConfirm = view.findViewById(R.id.btnConfirm);
        tvTotalAmount = view.findViewById(R.id.tvTotalAmount);

        // Get data from arguments
        if (getArguments() != null) {
            seats = getArguments().getIntegerArrayList("SEATS");
            movieName = getArguments().getString("MOVIE_NAME");
        }

        if (seats == null || seats.isEmpty()) {
            Toast.makeText(getContext(), "No seats received!", Toast.LENGTH_LONG).show();
            return view;
        }

        if (movieName == null || movieName.isEmpty()) {
            Toast.makeText(getContext(), "No movie name received!", Toast.LENGTH_LONG).show();
            return view;
        }

        // Initialize Database Helper
        databaseHelper = new DatabaseHelper(getContext());

        // Load snacks from database
        loadSnacksFromDatabase();

        // Setup RecyclerView
        recyclerViewSnacks.setLayoutManager(new LinearLayoutManager(getContext()));
        snackAdapter = new SnackAdapter(getContext(), snacks, this::updateTotalAmount);
        recyclerViewSnacks.setAdapter(snackAdapter);

        // Update total amount
        updateTotalAmount(0);

        btnConfirm.setOnClickListener(v -> {
            try {
                double totalSnackPrice = calculateTotalSnackPrice();
                ArrayList<SnackItem> selectedSnacks = getSelectedSnacks();

                // Navigate to TicketSummaryFragment instead of Activity
                TicketSummaryFragment ticketSummaryFragment = new TicketSummaryFragment();
                Bundle args = new Bundle();
                args.putString("MOVIE_NAME", movieName);
                args.putIntegerArrayList("SEATS", seats);
                args.putDouble("SNACKS_TOTAL", totalSnackPrice);
                args.putInt("TICKET_PRICE_PER_SEAT", 16);
                args.putString("SCREEN_TYPE", "ScreenX • Dolby Atmos");
                args.putString("THEATER", "Stars (90°Mall)");
                args.putString("HALL", "1st");
                args.putString("DATE", "13.05.2026");
                args.putString("TIME", "22:15");
                args.putSerializable("SELECTED_SNACKS", selectedSnacks);
                ticketSummaryFragment.setArguments(args);

                // Replace current fragment
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, ticketSummaryFragment)
                        .addToBackStack(null)
                        .commit();

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }

    private void loadSnacksFromDatabase() {
        // Load snacks from SQLite database
        snacks = databaseHelper.getAllSnacks();

        if (snacks.isEmpty()) {
            Toast.makeText(getContext(), "No snacks found in database!", Toast.LENGTH_SHORT).show();
            snacks = new ArrayList<>();
        }
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

    private void updateTotalAmount(double total) {
        total = calculateTotalSnackPrice();
        tvTotalAmount.setText("$" + String.format("%.2f", total));
    }
}