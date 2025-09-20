package com.echofarm;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;

public class MainActivity extends Activity {
    private GameView gameView;
    private TextView coinsText;
    private Button btnShop, btnSettings;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameView = findViewById(R.id.gameView);
        coinsText = findViewById(R.id.coinsText);
        btnShop = findViewById(R.id.btnShop);
        btnSettings = findViewById(R.id.btnSettings);

        btnShop.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ShopActivity.class));
            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
        updateUI();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    public void updateUI() {
        if (coinsText != null) coinsText.setText("Coins: " + gameView.getCoins());
    }
}
