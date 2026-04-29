package com.example.assignment1_l230504;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Map;

public class MyBookingsFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView tvNoBookings;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_bookings, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewBookings);
        tvNoBookings = view.findViewById(R.id.tvNoBookings);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadBookings();

        return view;
    }

    private void loadBookings() {
        sharedPreferences = getActivity().getSharedPreferences("BookingPrefs", Context.MODE_PRIVATE);

        // Get all saved bookings
        // For now, show last booking
        String movieName = sharedPreferences.getString("LAST_MOVIE", "");
        int seatCount = sharedPreferences.getInt("LAST_SEAT_COUNT", 0);
        float totalPrice = sharedPreferences.getFloat("LAST_TOTAL_PRICE", 0);

        ArrayList<BookingItem> bookings = new ArrayList<>();

        if (!movieName.isEmpty()) {
            bookings.add(new BookingItem(movieName, seatCount, totalPrice));
            tvNoBookings.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            BookingAdapter adapter = new BookingAdapter(getContext(), bookings);
            recyclerView.setAdapter(adapter);
        } else {
            tvNoBookings.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    // Inner class for Booking Item
    public static class BookingItem {
        String movieName;
        int seatCount;
        float totalPrice;

        public BookingItem(String movieName, int seatCount, float totalPrice) {
            this.movieName = movieName;
            this.seatCount = seatCount;
            this.totalPrice = totalPrice;
        }

        public String getMovieName() { return movieName; }
        public int getSeatCount() { return seatCount; }
        public float getTotalPrice() { return totalPrice; }
    }
}