package com.example.assignment1_l230504;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

public class NowShowingFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<Movie> list;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        list = new ArrayList<>();
        list.add(new Movie(R.drawable.interstellar,"Interstellar", "Sci-Fi",
                "https://www.youtube.com/watch?v=zSWdZVtXT7E"));
        list.add(new Movie( R.drawable.inception,"Inception", "Sci-Fi",
                "https://www.youtube.com/watch?v=YoHD9XEInc0"));
        list.add(new Movie(R.drawable.dark,"Dark Knight", "Action",
                "https://www.youtube.com/watch?v=EXeTwQWrcwY"));

        recyclerView.setAdapter(new MovieAdapter(getContext(), list));

        return view;
    }
}