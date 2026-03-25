package com.example.assignment1_l230504;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
public class ComingSoonFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<Movie> list;

    public ComingSoonFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        list = new ArrayList<>();

        list.add(new Movie( R.drawable.dune,"Dune: Part Two", "Sci-Fi",
                "https://www.youtube.com/watch?v=Way9Dexny3w"));

        list.add(new Movie(R.drawable.avatar,"Avatar 3", "Fantasy",
                "https://www.youtube.com/watch?v=d9MyW72ELq0"));

        list.add(new Movie(R.drawable.deadpool,"Deadpool 3", "Action/Comedy",
                "https://www.youtube.com/watch?v=73_1biulkYk"));


        MovieAdapter adapter = new MovieAdapter(getContext(), list);
        recyclerView.setAdapter(adapter);

        return view;
    }
}