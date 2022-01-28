package com.example.MyListProject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.AudioFocusType;
import com.huawei.hms.ads.HwAds;
import com.huawei.hms.ads.splash.SplashView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class AdsActivity extends AppCompatActivity {

    private static final int AD_TIMEOUT = 10000;
    private static final int MSG_AD_TIMEOUT = 1001;
    private boolean hasPaused = false;

    private Handler timeoutHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (AdsActivity.this.hasWindowFocus()) {
                jump();
            }
            return false;
        }
    });

    private void loadAd() {
        AdParam adParam = new AdParam.Builder().build();
        SplashView splashView = findViewById(R.id.splashAdView);
        SplashView.SplashAdLoadListener splashAdLoadListener = new SplashView.SplashAdLoadListener() {
            @Override
            public void onAdLoaded() {
                // Called when an ad is loaded successfully.

            }
            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Called when an ad fails to be loaded. The app home screen is then displayed.
                jump();
            }
            @Override
            public void onAdDismissed() {
                // Called when an ad has been displayed. The app home screen is then displayed.
                jump();
            }
        };
        String slotId;
        // Lock the screen orientation on the device. Your app will automatically adapt to the screen orientation.
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        int orientation = getScreenOrientation();
        // Set the default slogan and the splash ad unit ID based on the screen orientation on the device
        // Obtain SplashView.

        // Set the audio focus type for a video splash ad.
        splashView.setAudioFocusType(AudioFocusType.NOT_GAIN_AUDIO_FOCUS_WHEN_MUTE);
        // Load the ad.
        splashView.load("testq6zq98hecj", orientation, adParam, splashAdLoadListener);
        // Send a delay message to ensure that the app home screen can be properly displayed after the ad display times out.
        timeoutHandler.removeMessages(MSG_AD_TIMEOUT);
        timeoutHandler.sendEmptyMessageDelayed(MSG_AD_TIMEOUT, AD_TIMEOUT);
    }
    private int getScreenOrientation() {
        Configuration config = getResources().getConfiguration();
        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        } else {
            return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads);

        loadAd();

        HwAds.init(this);
    }

    private void jump() {
        if (!hasPaused) {
            hasPaused = true;
            startActivity(new Intent(AdsActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onStop() {
        timeoutHandler.removeMessages(MSG_AD_TIMEOUT);
        hasPaused = true;
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        hasPaused = false;
        jump();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}