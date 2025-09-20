package com.echofarm;
import android.app.Activity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

public class SettingsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout lay = new LinearLayout(this);
        lay.setOrientation(LinearLayout.VERTICAL);
        TextView t = new TextView(this);
        t.setText("Settings");
        lay.addView(t);
        Switch sfx = new Switch(this);
        sfx.setText("Sound (placeholder)");
        boolean soundOn = getSharedPreferences("echofarm", MODE_PRIVATE).getBoolean("sound_on", true);
        sfx.setChecked(soundOn);
        sfx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                getSharedPreferences("echofarm", MODE_PRIVATE).edit().putBoolean("sound_on", isChecked).apply();
            }
        });
        lay.addView(sfx);
        setContentView(lay);
    }
}
