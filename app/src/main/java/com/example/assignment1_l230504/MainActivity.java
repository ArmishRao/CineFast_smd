package com.example.assignment1_l230504;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private FirebaseAuth mAuth;
    private TextView navHeaderEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Add Toast with "CineFAST" tag
        Toast.makeText(getApplicationContext(), "CineFAST", Toast.LENGTH_SHORT).show();

        // Initialize views
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);

        // Setup Toolbar
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("CineFAST");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Setup Drawer Toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Setup Navigation View
        navigationView.setNavigationItemSelectedListener(this);

        // Update header with user email
        updateNavHeader();

        // Set default fragment (Home)
        if (savedInstanceState == null) {
            loadFragment(new Home_Fragment());
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    private void updateNavHeader() {
        View headerView = navigationView.getHeaderView(0);
        navHeaderEmail = headerView.findViewById(R.id.navHeaderEmail);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null && currentUser.getEmail() != null) {
            navHeaderEmail.setText(currentUser.getEmail());
            navHeaderEmail.setVisibility(View.VISIBLE);
        }
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.nav_home) {
            loadFragment(new Home_Fragment());
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("CineFAST");
            }
        } else if (itemId == R.id.nav_my_bookings) {
            loadFragment(new MyBookingsFragment());
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("My Bookings");
            }
        } else if (itemId == R.id.nav_logout) {
            drawerLayout.closeDrawer(GravityCompat.START);
            logout();
            return true;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        // Clear SharedPreferences session
        SharedPreferences prefs = getSharedPreferences("cinefast_session_pref_v3", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();

        // Sign out from Firebase
        FirebaseAuth.getInstance().signOut();

        // Navigate to Login screen
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // Remove the old options menu methods since we're using drawer now
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // We're not using options menu anymore, using drawer instead
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle action bar item clicks here
        return super.onOptionsItemSelected(item);
    }
}