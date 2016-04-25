package spopo.sonas.menu;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import spopo.sonas.MainActivity;

/**
 * Created by Luca on 4/23/2016.
 */

public class Menu implements DribbleCounter.onDribbleComplete, MenuItem {
    private String title;
    private int titleSound;
    //private static int[] promptSounds = MainActivity.PROMPTSOUNDS; //TODO: MenuItem stuff
    private MenuItem[] items;
    private Menu prev;
    private Menu child;
    private static Context context;
    private static MediaPlayer mediaPlayer;
    private static DribbleCounter counter;
    public static void init(Context context) {
        Menu.context = context;
    }
    public Menu( String title, int titleSound, MenuItem[] items){
        this.title = title;
        this.titleSound = titleSound;
        this.items = items;
    }
    public void prompt() {
        counter = new DribbleCounter(500, this);
        child = null;
        //counter.dribble();
        mediaPlayer = MediaPlayer.create(context, titleSound);
        mediaPlayer.start();
    }
    public void onSelected (Menu callingMenuItem) {
        prev = callingMenuItem;
        prev.child = this;
        prompt();
    }
    public boolean onCancel () {
        if (child != null) {
            return child.onCancel();
        }
        if (mediaPlayer!=null) {
            try {
                mediaPlayer.stop();
                mediaPlayer.release();
            } catch (IllegalStateException ex) {
            }
        }
        counter.cancel();
        if (prev!=null) {
            prev.prompt();
            return true;
        } else {
            return false;
        }
    }
    public void onDribbleCompleteListener( int count ) {
        if (mediaPlayer!=null) {
            try {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
            catch (IllegalStateException ex) { }
        }
        if (count > items.length) {
            Log.i("Sonas", "Wrong number of taps");
            this.prompt();
        }
        else {
            items[count - 1].onSelected(this);
        }
    }
    public static void bump() {
        if (counter!=null) {
            counter.dribble();
        }
    }
}
