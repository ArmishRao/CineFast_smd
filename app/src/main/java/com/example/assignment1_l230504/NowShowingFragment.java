package com.example.assignment1_l230504;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import java.util.ArrayList;

public class NowShowingFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ArrayList<Movie> movies = new ArrayList<>();
        movies.add(new Movie(R.drawable.dark, "The Dark Knight", "Action / 152 min",
                "https://www.youtube.com/watch?v=EXeTwQWrcwY"));
        movies.add(new Movie(R.drawable.inception, "Inception", "Sci-Fi / 148 min",
                "https://www.youtube.com/watch?v=YoHD9XEInc0"));
        movies.add(new Movie(R.drawable.interstellar, "Interstellar", "Sci-Fi / 169 min",
                "https://www.youtube.com/watch?v=zSWdZVtXT7E"));

        // Pass false for isComingSoon
        recyclerView.setAdapter(new MovieAdapter(getContext(), movies, false));
        return view;
    }
}