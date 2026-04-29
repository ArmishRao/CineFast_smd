package com.example.assignment1_l230504;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.assignment1_l230504.R;


public class homeScreenActivity extends AppCompatActivity {
//ListView listView;
Button btnTrailer1, btnBook1;
Button btnTrailer2, btnBook2;
Button btnTrailer3, btnBook3;
//ArrayList<String> arrNames=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        listView=findViewById(R.id.list_item);
//        arrNames.add("The Dark Night");
//        arrNames.add("Interstellar");
//        arrNames.add("Inception");
//        ArrayAdapter<String> adap=new ArrayAdapter<>(getApplicationContext(), R.layout.listitem,arrNames);
//        listView.setAdapter(adap);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if(position==0)
//                {
//                    Toast.makeText(homeScreenActivity.this, "Hello World", Toast.LENGTH_SHORT).show();
//
//                }

//            }
//        });

        btnTrailer1 = findViewById(R.id.btnTrailer1);
        btnBook1 = findViewById(R.id.btnBook1);
        btnTrailer2= findViewById(R.id.btnTrailer2);
        btnBook2 = findViewById(R.id.btnBook2);
        btnTrailer3 = findViewById(R.id.btnTrailer3);
        btnBook3 = findViewById(R.id.btnBook3);
        btnTrailer1.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/watch?v=zSWdZVtXT7E"));
            startActivity(intent);
        });


        btnBook1.setOnClickListener(v -> {
            Intent intent = new Intent(homeScreenActivity.this,
                    SeatSelectionActivity.class);
            intent.putExtra("MOVIE_NAME", "Interstellar");
            startActivity(intent);
        });

        btnTrailer2.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/watch?v=zSWdZVtXT7E"));
            startActivity(intent);
        });

        btnBook2.setOnClickListener(v -> {
            Intent intent = new Intent(homeScreenActivity.this,
                    SeatSelectionActivity.class);
            intent.putExtra("MOVIE_NAME", "Interstellar");
            startActivity(intent);
        });
        btnTrailer3.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/watch?v=zSWdZVtXT7E"));
            startActivity(intent);
        });


        btnBook3.setOnClickListener(v -> {
            Intent intent = new Intent(homeScreenActivity.this,
                    SeatSelectionActivity.class);
            intent.putExtra("MOVIE_NAME", "Interstellar");
            startActivity(intent);
        });

    }
}