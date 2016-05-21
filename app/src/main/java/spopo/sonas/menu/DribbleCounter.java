package spopo.sonas.menu;


import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Luca on 4/17/2016.
 */

public class DribbleCounter extends ScheduledThreadPoolExecutor implements Runnable {

    public interface onDribbleComplete {
        public void onDribbleCompleteListener(int n);
    }

    private int count;
    private int thresholdTime;
    ScheduledFuture scheduled;
    onDribbleComplete handler;
    public DribbleCounter(int thresholdTime, onDribbleComplete handler) {
        super(1);
        count = 0;
        this.thresholdTime = thresholdTime;
        this.handler = handler;
        //scheduled = this.schedule(this, thresholdTime, TimeUnit.MILLISECONDS);
    }

    public void dribble(){
        if (scheduled != null) {
            scheduled.cancel(true);
        }
        scheduled = this.schedule(this, thresholdTime, TimeUnit.MILLISECONDS);
        count += 1;
    }

    public void cancel() {
        if (scheduled != null)
        {  scheduled.cancel(true); }
        this.remove(this);
        this.purge();
    }

    public void run(){
        cancel();
        this.handler.onDribbleCompleteListener(count);
    }
}
