package com.example.movienight.Prenium;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import static android.content.ContentValues.TAG;

public class Ad {

    public static void displayInterstitialAd(Context context, Activity activity) {
        if (context == null || activity == null)
            return;

        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(context,"ca-app-pub-3940256099942544/1033173712", adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                interstitialAd.show(activity);
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                Log.i(TAG, loadAdError.getMessage());
            }
        });
    }

    public static  void displayBannerAd(AdView adView) {
        if (adView == null)
            return;

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }
}
