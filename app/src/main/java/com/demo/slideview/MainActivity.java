package com.demo.slideview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    private static final float SWIPE_THRESHOLD = 100f;
    private static final float SWIPE_VELOCITY_THRESHOLD = 100f;
    private LinearLayout slideView;
    private ImageView closeIcon, openIcon;

    private GestureDetector gestureDetector;
    private FrameLayout slideViewContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gestureDetector = new GestureDetector(this, new MyGestureListener());

        slideViewContainer = findViewById(R.id.slideViewContainer);

        slideView = findViewById(R.id.slideView);
        closeIcon = findViewById(R.id.closeIcon);
        openIcon = findViewById(R.id.openIcon);

        // Initially, set the slide view's visibility to GONE
        slideViewContainer.setVisibility(View.GONE);

        openIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSlideView();
            }
        });

        closeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSlideView();
            }
        });
    }

    private void showSlideView() {
        // Show the semi-transparent background
        View transparentBackground = findViewById(R.id.transparentBackground);
        transparentBackground.setVisibility(View.VISIBLE);

        // Get the height of the screen
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenHeight = displayMetrics.heightPixels;

        // Slide up animation
        TranslateAnimation slideUp = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF, 0.0f,
                TranslateAnimation.RELATIVE_TO_SELF, 0.0f,
                TranslateAnimation.RELATIVE_TO_SELF, 1.0f,
                TranslateAnimation.RELATIVE_TO_SELF, 0.5f
        );
        slideUp.setDuration(100);
        slideUp.setInterpolator(new AccelerateDecelerateInterpolator()); // Use the interpolator
        slideViewContainer.startAnimation(slideUp);

        // Set the slide view's height to half of the screen
        slideView.getLayoutParams().height = screenHeight / 2;
        slideView.requestLayout();

        // Make the slide view container visible
        slideViewContainer.setVisibility(View.VISIBLE);
    }

    private void hideSlideView() {
        // Hide the semi-transparent background
        View transparentBackground = findViewById(R.id.transparentBackground);
        transparentBackground.setVisibility(View.GONE);

        // Slide down animation
        TranslateAnimation slideDown = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF, 0.0f,
                TranslateAnimation.RELATIVE_TO_SELF, 0.0f,
                TranslateAnimation.RELATIVE_TO_SELF, 0.5f,
                TranslateAnimation.RELATIVE_TO_SELF, 1.0f
        );
        slideDown.setDuration(100);
        slideDown.setInterpolator(new AccelerateDecelerateInterpolator()); // Use the interpolator
        slideViewContainer.startAnimation(slideDown);

        // Make the slide view container invisible after the animation is complete
        slideViewContainer.setVisibility(View.GONE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event);
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            // Detect swipe gesture here
            if (e1.getY() - e2.getY() > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                //showSlideView();
                return true;
            } else if(e1.getY() + e2.getY() > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD){
                hideSlideView();
                return true;
            } /** uncomment if swipe up is needed */
            return false;
        }
    }
}