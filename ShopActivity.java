package com.echofarm;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View;

public class ShopActivity extends Activity {
    IAPManagerStub iap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iap = new IAPManagerStub();
        LinearLayout lay = new LinearLayout(this);
        lay.setOrientation(LinearLayout.VERTICAL);
        TextView t = new TextView(this);
        t.setText("Shop - Purchase coins (debug + stub)"); lay.addView(t);

        Button b1 = new Button(this); b1.setText("Buy 10 coins (debug)"); 
        b1.setOnClickListener(new View.OnClickListener(){ @Override public void onClick(View v){
            getSharedPreferences("echofarm", MODE_PRIVATE).edit().putInt("coins", getSharedPreferences("echofarm", MODE_PRIVATE).getInt("coins",0)+10).apply();
            finish();
        }});
        lay.addView(b1);

        Button b2 = new Button(this); b2.setText("Buy 100 coins (simulate IAP)"); 
        b2.setOnClickListener(new View.OnClickListener(){ @Override public void onClick(View v){
            iap.Buy("coins_medium"); // stub will add coins
            finish();
        }});
        lay.addView(b2);

        Button b3 = new Button(this); b3.setText("Remove Ads (simulate)"); 
        b3.setOnClickListener(new View.OnClickListener(){ @Override public void onClick(View v){
            iap.Buy("remove_ads"); finish();
        }});
        lay.addView(b3);

        setContentView(lay);
    }
}
