package com.youssef.foodplanner;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.youssef.foodplanner.Auth.View.AuthActivity;

public class SplashActivity extends AppCompatActivity {

    private LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        lottieAnimationView = findViewById(R.id.lottieAnimation);
        setupLottieAnimation();
    }

    private void setupLottieAnimation() {
        lottieAnimationView.setAnimation(R.raw.animation);
        lottieAnimationView.setRepeatCount(0);

        // Use Android's Animator.AnimatorListener instead of Lottie-specific listener
        lottieAnimationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                // Animation started
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                navigateToAppropriateScreen();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                navigateToAppropriateScreen();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // Not used
            }
        });

        lottieAnimationView.playAnimation();
    }

    private void navigateToAppropriateScreen() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Class<?> destination = user != null ? MainActivity.class : AuthActivity.class;
        startActivity(new Intent(this, destination));
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    protected void onDestroy() {
        if (lottieAnimationView != null) {
            lottieAnimationView.cancelAnimation();
        }
        super.onDestroy();
    }
}