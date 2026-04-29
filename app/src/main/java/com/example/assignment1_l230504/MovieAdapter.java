package com.example.assignment1_l230504;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    Context context;
    ArrayList<Movie> movies;
    boolean isComingSoon;

    public MovieAdapter(Context context, ArrayList<Movie> movies, boolean isComingSoon) {
        this.context = context;
        this.movies = movies;
        this.isComingSoon = isComingSoon;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgMovie;
        TextView txtName, txtGenre;
        Button btnBook, btnTrailer;

        public ViewHolder(View itemView) {
            super(itemView);
            imgMovie = itemView.findViewById(R.id.imgMovie);
            txtName = itemView.findViewById(R.id.txtName);
            txtGenre = itemView.findViewById(R.id.txtGenre);
            btnBook = itemView.findViewById(R.id.btnBook);
            btnTrailer = itemView.findViewById(R.id.btnTrailer);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movie movie = movies.get(position);

        holder.txtName.setText(movie.getName());
        holder.txtGenre.setText(movie.getGenre());
        holder.imgMovie.setImageResource(movie.getImage());

        holder.btnTrailer.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(movie.getTrailerURL()));
            context.startActivity(intent);
        });


        holder.btnBook.setOnClickListener(v -> {
            Intent intent = new Intent(context, SeatSelectionActivity.class);
            intent.putExtra("MOVIE_NAME", movie.getName());
            intent.putExtra("IS_COMING_SOON", isComingSoon);
            intent.putExtra("TRAILER_URL", movie.getTrailerURL());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }
}