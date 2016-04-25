package spopo.sonas.play;

import spopo.sonas.MainActivity;

/**
 * Created by Luca on 4/20/2016.
 */
public class MapObject {
    protected Pair position;
    protected Pair velocity;
    protected Pair acceleration;
    protected double rotation = 0; //TODO: For future updates where turning may be allowed.
    private float drag = MainActivity.DRAG;
    public MapObject(float x, float y)
    {
        this.position = new Pair(x, y);
        this.velocity = new Pair();
        this.acceleration = new Pair();
    }
    public MapObject(Pair pos)
    {
       this(pos.x, pos.y);
    }

    public void setAcceleration(float x_val, float y_val) {
        this.acceleration.x = x_val;
        this.acceleration.y = y_val;
    }
    public void setAcceleration(Pair val) {
        this.setAcceleration(val.x, val.y);
    }
    public Pair getAcceleration()
    {
        return this.acceleration.clone();
    }

    public void setVelocity(float x_val, float y_val) {
        this.velocity.x = x_val;
        this.velocity.y = y_val;
    }
    public void setVelocity(Pair val) {
        this.setVelocity(val.x, val.y);
    }
    public Pair getVelocity()
    {
        return this.velocity.clone();
    }

    public void setPosition(float x_val, float y_val) {
        this.position.x = x_val;
        this.position.y = y_val;
    }
    public void setPosition(Pair val) {
        this.setPosition(val.x, val.y);
    }
    public Pair getPosition()
    {
        return this.position.clone();
    }

    public void update(float time) {
        this.position.add(this.velocity.clone().mul(time));
        this.velocity.add(this.acceleration.clone().mul(time));
        this.velocity.mul(1-drag);
    }
}
