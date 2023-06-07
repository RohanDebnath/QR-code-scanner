package com.example.qrcode;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout container;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get references to the container and TextView
        container = findViewById(R.id.container);
        textView = findViewById(R.id.textView);

        // Create and start the animation
        startAnimation();
    }

    private void startAnimation() {
        // Create the ball animation
        for (int i = 0; i < 10; i++) {
            ImageView ball = new ImageView(this);
            ball.setImageResource(R.drawable.qr);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            ball.setLayoutParams(params);
            container.addView(ball);

            ObjectAnimator ballAnimator = ObjectAnimator.ofFloat(ball, View.TRANSLATION_Y, -1000f, 0f);
            ballAnimator.setDuration(1500);
            ballAnimator.setInterpolator(new AccelerateInterpolator());
            ballAnimator.setStartDelay(i * 100);
            ballAnimator.start();
        }

        // Create the text animation
        ObjectAnimator textAnimator = ObjectAnimator.ofFloat(textView, View.SCALE_X, 0f, 1f);
        textAnimator.setDuration(3000);
        textAnimator.setInterpolator(new AccelerateInterpolator());

        // Add a listener to start the new activity when the animation completes
        textAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // Start the new activity
                startActivity(new Intent(MainActivity.this, Login.class));
                finish(); // Finish the current activity if desired
            }
        });

        textAnimator.start();

        // Create the final animation to fade in the main content view
        ObjectAnimator contentAnimator = ObjectAnimator.ofFloat(textView, View.ALPHA, 0f, 1f);
        contentAnimator.setDuration(500);
        contentAnimator.setStartDelay(2000);
        contentAnimator.start();
    }
}
