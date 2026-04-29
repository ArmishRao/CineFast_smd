package com.example.assignment1_l230504;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder> {

    private Context context;
    private ArrayList<MyBookingsFragment.BookingItem> bookings;

    public BookingAdapter(Context context, ArrayList<MyBookingsFragment.BookingItem> bookings) {
        this.context = context;
        this.bookings = bookings;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_booking, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MyBookingsFragment.BookingItem booking = bookings.get(position);

        holder.tvMovieName.setText(booking.getMovieName());
        holder.tvSeats.setText("Seats: " + booking.getSeatCount());
        holder.tvTotal.setText("Total: $" + String.format("%.2f", booking.getTotalPrice()));
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMovieName, tvSeats, tvTotal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMovieName = itemView.findViewById(R.id.tvMovieName);
            tvSeats = itemView.findViewById(R.id.tvSeats);
            tvTotal = itemView.findViewById(R.id.tvTotal);
        }
    }
}