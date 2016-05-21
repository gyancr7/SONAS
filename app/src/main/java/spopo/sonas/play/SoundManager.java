package spopo.sonas.play;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.util.Log;

import spopo.sonas.MainActivity;
import spopo.sonas.R;

/**
 * Created by Luca on 4/20/2016.
 */
public class SoundManager {
    private static final double HALF_HEAD_SPACE = MainActivity.THRESHOLD_MAP_OBJECT_DISTANCE;
    private static MediaPlayer mediaplayer;
    private static MapObject source; //Also cares about velocity for future doppler effect.
    private static MapObject destination;
    private static Context context;

    public static boolean init (Context context) {
        if (source != null || destination != null || mediaplayer != null || SoundManager.context != null) {
            return false;
        }
        mediaplayer = new MediaPlayer();
        SoundManager.context = context;
        return true;
    }
    public static void destroy() {
        mediaplayer.release();
        mediaplayer = null;
        context = null;
    }
    public static void set(MapObject source, MapObject destination, int track) throws Exception{
        SoundManager.source = source;
        SoundManager.destination = destination;
        mediaplayer.reset();
        mediaplayer.release(); //TODO: Reuse the object
//        AssetFileDescriptor afd = context.getResources().openRawResourceFd(track);
//        if (afd == null) throw new Exception("The track provided failed to load.");
//        mediaplayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
//        afd.close();
  //      mediaplayer.prepare();
        mediaplayer = MediaPlayer.create(context, track);
        update();
        mediaplayer.setLooping(true);
        mediaplayer.start();
        Log.i("Sonas", "Set called for SoundManager.");
    }
    public static void stop() {
        mediaplayer.stop();
    }

    public static void play(int track) {
        mediaplayer.reset();
        mediaplayer.release();
        mediaplayer = MediaPlayer.create(context, track);
        mediaplayer.setLooping(false);
        mediaplayer.start();
        mediaplayer.setVolume(1, 1);
    }

    public static void update() {
        if (source == null || destination == null || mediaplayer == null || SoundManager.context == null) {
            Log.e("Sonas", "SoundManager.update called with uninitialized state");
            return;
        }
        float l_dist, r_dist, l_vol, r_vol;
        Pair ear = destination.getPosition();
        Pair offset = new Pair( (float)( HALF_HEAD_SPACE*Math.cos(destination.rotation) ), (float)( HALF_HEAD_SPACE*Math.sin(destination.rotation) ));
        ear.add(offset);
        l_dist = ear.distance(source.position);
        offset.mul(-2);
        ear.add(offset);
        r_dist = ear.distance(source.position);

        //TODO: Apply transformations
        //l_vol = Math.max(0, 1 - (float)Math.abs(0.5*Math.log10(l_dist)));
        //r_vol = Math.max(0, 1 - (float)Math.abs(0.5*Math.log10(r_dist)));
        l_vol = 10/(10+l_dist);
        r_vol = 10/(10+r_dist);

        mediaplayer.setVolume(l_vol, r_vol);
        Log.i("Sonas", "Volumes: "+l_vol+", "+r_vol);
    }
}
