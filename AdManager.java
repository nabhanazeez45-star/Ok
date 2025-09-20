package com.echofarm;
import android.app.Activity;
import android.util.Log;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;

// AdMob skeleton - requires setup in Android Studio and real Ad Unit IDs
public class AdManager {
    private static AdManager instance;
    private InterstitialAd interstitial;
    private RewardedAd rewarded;

    private AdManager(Activity activity) {
        MobileAds.initialize(activity, initializationStatus -> {});
        // load ads using real Ad Unit IDs
    }

    public static void init(Activity activity) { if (instance==null) instance = new AdManager(activity); }
    public static AdManager I() { return instance; }

    public void showInterstitial(Activity activity) {
        Log.d("AdManager","showInterstitial called - implement ad loading and showing.");
    }
    public void showRewarded(Activity activity, Runnable onReward) {
        Log.d("AdManager","showRewarded called - implement ad loading and showing.");
        if (onReward!=null) onReward.run(); // fallback grant
    }
}
