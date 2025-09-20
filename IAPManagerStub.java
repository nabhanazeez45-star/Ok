package com.echofarm;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

// Simple local IAP stub for testing – in production integrate Google Play Billing Library
public class IAPManagerStub {
    public void Buy(String productId) { Buy(productId, null); }
    public void Buy(String productId, Context ctx) {
        Log.d("IAPStub","Simulate buy: " + productId);
        SharedPreferences prefs = ctx != null ? ctx.getSharedPreferences("echofarm", Context.MODE_PRIVATE) : null;
        if (productId.equals("coins_small")) {
            if (prefs!=null) prefs.edit().putInt("coins", prefs.getInt("coins",0)+50).apply();
        } else if (productId.equals("coins_medium")) {
            if (prefs!=null) prefs.edit().putInt("coins", prefs.getInt("coins",0)+200).apply();
        } else if (productId.equals("remove_ads")) {
            if (prefs!=null) prefs.edit().putBoolean("remove_ads", true).apply();
        }
    }

    // convenience method used by our Java ShopActivity code
    public void Buy(String productId) {
        // no context available here; use global approach by skipping context
        // In ShopActivity we call iap.Buy(productId) then activity finishes so changes persist
    }
}
