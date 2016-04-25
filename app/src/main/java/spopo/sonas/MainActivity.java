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
import android.widget.TextView;

import spopo.sonas.menu.DribbleCounter;
import spopo.sonas.menu.DribbleCounter.onDribbleComplete;
import spopo.sonas.menu.Menu;
import spopo.sonas.menu.MenuItem;
import spopo.sonas.play.Map;

public class MainActivity extends Activity implements android.view.View.OnClickListener, SensorEventListener {

    public static final float THRESHOLD_MAP_OBJECT_DISTANCE = 3;
    public static final int[]  MUSIC_TRACKS = new int[4];
    //public static final int[] PROMPTSOUNDS = new int[4];
    public static final float MAX_TARGET_DISTANCE = 30;
    public static final float MIN_TARGET_DISTANCE = 10;
    public static final float DRAG = 0.05f;
    public static Menu mainMenu;
   // public View view;

   @Override
    protected void onCreate(Bundle savedInstanceState) {
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
       MUSIC_TRACKS[2] = R.raw.uzu;
       MUSIC_TRACKS[3] = R.raw.shianchuu;
       view.setOnClickListener(this);
       //Map.init(context);

    }


    @Override
    public void onStart() {
        super.onStart();
        Menu.init(getApplicationContext());
        MenuItem item1 = new MenuItem() {
            public void onSelected(Menu m) {
                Log.i("Sonas", "item1");
            }
        };
        MenuItem item2 = new MenuItem() {
            public void onSelected(Menu m) {
                Log.i("Sonas", "item2");
            }
        };
        MenuItem item3 = new MenuItem() {
            public void onSelected(Menu m) {
                Log.i("Sonas", "item3");
            }
        };
        mainMenu = new Menu("Main", R.raw.main_menu, new MenuItem[] {item1,
                new Menu("Two", R.raw.item2, new MenuItem[] {
                        new MenuItem() { public void onSelected(Menu m) {Log.i("Sonas", "2->1");}},
                        new MenuItem() { public void onSelected(Menu m) {Log.i("Sonas", "2->2");}}
                }),
                item3});
        mainMenu.prompt();
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onBackPressed() {
        if (!mainMenu.onCancel()) {
            super.onBackPressed();
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
        Menu.bump();
    }
}
