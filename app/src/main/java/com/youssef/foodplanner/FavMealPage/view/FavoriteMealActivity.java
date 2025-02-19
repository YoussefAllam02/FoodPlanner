package com.youssef.foodplanner.FavMealPage.view;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import com.youssef.foodplanner.R;

public class FavoriteMealActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoruite_meal_activtiy);

        if (savedInstanceState == null) {
            favouriteFragment favouriteFragment = new favouriteFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, favouriteFragment);
            transaction.commit();
        }
    }
}
