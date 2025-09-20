package com.echofarm;
import android.app.Activity;
import android.util.Log;
import com.android.billingclient.api.*; // requires billing dependency
import java.util.List;

// Skeleton BillingManager - implement listeners and mapping to product IDs
public class BillingManager implements PurchasesUpdatedListener {
    private static BillingManager instance;
    private BillingClient billingClient;

    private BillingManager(Activity activity) {
        billingClient = BillingClient.newBuilder(activity).setListener(this).enablePendingPurchases().build();
        billingClient.startConnection(new BillingClientStateListener() {
            @Override public void onBillingSetupFinished(BillingResult billingResult) {
                Log.d("Billing","Setup finished: " + billingResult.getResponseCode());
            }
            @Override public void onBillingServiceDisconnected() { Log.d("Billing","Disconnected"); }
        });
    }

    public static void init(Activity activity) { if (instance==null) instance = new BillingManager(activity); }
    public static BillingManager I() { return instance; }

    public void purchase(Activity activity, String productId) {
        List<String> skuList = java.util.Collections.singletonList(productId);
        SkuDetailsParams params = SkuDetailsParams.newBuilder().setSkusList(skuList).setType(BillingClient.SkuType.INAPP).build();
        billingClient.querySkuDetailsAsync(params, (billingResult, skuDetailsList) -> {
            if (skuDetailsList!=null && skuDetailsList.size()>0) {
                BillingFlowParams flowParams = BillingFlowParams.newBuilder().setSkuDetails(skuDetailsList.get(0)).build();
                billingClient.launchBillingFlow(activity, flowParams);
            }
        });
    }

    @Override
    public void onPurchasesUpdated(BillingResult billingResult, List<Purchase> purchases) {
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (Purchase p : purchases) {
                // TODO: acknowledge and grant items
                Log.d("Billing","Purchase: " + p.getSku());
            }
        }
    }
}
