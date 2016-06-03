package com.constantbeta.showplayer;

import java.util.Timer;
import java.util.TimerTask;

public class ShowTimer
{
    public interface Listener
    {
        void onTick(int curTime);
        void onDone();
    }

    private final Listener listener;
    private final int      tickSize;
    private final double   speed;
    private final Timer    timer      = new Timer();
    private final int      totalDuration;

    private TimerTask      timerTask;
    private int            curTime;


    public ShowTimer(Listener listener, int totalDuration, double speed, int tickSize)
    {
        this.listener      = listener;
        this.totalDuration = totalDuration;
        this.speed         = speed;
        this.tickSize      = tickSize;
        setTime(0);
    }

    public void start()
    {
        scheduleNextTick();
    }

    public void pause()
    {
        if (null != timerTask)
        {
            timerTask.cancel();
            timerTask = null;
        }
    }

    public void setTime(int elapsedTime)
    {
        curTime = elapsedTime;
        listener.onTick(curTime);
    }

    private void scheduleNextTick()
    {
        assert null == timerTask;

        timerTask = new TimerTask()
        {
            @Override
            public void run()
            {
                timerTask = null;

                if (curTime + tickSize > totalDuration)
                {
                    listener.onTick(totalDuration);
                    listener.onDone();
                }
                else
                {
                    curTime += tickSize;
                    listener.onTick(curTime);
                    scheduleNextTick();
                }
            }
        };

        timer.schedule(timerTask, (int)(tickSize / speed));
    }
}
