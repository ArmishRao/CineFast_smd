package com.example.assignment1_l230504;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SnackAdapter extends RecyclerView.Adapter<SnackAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Snack> snacks;
    private OnQuantityChangeListener listener;

    public interface OnQuantityChangeListener {
        void onQuantityChanged(double total);  // Changed to accept double
    }

    public SnackAdapter(Context context, ArrayList<Snack> snacks, OnQuantityChangeListener listener) {
        this.context = context;
        this.snacks = snacks;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_snack, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Snack snack = snacks.get(position);

        holder.imgSnack.setImageResource(snack.getImageResource());
        holder.txtName.setText(snack.getName());
        holder.txtDescription.setText(snack.getDescription());
        holder.txtPrice.setText("$" + String.format("%.2f", snack.getPrice()));
        holder.txtQuantity.setText(String.valueOf(snack.getQuantity()));

        holder.btnPlus.setOnClickListener(v -> {
            snack.incrementQuantity();
            holder.txtQuantity.setText(String.valueOf(snack.getQuantity()));
            if (listener != null) {
                listener.onQuantityChanged(calculateTotal());  // Passes double parameter
            }
        });

        holder.btnMinus.setOnClickListener(v -> {
            snack.decrementQuantity();
            holder.txtQuantity.setText(String.valueOf(snack.getQuantity()));
            if (listener != null) {
                listener.onQuantityChanged(calculateTotal());  // Passes double parameter
            }
        });
    }

    private double calculateTotal() {
        double total = 0;
        for (Snack snack : snacks) {
            total += snack.getTotalPrice();
        }
        return total;
    }

    @Override
    public int getItemCount() {
        return snacks.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgSnack;
        TextView txtName, txtDescription, txtPrice, txtQuantity;
        Button btnPlus, btnMinus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSnack = itemView.findViewById(R.id.imgSnack);
            txtName = itemView.findViewById(R.id.txtName);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            btnMinus = itemView.findViewById(R.id.btnMinus);
        }
    }
}