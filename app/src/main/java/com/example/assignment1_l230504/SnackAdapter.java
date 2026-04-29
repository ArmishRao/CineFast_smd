package com.example.assignment1_l230504;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class SnackAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Snack> snacks;
    private LayoutInflater inflater;
    private OnQuantityChangeListener listener;

    public interface OnQuantityChangeListener {
        void onQuantityChanged();
    }

    public SnackAdapter(Context context, ArrayList<Snack> snacks, OnQuantityChangeListener listener) {
        this.context = context;
        this.snacks = snacks;
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return snacks.size();
    }

    @Override
    public Object getItem(int position) {
        return snacks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_snack, parent, false);
            holder = new ViewHolder();
            holder.imgSnack = convertView.findViewById(R.id.imgSnack);
            holder.txtName = convertView.findViewById(R.id.txtName);
            holder.txtDescription = convertView.findViewById(R.id.txtDescription);
            holder.btnPlus = convertView.findViewById(R.id.btnPlus);
            holder.btnMinus = convertView.findViewById(R.id.btnMinus);
            holder.txtQuantity = convertView.findViewById(R.id.txtQuantity);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Snack snack = snacks.get(position);

        holder.imgSnack.setImageResource(snack.getImageResource());
        holder.txtName.setText(snack.getName());
        holder.txtDescription.setText(snack.getDescription());
        holder.txtQuantity.setText(String.valueOf(snack.getQuantity()));

        TextView txtPrice = convertView.findViewById(R.id.txtPrice);
        txtPrice.setText("$" + String.format("%.2f", snack.getPrice()));

        holder.btnPlus.setOnClickListener(v -> {
            snack.incrementQuantity();
            holder.txtQuantity.setText(String.valueOf(snack.getQuantity()));
            if (listener != null) {
                listener.onQuantityChanged();
            }
        });

        holder.btnMinus.setOnClickListener(v -> {
            snack.decrementQuantity();
            holder.txtQuantity.setText(String.valueOf(snack.getQuantity()));
            if (listener != null) {
                listener.onQuantityChanged();
            }
        });

        return convertView;
    }

    static class ViewHolder {
        ImageView imgSnack;
        TextView txtName;
        TextView txtDescription;
        TextView txtQuantity;
        Button btnPlus;
        Button btnMinus;
    }
}