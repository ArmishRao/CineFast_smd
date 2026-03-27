//package com.example.assignment1_l230504;
//
//import android.os.Bundle;
//
//import androidx.fragment.app.Fragment;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import com.google.android.material.tabs.TabLayout;
//import com.google.android.material.tabs.TabLayoutMediator;
//
//import androidx.viewpager2.widget.ViewPager2;
//public class Home_Fragment extends Fragment {
//
//    TabLayout tabLayout;
//    ViewPager2 viewPage;
//
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_home_, container, false);
//
//        tabLayout = view.findViewById(R.id.tabLayout);
//        viewPage = view.findViewById(R.id.viewPager);
//
//        ViewPageAdapter adapter = new ViewPageAdapter(this);
//        viewPage.setAdapter(adapter);
//
//        new TabLayoutMediator(tabLayout, viewPage,
//                (tab, position) -> {
//                    if (position == 0) tab.setText("Now Showing");
//                    else tab.setText("Coming Soon");
//                }).attach();
//
//        return view;
//    }
package com.example.assignment1_l230504;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager2.widget.ViewPager2;

public class Home_Fragment extends Fragment {

    private Button btnToday, btnTomorrow;
    private String selectedDate = "Today";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); // Enable options menu
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_, container, false);

        btnToday = view.findViewById(R.id.btnToday);
        btnTomorrow = view.findViewById(R.id.btnTomorrow);
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        ViewPager2 viewPager = view.findViewById(R.id.viewPager);

        // Date button click listeners
        btnToday.setOnClickListener(v -> {
            selectedDate = "Today";
            updateDateButtonStyles();
            refreshMovies();
        });

        btnTomorrow.setOnClickListener(v -> {
            selectedDate = "Tomorrow";
            updateDateButtonStyles();
            refreshMovies();
        });

        ViewPageAdapter adapter = new ViewPageAdapter(this);
        viewPager.setAdapter(adapter);

        new com.google.android.material.tabs.TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    if (position == 0) tab.setText("Now Showing");
                    else tab.setText("Coming Soon");
                }).attach();

        return view;
    }

    private void updateDateButtonStyles() {
        if (selectedDate.equals("Today")) {
            btnToday.setBackgroundTintList(getResources().getColorStateList(R.color.accent_red));
            btnTomorrow.setBackgroundTintList(getResources().getColorStateList(R.color.button_gray));
        } else {
            btnToday.setBackgroundTintList(getResources().getColorStateList(R.color.button_gray));
            btnTomorrow.setBackgroundTintList(getResources().getColorStateList(R.color.accent_red));
        }
    }

    private void refreshMovies() {
        // Refresh the ViewPager to update movie lists based on selected date
        ViewPager2 viewPager = getView().findViewById(R.id.viewPager);
        if (viewPager != null && viewPager.getAdapter() != null) {
            viewPager.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // Add three-dots menu item
        menu.add(Menu.NONE, 1, Menu.NONE, "View Last Booking")
                .setIcon(android.R.drawable.ic_menu_info_details)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getTitle() != null && item.getTitle().equals("View Last Booking")) {
            showLastBooking();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showLastBooking() {
        SharedPreferences prefs = requireContext().getSharedPreferences("BookingPrefs", Context.MODE_PRIVATE);

        String movieName = prefs.getString("LAST_MOVIE", "");
        int seatCount = prefs.getInt("LAST_SEAT_COUNT", 0);
        double totalPrice = prefs.getFloat("LAST_TOTAL_PRICE", 0);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Last Booking");

        if (movieName.isEmpty()) {
            builder.setMessage("No previous booking found.");
        } else {
            String message = "Movie: " + movieName + "\n" +
                    "Seats: " + seatCount + "\n" +
                    "Total Price: $" + String.format("%.2f", totalPrice);
            builder.setMessage(message);
        }

        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}