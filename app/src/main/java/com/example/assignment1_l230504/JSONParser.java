package com.example.assignment1_l230504;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

public class JSONParser {

    private Context context;

    public JSONParser(Context context) {
        this.context = context;
    }

    public ArrayList<Movie> parseMoviesFromAssets(String filename) {
        ArrayList<Movie> movies = new ArrayList<>();

        try {
            // Read JSON file from assets
            InputStream inputStream = context.getAssets().open(filename);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            String jsonString = new String(buffer, "UTF-8");

            // Parse JSON array
            JSONArray jsonArray = new JSONArray(jsonString);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                // Get image resource ID from drawable name
                String imageName = jsonObject.getString("image");
                int imageResId = getDrawableResourceId(imageName);

                String name = jsonObject.getString("name");
                String genre = jsonObject.getString("genre");
                String trailerURL = jsonObject.getString("trailerURL");

                Movie movie = new Movie(imageResId, name, genre, trailerURL);
                movies.add(movie);
            }

        } catch (Exception e) {
            Log.e("JSONParser", "Error parsing JSON: " + e.getMessage());
            e.printStackTrace();
        }

        return movies;
    }

    private int getDrawableResourceId(String imageName) {
        // Map JSON image names to drawable resource IDs
        switch (imageName) {
            case "dark":
                return R.drawable.dark;
            case "inception":
                return R.drawable.inception;
            case "interstellar":
                return R.drawable.interstellar;
            case "dune":
                return R.drawable.dune;
            case "avatar":
                return R.drawable.avatar;
            case "deadpool":
                return R.drawable.deadpool;
            default:
                // Try to get resource dynamically
                return context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
        }
    }
}