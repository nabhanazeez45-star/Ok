package com.echofarm;
import android.util.Log;

// Simple ad manager stub. In production integrate AdMob or Unity Ads SDK.
public class AdManagerStub {
    public void showRewarded(android.content.Context ctx, final Runnable onReward) {
        Log.d("AdStub","Simulate rewarded ad - granting reward immediately.");
        if (onReward!=null) onReward.run();
    }
    public void showInterstitial(android.content.Context ctx) {
        Log.d("AdStub","Simulate interstitial ad."); 
    }
}
