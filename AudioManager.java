package com.echofarm;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;

// Simple AudioManager using SoundPool for short effects and MediaPlayer for music
public class AudioManager {
    private static AudioManager instance;
    private SoundPool soundPool;
    private int sPlant = -1, sHarvest = -1;
    private MediaPlayer musicPlayer;
    private boolean soundOn = true;

    private AudioManager(Context ctx) {
        SoundPool.Builder b = new SoundPool.Builder();
        b.setMaxStreams(4);
        soundPool = b.build();

        // load placeholder sounds from assets if present - developers should replace with real sounds
        try {
            sPlant = soundPool.load(ctx.getAssets().openFd("sounds/plant.wav"), 1);
            sHarvest = soundPool.load(ctx.getAssets().openFd("sounds/harvest.wav"), 1);
        } catch (Exception e) {
            Log.d("AudioManager","No sound assets found - using silent placeholders.");
        }
    }

    public static void init(Context ctx) {
        if (instance==null) instance = new AudioManager(ctx);
    }

    public static AudioManager I() { return instance; }

    public void playPlant() { if (!soundOn) return; if (sPlant!=-1) soundPool.play(sPlant,1,1,1,0,1); }
    public void playHarvest() { if (!soundOn) return; if (sHarvest!=-1) soundPool.play(sHarvest,1,1,1,0,1); }

    public void setSoundEnabled(boolean on) { soundOn = on; }
    public boolean isSoundEnabled() { return soundOn; }

    // Music control (optional)
    public void playMusic(Context ctx, int resId) {
        try {
            if (musicPlayer!=null) { musicPlayer.stop(); musicPlayer.release(); }
            musicPlayer = MediaPlayer.create(ctx, resId);
            musicPlayer.setLooping(true);
            if (soundOn) musicPlayer.start();
        } catch (Exception e) {}
    }
}
