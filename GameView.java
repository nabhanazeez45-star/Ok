package com.echofarm;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GameView extends SurfaceView implements Runnable, SurfaceHolder.Callback {
    Thread gameThread = null;
    SurfaceHolder holder;
    volatile boolean running = false;
    Paint paint = new Paint();

    int cols = 6;
    int rows = 8;
    Tile[][] farm;

    SharedPreferences prefs;
    int coins = 0;

    long lastUpdateTime = 0;

    public GameView(Context context) {
        super(context);
        holder = getHolder();
        holder.addCallback(this);
        prefs = context.getSharedPreferences("echofarm", Context.MODE_PRIVATE);
        initFarm();
        load();
    }

    void initFarm() {
        farm = new Tile[cols][rows];
        for (int x=0;x<cols;x++){
            for (int y=0;y<rows;y++){
                farm[x][y] = new Tile();
            }
        }
    }

    void load() {
        coins = prefs.getInt("coins", 0);
        String farmJson = prefs.getString("farm_state", "");
        if (farmJson != null && farmJson.length() > 0) {
            try {
                JSONArray arr = new JSONArray(farmJson);
                for (int i=0;i<arr.length() && i<cols*rows;i++) {
                    JSONObject o = arr.getJSONObject(i);
                    int x = o.getInt("x");
                    int y = o.getInt("y");
                    boolean planted = o.getBoolean("planted");
                    float age = (float)o.getDouble("age");
                    boolean mature = o.getBoolean("mature");
                    float growth = (float)o.getDouble("growth");
                    if (x>=0 && x<cols && y>=0 && y<rows) {
                        Tile t = farm[x][y];
                        t.planted = planted;
                        t.age = age;
                        t.mature = mature;
                        t.growthTime = growth;
                    }
                }
            } catch (JSONException e) { e.printStackTrace(); }
        }
    }

    void save() {
        prefs.edit().putInt("coins", coins).apply();
        JSONArray arr = new JSONArray();
        try {
            for (int x=0;x<cols;x++){
                for (int y=0;y<rows;y++){
                    Tile t = farm[x][y];
                    JSONObject o = new JSONObject();
                    o.put("x", x);
                    o.put("y", y);
                    o.put("planted", t.planted);
                    o.put("age", t.age);
                    o.put("mature", t.mature);
                    o.put("growth", t.growthTime);
                    arr.put(o);
                }
            }
            prefs.edit().putString("farm_state", arr.toString()).apply();
        } catch (JSONException e) { e.printStackTrace(); }
    }

    @Override
    public void run() {
        lastUpdateTime = System.currentTimeMillis();
        while (running) {
            if (!holder.getSurface().isValid()) continue;
            long now = System.currentTimeMillis();
            float dt = (now - lastUpdateTime) / 1000f;
            update(dt);
            Canvas c = holder.lockCanvas();
            if (c!=null) {
                drawGame(c);
                holder.unlockCanvasAndPost(c);
            }
            lastUpdateTime = now;
            try { Thread.sleep(16); } catch (InterruptedException e) {}
        }
        save();
    }

    void update(float dt) {
        for (int x=0;x<cols;x++){
            for (int y=0;y<rows;y++){
                Tile t = farm[x][y];
                if (t.hasCrop()) {
                    t.age += dt;
                    if (!t.mature && t.age >= t.growthTime) {
                        t.mature = true;
                    }
                }
            }
        }
    }

    void drawGame(Canvas c) {
        int w = c.getWidth();
        int h = c.getHeight();
        c.drawColor(Color.rgb(170,220,120));
        int cellW = w / cols;
        int cellH = h / rows;
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(36);

        paint.setColor(Color.BLACK);
        c.drawText("Coins: " + coins, 10, 40, paint);

        for (int x=0;x<cols;x++){
            for (int y=0;y<rows;y++){
                int left = x*cellW;
                int top = y*cellH;
                paint.setColor(Color.rgb(200,180,140));
                c.drawRect(left+4, top+4, left+cellW-4, top+cellH-4, paint);
                Tile t = farm[x][y];
                if (t.hasCrop()) {
                    if (t.mature) paint.setColor(Color.GREEN);
                    else paint.setColor(Color.YELLOW);
                    c.drawCircle(left+cellW/2, top+cellH/2, Math.min(cellW,cellH)/4, paint);
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction()==MotionEvent.ACTION_DOWN) {
            int w = getWidth(), h = getHeight();
            int cellW = w / cols;
            int cellH = h / rows;
            int x = (int)(event.getX() / cellW);
            int y = (int)(event.getY() / cellH);
            if (x>=0 && x<cols && y>=0 && y<rows) {
                Tile t = farm[x][y];
                if (!t.hasCrop()) {
                    if (coins >= 1) {
                        coins -= 1;
                        t.plant();
                    } else {
                        // not enough coins - open shop
                        // open shop activity (post a runnable to UI thread)
                        post(new Runnable(){ public void run(){ getContext().startActivity(new android.content.Intent(getContext(), ShopActivity.class)); } });
                    }
                } else {
                    if (t.mature) {
                        coins += t.harvestValue();
                        t.clear();
                    }
                }
            }
            return true;
        }
        return super.onTouchEvent(event);
    }

    public void pause() {
        running = false;
        try { if (gameThread != null) gameThread.join(); } catch (InterruptedException e) {}
    }

    public void resume() {
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override public void surfaceCreated(SurfaceHolder holder) { resume(); }
    @Override public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}
    @Override public void surfaceDestroyed(SurfaceHolder holder) { pause(); }

    public int getCoins() { return coins; }

    static class Tile {
        float age = 0f;
        float growthTime = 5f; // seconds to mature
        boolean mature = false;
        boolean planted = false;

        void plant() { planted = true; age = 0; mature = false; }
        boolean hasCrop() { return planted; }
        int harvestValue() { return 2; }
        void clear() { planted = false; age=0; mature=false; }
    }
}
