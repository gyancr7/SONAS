package spopo.sonas;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import spopo.sonas.menu.Menu;
import spopo.sonas.menu.MenuItem;
import spopo.sonas.play.Map;
import spopo.sonas.play.SoundManager;

public class MainActivity extends Activity implements View.OnClickListener, SensorEventListener {

    public static float THRESHOLD_MAP_OBJECT_DISTANCE = 3;
    public static final int[]  MUSIC_TRACKS = new int[3];
    //public static final int[] PROMPTSOUNDS = new int[4];
    public static final float MAX_TARGET_DISTANCE = 30;
    public static final float MIN_TARGET_DISTANCE = 10;
    public static final float TARGTIME = 60000;
    public static final float DRAG = 0.05f;
    public static Menu mainMenu;
    private boolean playflag;
   // public View view;

   @Override
    protected void onCreate(Bundle savedInstanceState) {
       playflag = false;
       super.onCreate(savedInstanceState);
       Context context = this.getApplicationContext();
       SensorManager senman =  (SensorManager)context.getSystemService(SENSOR_SERVICE);
       Sensor rotsen = senman.getDefaultSensor(Sensor.TYPE_GRAVITY); //TODO: Also false as second param, maybe
       senman.registerListener(this, rotsen, SensorManager.SENSOR_DELAY_GAME);

        if (rotsen == null){
           Log.i("Sonas", "NO SENSOR!");
          /*
           finish(); //Emulator always comes here
           return;    //Emulator always comes here
            //*/
       }

       View view = new View(context);
       setContentView(view);
       MUSIC_TRACKS[0] = R.raw.track1;
       MUSIC_TRACKS[1] = R.raw.hyouri;
       //MUSIC_TRACKS[3] = R.raw.uzu;
       MUSIC_TRACKS[2] = R.raw.shianchuu;
       view.setOnClickListener(this);
       SoundManager.init(context);
       //Map.init(context);

    }


    @Override
    public void onStart() {
        super.onStart();
        Menu.init(getApplicationContext());
        MenuItem item1 = new MenuItem() {
            public boolean onSelected(Menu m) {
                Log.i("Sonas", "New Game");
                Map.init(getApplicationContext());
                playflag = true;
                return true;
            }
        };
        MenuItem item2 = new MenuItem() {
            public boolean onSelected(Menu m) {
                Log.i("Sonas", "Instructions");
                SoundManager.play(R.raw.instructions);
                return true;
            }
        };
        MenuItem item3 = new MenuItem() {
            public boolean onSelected(Menu m) {
                Log.i("Sonas", "item3");
                return true;
            }
        };
        mainMenu = new Menu("Main", R.raw.main_menu, new MenuItem[] {item1,
                new Menu("Difficulty", R.raw.instructions, new MenuItem[] {
                new MenuItem() { public boolean onSelected(Menu m) {Log.i("Sonas", "Read"); return true;}},
                }),
                new Menu("Difficulty", R.raw.difficulty, new MenuItem[] {
                        new MenuItem() { public boolean onSelected(Menu m) {Log.i("Sonas", "Set Easy"); THRESHOLD_MAP_OBJECT_DISTANCE = 3; return true;}},
                        new MenuItem() { public boolean onSelected(Menu m) {Log.i("Sonas", "Set Medium"); THRESHOLD_MAP_OBJECT_DISTANCE = 2; return true;}},
                        new MenuItem() { public boolean onSelected(Menu m) {Log.i("Sonas", "Set Hard"); THRESHOLD_MAP_OBJECT_DISTANCE = 1; return true;}}
                })});
        mainMenu.prompt();
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onBackPressed() {
        if (!playflag && !mainMenu.onCancel()) {
            super.onBackPressed();
        }
        else if (playflag) {
            Map.stop();
            playflag = !playflag;
            mainMenu.prompt();
        }
    }

    public void onAccuracyChanged(Sensor s, int i)
    {
        //Nothing
    }

    public void onSensorChanged(SensorEvent se)
    {
        Map.setPlayerInput(se.values[0], se.values[1]);
    }


    public void onClick(View clickedView){
        Log.i("Sonas", "Bumping");
        if (!playflag) { Menu.bump(); }
    }
}
