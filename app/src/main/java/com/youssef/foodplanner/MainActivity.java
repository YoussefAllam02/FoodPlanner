package com.youssef.foodplanner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private NavController navController;
    private LottieAnimationView lottieNoInternet;
    private View noInternetContainer;
    private BroadcastReceiver networkReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        lottieNoInternet = findViewById(R.id.lottie_no_internet);
        noInternetContainer = findViewById(R.id.no_internet_container);
        View btnRetry = findViewById(R.id.btn_retry);

        // Setup navigation
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.frag_host);
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
            NavigationUI.setupWithNavController(bottomNavigationView, navController);

            // Listen for destination changes
            navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
                // Hide BottomNavigationView for specific fragments (e.g., login/signup)
                if (destination.getId() == R.id.loginFragment || destination.getId() == R.id.signupFragment) {
                    bottomNavigationView.setVisibility(View.GONE);
                } else {
                    bottomNavigationView.setVisibility(View.VISIBLE);
                }

                // Show "No Internet" animation only on Home and Search pages
                if (destination.getId() == R.id.homeFragment || destination.getId() == R.id.searchFragment) {
                    checkNetworkConnection();
                } else {
                    // Hide "No Internet" animation for other pages (e.g., Meal Plan, Favourite)
                    noInternetContainer.setVisibility(View.GONE);
                    lottieNoInternet.pauseAnimation();
                }
            });
        }

        // Setup network receiver
        networkReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                checkNetworkConnection();
            }
        };

        // Retry button click listener
        btnRetry.setOnClickListener(v -> checkNetworkConnection());

        // Initial network check
        checkNetworkConnection();
    }

    private void checkNetworkConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        // Get the current destination
        int currentDestination = navController.getCurrentDestination() != null
                ? navController.getCurrentDestination().getId()
                : -1;

        // Show "No Internet" animation only on Home and Search pages
        if (currentDestination == R.id.homeFragment || currentDestination == R.id.searchFragment) {
            if (isConnected) {
                showContent();
            } else {
                showNoInternet();
            }
        }
    }

    private void showNoInternet() {
        noInternetContainer.setVisibility(View.VISIBLE);
        lottieNoInternet.playAnimation();
    }

    private void showContent() {
        noInternetContainer.setVisibility(View.GONE);
        lottieNoInternet.pauseAnimation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(networkReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (lottieNoInternet != null) {
            lottieNoInternet.cancelAnimation();
        }
    }
}