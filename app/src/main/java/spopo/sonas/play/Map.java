package spopo.sonas.play;

import android.content.Context;
import android.util.Log;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import spopo.sonas.MainActivity;
import spopo.sonas.menu.Menu;
import spopo.sonas.menu.MenuItem;

/**
 * Created by Luca on 4/20/2016.
 */
public class Map implements Runnable {
    private static Target target;
    private static Player player;
    private static int score;
    private static float timeLeft = MainActivity.TARGTIME;
    private static int track = 0;
    private static int[] tracks;
    private static double stamp;
    private static Map mapobj;
    private static ScheduledThreadPoolExecutor scheduler;
    public static void init(Context context) {
        timeLeft = MainActivity.TARGTIME;
        scheduler = new ScheduledThreadPoolExecutor(1);
        target = new Target(0,0);
        player = new Player(0, 0);
        Map.tracks = MainActivity.MUSIC_TRACKS;

        newTarget();
        if (mapobj == null) {mapobj = new Map();}
        scheduler.scheduleAtFixedRate(mapobj, 0, 100, TimeUnit.MILLISECONDS);
    }

    public  static  void stop() {
        scheduler.remove(mapobj);
        scheduler.purge();
        SoundManager.stop();
    }

    public static void changeTrack() {
        tracks = MainActivity.MUSIC_TRACKS;
        int ntrack = (int) (Math.random() * (tracks.length-1));
        if (ntrack>=track) {
            track = ntrack + 1;
        }
        else {
            track = ntrack;
        }
    }
    public static void newTarget(){
        player.setPosition(0, 0);
        float theta = (float)(Math.random()*Math.PI*2);
        float r = MainActivity.MIN_TARGET_DISTANCE +
                (float)Math.random()* (MainActivity.MAX_TARGET_DISTANCE - MainActivity.MIN_TARGET_DISTANCE );
        target.setPosition((float)Math.cos(theta)*r,(float) Math.sin(theta)*r);
        changeTrack();
        try {
            SoundManager.set(target, player, MainActivity.MUSIC_TRACKS[ track ]);
        }
        catch (Exception exc) {
            Log.e("Sonas", exc.toString());
        }
    }
    public void run()
    {
        update();
    }
    public static void update() {
        float time = 100;
        player.update(time/1000);
        SoundManager.update();
        Log.i("Sonas", "Player position: " + player.position.x + ", " + player.position.y);
        Log.i("Sonas", "Target position: " + target.position.x + ", " + target.position.y);
        Log.i("Sonas", "Distance: " + player.position.distance(target.position));
        if (player.position.distance(target.position) < MainActivity.THRESHOLD_MAP_OBJECT_DISTANCE) {
            score++;
            timeLeft+=MainActivity.TARGTIME; //TODO: Dynamic, or at least not magic
            newTarget();
        }
        timeLeft-=time;
        Log.i("Sonas", "Time Left: "+timeLeft);
        if (timeLeft < 0) { stop();};
    }
    public static void setPlayerInput(float x, float y) {
        if (player!=null) {player.setVelocity(x, y); }
    }
    public static void setPlayerInput(Pair val) {
        Map.setPlayerInput(val.x, val.y);
    }
}
