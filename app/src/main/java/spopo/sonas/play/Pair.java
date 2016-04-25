package spopo.sonas.play;

import android.util.Log;

/**
 * Created by Luca on 4/20/2016.
 */
public class Pair {
    public float x;
    public float y;
    public Pair(){
        this.x = 0;
        this.y = 0;
    }
    public Pair(float x, float y) {
        this.x = x;
        this.y = y;
    }
    public Pair clone() {
        return new Pair(x, y);
    }
    public Pair add(Pair b) {
        this.x += b.x;
        this.y += b.y;
        return this;
    }
    public Pair add(float x, float y) {
        this.x += x;
        this.y += y;
        return this;
    }
    public Pair mul(float m) {
        this.x *= m;
        this.y *= m;
        return this;
    }
    public static float distance(Pair a, Pair b) {
        return (float)Math.pow(
                Math.pow(a.x-b.x, 2)
                        + Math.pow(a.y - b.y, 2),
                0.5);
    }
    public float distance(Pair b) {
        return this.distance(this, b);
    }

}
