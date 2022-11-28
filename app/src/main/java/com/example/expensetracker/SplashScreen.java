package com.example.expensetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {
   ImageView logo;
   ImageView bgImage;
   TextView appNameText;
   LottieAnimationView lottieAnimationView;

   FirebaseAuth mAuth;
   FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        logo = findViewById(R.id.logoImg);
        bgImage = findViewById(R.id.backgroundImg);
        lottieAnimationView = findViewById(R.id.lottieAnimation);
        appNameText = findViewById(R.id.appName);

        bgImage.animate().translationY(-3000).setDuration(1000).setStartDelay(4000);
        logo.animate().translationY(2800).setDuration(1000).setStartDelay(4000);
        appNameText.animate().translationY(2800).setDuration(1000).setStartDelay(4000);
        lottieAnimationView.animate().translationY(2800).setDuration(1800).setStartDelay(4000);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        final Handler handler = new Handler();
        final Runnable r = new Runnable() {
            @Override
            public void run() {
                if(mUser != null){
                    startActivity(new Intent(SplashScreen.this, HomeActivity.class));
                }
                else{
                    openActivity();
                }

            }
        };
        handler.postDelayed(r,5200);


    }

    private void openActivity() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}