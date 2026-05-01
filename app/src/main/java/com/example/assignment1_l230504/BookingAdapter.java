package com.example.assignment1_l230504;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder> {

    private Context context;
    private ArrayList<MyBookingsFragment.BookingItem> bookings;
    private OnCancelClickListener cancelClickListener;

    public interface OnCancelClickListener {
        void onCancelClick(MyBookingsFragment.BookingItem booking, int position);
    }

    public BookingAdapter(Context context, ArrayList<MyBookingsFragment.BookingItem> bookings) {
        this.context = context;
        this.bookings = bookings;
    }

    public void setOnCancelClickListener(OnCancelClickListener listener) {
        this.cancelClickListener = listener;
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

        // Set movie poster
        if (booking.getImageResId() != 0) {
            holder.imgMoviePoster.setImageResource(booking.getImageResId());
        } else {
            holder.imgMoviePoster.setImageResource(R.drawable.interstellar);
        }

        // Set movie name
        holder.tvMovieName.setText(booking.getMovieName() != null ? booking.getMovieName() : "Unknown Movie");

        // Set date and time
        String dateTime = (booking.getDate() != null ? booking.getDate() : "Unknown") +
                " | " +
                (booking.getTime() != null ? booking.getTime() : "Unknown");
        holder.tvDateTime.setText(dateTime);

        // Set tickets count
        String ticketsText = booking.getTicketCount() + (booking.getTicketCount() == 1 ? " Ticket" : " Tickets");
        holder.tvTickets.setText(ticketsText);

        // Set cancel button based on cancellable status
        if (booking.isCancellable()) {
            holder.btnCancel.setEnabled(true);
            holder.btnCancel.setText("Cancel");
            holder.btnCancel.setBackgroundTintList(context.getColorStateList(android.R.color.holo_red_dark));
            holder.btnCancel.setAlpha(1.0f);
        } else {
            holder.btnCancel.setEnabled(false);
            holder.btnCancel.setText("Past");
            holder.btnCancel.setBackgroundTintList(context.getColorStateList(android.R.color.darker_gray));
            holder.btnCancel.setAlpha(0.6f);
        }

        // Set cancel button click listener
        holder.btnCancel.setOnClickListener(v -> {
            if (cancelClickListener != null && booking.isCancellable()) {
                cancelClickListener.onCancelClick(booking, position);
            } else if (!booking.isCancellable()) {
                Toast.makeText(context, "Cannot cancel past bookings!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgMoviePoster;
        TextView tvMovieName, tvDateTime, tvTickets;
        Button btnCancel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgMoviePoster = itemView.findViewById(R.id.imgMoviePoster);
            tvMovieName = itemView.findViewById(R.id.tvMovieName);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            tvTickets = itemView.findViewById(R.id.tvTickets);
            btnCancel = itemView.findViewById(R.id.btnCancel);
        }
    }
}