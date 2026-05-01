package com.example.assignment1_l230504;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

public class ComingSoonFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private JSONParser jsonParser;
    private ArrayList<Movie> movies = new ArrayList<>();
    private MovieAdapter movieAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progressBar);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Show loading indicator
        progressBar.setVisibility(View.VISIBLE);

        // Load movies from JSON
        loadMoviesFromJSON();

        return view;
    }

    private void loadMoviesFromJSON() {
        jsonParser = new JSONParser(getContext());

        // Load movies from assets/coming_soon.json
        movies = jsonParser.parseMoviesFromAssets("coming_soon.json");

        // Hide loading indicator
        progressBar.setVisibility(View.GONE);

        if (movies.isEmpty()) {
            Toast.makeText(getContext(), "No movies found. Check JSON file.", Toast.LENGTH_SHORT).show();
        } else {
            movieAdapter = new MovieAdapter(getContext(), movies, true);
            recyclerView.setAdapter(movieAdapter);
        }
    }
}