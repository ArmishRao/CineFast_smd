package com.example.assignment1_l230504;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import java.util.ArrayList;

public class ComingSoonFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ArrayList<Movie> movies = new ArrayList<>();
        movies.add(new Movie(R.drawable.dune, "Dune: Part Two", "Sci-Fi / 166 min",
                "https://www.youtube.com/watch?v=Way9Dexny3w"));
        movies.add(new Movie(R.drawable.avatar, "Avatar 3", "Fantasy / 180 min",
                "https://www.youtube.com/watch?v=d9MyW72ELq0"));
        movies.add(new Movie(R.drawable.deadpool, "Deadpool 3", "Action/Comedy / 120 min",
                "https://www.youtube.com/watch?v=73_1biulkYk"));


        recyclerView.setAdapter(new MovieAdapter(getContext(), movies, true));
        return view;
    }
}