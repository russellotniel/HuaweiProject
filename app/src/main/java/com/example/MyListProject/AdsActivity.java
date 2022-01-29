package com.example.MyListProject;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.AudioFocusType;
import com.huawei.hms.ads.HwAds;
import com.huawei.hms.ads.splash.SplashAdDisplayListener;
import com.huawei.hms.ads.splash.SplashView;

public class AdsActivity extends Activity{
    private static final String TAG = AdsActivity.class.getSimpleName();
    private static final int AD_TIMEOUT = 10000;
    private static final int MSG_AD_TIMEOUT = 1001;
    private boolean hasPaused = false;

    private SplashView splashView;

    private Handler timeoutHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (AdsActivity.this.hasWindowFocus()) {
                jump();
            }
            return false;
        }
    });

    private SplashView.SplashAdLoadListener splashAdLoadListener = new SplashView.SplashAdLoadListener() {
        @Override
        public void onAdLoaded() {
            Log.i(TAG, "SplashAdLoadListener onAdLoaded.");
            Toast.makeText(AdsActivity.this, getString(R.string.status_load_ad_success), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAdFailedToLoad(int errorCode) {
            Log.i(TAG, "SplashAdLoadListener onAdFailedToLoad, errorCode: " + errorCode);
            Toast.makeText(AdsActivity.this, getString(R.string.status_load_ad_fail) + errorCode, Toast.LENGTH_SHORT).show();
            jump();
        }

        @Override
        public void onAdDismissed() {
            Log.i(TAG, "SplashAdLoadListener onAdDismissed.");
            Toast.makeText(AdsActivity.this, getString(R.string.status_ad_dismissed), Toast.LENGTH_SHORT).show();
            jump();
        }
    };

    private SplashAdDisplayListener adDisplayListener = new SplashAdDisplayListener() {
        @Override
        public void onAdShowed() {
            Log.i(TAG, "SplashAdDisplayListener onAdShowed.");
        }

        @Override
        public void onAdClick() {
            Log.i(TAG, "SplashAdDisplayListener onAdClick.");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads);

        HwAds.init(this);

        loadAd();
    }

    private void loadAd() {
        Log.i(TAG, "Start to load ad");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        int orientation = getScreenOrientation();
        AdParam adParam = new AdParam.Builder().build();
        splashView = findViewById(R.id.splash_ad_view);
        splashView.setAdDisplayListener(adDisplayListener);

        String slotId;
        if (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            slotId = getString(R.string.ad_id_splash);
        } else {
            slotId = getString(R.string.ad_id_splash_landscape);
        }
        splashView.setLogo(findViewById(R.id.logo_area));

        splashView.setLogoResId(R.mipmap.ic_launcher);
        splashView.setMediaNameResId(R.string.app_name);
        splashView.setAudioFocusType(AudioFocusType.NOT_GAIN_AUDIO_FOCUS_WHEN_MUTE);
        splashView.load(slotId, orientation, adParam, splashAdLoadListener);
        Log.i(TAG, "End to load ad");

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

    private void jump() {
        Log.i(TAG, "jump hasPaused: " + hasPaused);
        if (!hasPaused) {
            hasPaused = true;
            Log.i(TAG, "jump into application");

            startActivity(new Intent(AdsActivity.this, LoginPage.class));

            Handler mainHandler = new Handler();
            mainHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 1000);
        }
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "SplashActivity onStop.");
        // Remove the timeout message from the message queue.
        timeoutHandler.removeMessages(MSG_AD_TIMEOUT);
        hasPaused = true;
        super.onStop();
    }

    @Override
    protected void onRestart() {
        Log.i(TAG, "SplashActivity onRestart.");
        super.onRestart();
        hasPaused = false;
        jump();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "SplashActivity onDestroy.");
        super.onDestroy();
        if (splashView != null) {
            splashView.destroyView();
        }
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "SplashActivity onPause.");
        super.onPause();
        if (splashView != null) {
            splashView.pauseView();
        }
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "SplashActivity onResume.");
        super.onResume();
        if (splashView != null) {
            splashView.resumeView();
        }
    }
}
